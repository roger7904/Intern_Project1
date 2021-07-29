package com.example.intern_project1.view.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.intern_project1.utils.BitmapHelper
import com.example.intern_project1.view.adapter.MarkerInfoWindowAdapter
import com.example.intern_project1.R
import com.example.intern_project1.base.BaseState
import com.example.intern_project1.databinding.ActivityMapBinding
import com.example.intern_project1.model.entities.FactoryObject
import com.example.intern_project1.utils.Injection
import com.example.intern_project1.viewmodel.FactoryViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.ktx.addMarker

class MapActivity : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private lateinit var mbinding: ActivityMapBinding
    private lateinit var mFactoryViewModel: FactoryViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var cameraPosition: CameraPosition? = null
    private val defaultLocation = LatLng(25.0, 121.0)
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        mbinding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(mbinding.root)

        setSupportActionBar(mbinding.toolbar)

        mFactoryViewModel = ViewModelProvider(this, Injection.provideFactoryViewModel(this)).get(
            FactoryViewModel::class.java)

        mFactoryViewModel.getFactoryInfoFromApi()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.back -> {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map=googleMap

        map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.style
            )
        )

        factoryViewModelObserver()

        // Prompt the user for permission.
        getLocationPermission()

        // Turn on the My Location layer and the related control on the map.
        updateLocationUISetting()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
    }

    private fun factoryViewModelObserver() {

        mFactoryViewModel.state.observe(this){
            when (it) {
                is BaseState.Success -> {
                    it?.let {

                        addMarkers(map,it.data.data.data)

                        Log.i("FactoryInfo Response", "${it.data.data.data[0]}")
                    }
                }
                is BaseState.Error -> {
                    it?.let {
                        //do some error action
                        Log.i("API Error", "${it.message}")
                    }
                }
                is BaseState.Loading -> {
                    it?.let {
                        //do some loading action
                        Log.i("Loading", "true")
                    }
                }
            }
        }
    }

    private val factoryIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(
            this,
            R.color.main_color
        )
        BitmapHelper.vectorToBitmap(
            this,
            R.drawable.ic_baseline_radio_button_checked_24,
            color
        )
    }

    private fun addMarkers(googleMap: GoogleMap,factoryPlace: List<FactoryObject.DataX>) {
        factoryPlace.forEach { result ->
            val marker = googleMap.addMarker {
                title(result.name)
                position(LatLng(result.latitude.toDouble(),result.longitude.toDouble()))
                icon(factoryIcon)
            }
            // Set place as the tag on the marker object so it can be referenced within
            // MarkerInfoWindowAdapter
            marker.tag = result
            googleMap.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

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
        updateLocationUISetting()
    }

    private fun updateLocationUISetting() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map.isMyLocationEnabled = true
                map.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map.isMyLocationEnabled = false
                map.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
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
        private const val DEFAULT_ZOOM = 11
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }
}