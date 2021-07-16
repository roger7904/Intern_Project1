package com.example.intern_project1.view.activities

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.intern_project1.R
import com.example.intern_project1.BitmapHelper
import com.example.intern_project1.MarkerInfoWindowAdapter
import com.example.intern_project1.place.Place
import com.example.intern_project1.place.PlaceRenderer
import com.example.intern_project1.place.PlacesReader
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad


class MapActivity : AppCompatActivity() , OnMapReadyCallback {

    private val places: List<Place> by lazy {
        PlacesReader(this).read()
    }

    // [START maps_android_add_map_codelab_ktx_coroutines]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        lifecycleScope.launchWhenCreated {
            // Get map
            val googleMap = mapFragment.awaitMap()

            addClusteredMarkers(googleMap)

            // Wait for map to finish loading
            googleMap.awaitMapLoad()

            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this@MapActivity, R.raw.style
                )
            )

            // Ensure all places are visible in the map
            val bounds = LatLngBounds.builder()
            places.forEach { bounds.include(it.latLng) }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
        }
    }
    // [END maps_android_add_map_codelab_ktx_coroutines]

    /**
     * Adds markers to the map with clustering support.
     */
    private fun addClusteredMarkers(googleMap: GoogleMap) {
        // Create the ClusterManager class and set the custom renderer
        val clusterManager = ClusterManager<Place>(this, googleMap)
        clusterManager.renderer =
            PlaceRenderer(
                this,
                googleMap,
                clusterManager
            )

        // Set custom info window adapter
        clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))

        // Add the places to the ClusterManager
        clusterManager.addItems(places)
        clusterManager.cluster()

        // Show polygon
//        clusterManager.setOnClusterItemClickListener { item ->
//            addCircle(googleMap, item)
//            return@setOnClusterItemClickListener false
//        }

        // When the camera starts moving, change the alpha value of the marker to translucent
        googleMap.setOnCameraMoveStartedListener {
            clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
        }

        googleMap.setOnCameraIdleListener {
            // When the camera stops moving, change the alpha value back to opaque
            clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }

            // Call clusterManager.onCameraIdle() when the camera stops moving so that re-clustering
            // can be performed when the camera stops moving
            clusterManager.onCameraIdle()
        }
    }

    private var circle: Circle? = null

    // [START maps_android_add_map_codelab_ktx_add_circle]
    /**
     * Adds a [Circle] around the provided [item]
     */
//    private fun addCircle(googleMap: GoogleMap, item: Place) {
//        circle?.remove()
//        circle = googleMap.addCircle {
//            center(item.latLng)
//            radius(1000.0)
//            fillColor(ContextCompat.getColor(this@MapActivity, R.color.colorPrimaryTranslucent))
//            strokeColor(ContextCompat.getColor(this@MapActivity, R.color.colorPrimary))
//        }
//    }
    // [END maps_android_add_map_codelab_ktx_add_circle]

    private val bicycleIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.main_color)
        BitmapHelper.vectorToBitmap(this, R.drawable.ic_baseline_radio_button_checked_24, color)
    }

    // [START maps_android_add_map_codelab_ktx_add_markers]
    /**
     * Adds markers to the map. These markers won't be clustered.
     */
    private fun addMarkers(googleMap: GoogleMap) {
        places.forEach { place ->
            val marker = googleMap.addMarker {
                title(place.name)
                position(place.latLng)
                icon(bicycleIcon)
            }
            // Set place as the tag on the marker object so it can be referenced within
            // MarkerInfoWindowAdapter
            marker.tag = place
        }
    }
    // [END maps_android_add_map_codelab_ktx_add_markers]

    companion object {
        val TAG = MapActivity::class.java.simpleName
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready for use.
     */


    override fun onMapReady(googleMap: GoogleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.style
                )
            )
            Log.i(TAG, "123")
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
        // Position the map's camera near Sydney, Australia.
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(-34, 151)))
    }


}