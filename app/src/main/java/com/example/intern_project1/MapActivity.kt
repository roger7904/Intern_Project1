package com.example.intern_project1.view.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.intern_project1.BitmapHelper
import com.example.intern_project1.MarkerInfoWindowAdapter
import com.example.intern_project1.R
import com.example.intern_project1.databinding.ActivityMainBinding
import com.example.intern_project1.databinding.ActivityMapBinding
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import com.example.intern_project1.place.Place
import com.example.intern_project1.place.PlaceRenderer
import com.example.intern_project1.place.PlacesReader
import com.example.intern_project1.utils.Injection
import com.example.intern_project1.viewmodel.FactoryViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.addMarker
import com.google.android.libraries.places.api.net.PlacesClient


class MapActivity : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var mbinding: ActivityMapBinding
    private lateinit var mFactoryViewModel: FactoryViewModel

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null

    private val places: List<Place> by lazy {
        PlacesReader(this).read()
    }

    // [START maps_android_add_map_codelab_ktx_coroutines]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mbinding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(mbinding.root)

        mFactoryViewModel = ViewModelProvider(this, Injection.provideFactoryViewModel(this)).get(
            FactoryViewModel::class.java)

        mFactoryViewModel.getFactoryInfoFromApi()

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        lifecycleScope.launchWhenCreated {
//            // Get map
//            val googleMap = mapFragment.awaitMap()
//
//            addClusteredMarkers(googleMap)
//
//            // Wait for map to finish loading
//            googleMap.awaitMapLoad()
//
//            googleMap.setMapStyle(
//                MapStyleOptions.loadRawResourceStyle(
//                    this@MapActivity, R.raw.style
//                )
//            )
//
//            // Ensure all places are visible in the map
//            val bounds = LatLngBounds.builder()
//            places.forEach { bounds.include(it.latLng) }
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
//        }
    }
    // [END maps_android_add_map_codelab_ktx_coroutines]

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready for use.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        //addClusteredMarkers(googleMap)

        factoryViewModelObserver(googleMap)

        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.style
            )
        )

//        googleMap.setOnMapLoadedCallback {
//            // Ensure all places are visible in the map
//            val bounds = LatLngBounds.builder()
//            places.forEach { bounds.include(it.latLng) }
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
//        }

//        val australiaBounds = LatLngBounds(
//            LatLng((-44.0), 113.0),  // SW bounds
//            LatLng((-10.0), 154.0) // NE bounds
//        )
//        map.moveCamera(CameraUpdateFactory.newLatLngBounds(australiaBounds, 0))

        // Position the map's camera near Sydney, Australia.
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(-34, 151)))

        // Prompt the user for permission.
        getLocationPermission()
        // [END_EXCLUDE]

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI(googleMap)

        // Get the current location of the device and set the position of the map.
        getDeviceLocation(googleMap)
    }

    private fun factoryViewModelObserver(googleMap: GoogleMap) {

        mFactoryViewModel.response.observe(
            this,
            Observer { response ->
                response?.let {

                    addMarkers(googleMap,response.data.data)

                    Log.i("FactoryInfo Response", "${response.data.data[0]}")
                }
            })

        mFactoryViewModel.loadingError.observe(
            this,
            Observer { dataError ->
                dataError?.let {
                    Log.i("API Error", "$dataError")
                }
            })

        mFactoryViewModel.loading.observe(this,
            Observer { loading ->
                loading?.let {
                    Log.i("Loading", "$loading")
                }
            })
    }

    /**
     * Adds markers to the map with clustering support.
     */
    private fun addClusteredMarkers(googleMap: GoogleMap,factoryPlace: List<FactoryObject.DataX>) {
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
    private fun addMarkers(googleMap: GoogleMap,factoryPlace: List<FactoryObject.DataX>) {
        factoryPlace.forEach { result ->
            val marker = googleMap.addMarker {
                title(result.name)
                position(LatLng(result.latitude.toDouble(),result.longitude.toDouble()))
                icon(bicycleIcon)
            }
            // Set place as the tag on the marker object so it can be referenced within
            // MarkerInfoWindowAdapter
            marker.tag = result
            googleMap.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))
        }
    }


    // [END maps_android_add_map_codelab_ktx_add_markers]

    /**
     * Prompts the user for permission to use the device location.
     */
    // [START maps_current_place_location_permission]
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    // [START maps_current_place_on_request_permissions_result]
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionGranted = false

        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
        //updateLocationUI()
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    // [START maps_current_place_update_location_ui]
    private fun updateLocationUI(map :GoogleMap) {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    // [START maps_current_place_get_device_location]
    private fun getDeviceLocation(map :GoogleMap) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                            Log.i(TAG, "Current location is "
                                    +lastKnownLocation!!.latitude+lastKnownLocation!!.longitude)
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    companion object {
        private val TAG = MapActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
}