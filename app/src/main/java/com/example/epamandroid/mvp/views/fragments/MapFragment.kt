package com.example.epamandroid.mvp.views.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.epamandroid.R
import com.example.epamandroid.mvp.views.activities.AddLostDogActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private const val TAG: String = "MapFragment"
        private const val LOST_DOG_TITLE_KEY: String = "addLostDog"
        private const val DEFAULT_ZOOM_KEY: Float = 15F
        private const val LOCATION_PERMISSION_KEY: Int = 1778
    }

    private var lostDogsMap: GoogleMap? = null
    private var locationPermissionsGranted: Boolean = false
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var lostDogMarker: Marker? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLocationPermission()
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragmentMapView) as SupportMapFragment

        mapFragment.getMapAsync(this@MapFragment)
    }

    private fun getLocationPermission() {
        val permissions: Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_DENIED
                && context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } == PackageManager.PERMISSION_DENIED
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, LOCATION_PERMISSION_KEY)
        } else {
            locationPermissionsGranted = true
            initMap()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        locationPermissionsGranted = false

        when (requestCode) {
            LOCATION_PERMISSION_KEY -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    locationPermissionsGranted = true
                    initMap()
                } else {
                    locationPermissionsGranted = false
                }
            }
        }

    }

    private fun getDeviceLocation() {
        fusedLocationProviderClient = context?.let { LocationServices.getFusedLocationProviderClient(it) }

        try {
            if (locationPermissionsGranted) {
                val location = fusedLocationProviderClient?.lastLocation
                location?.addOnCompleteListener{
                    if (it.isSuccessful){
                        val currentLocation = it.result as Location

                        moveCamera(LatLng(currentLocation.latitude, currentLocation.longitude), DEFAULT_ZOOM_KEY)
                    } else {
                        Log.d(TAG, "current location is null")
                        Toast.makeText(context, getString(R.string.unable_location), Toast.LENGTH_LONG).show()
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, e.message)
        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        lostDogsMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun setOnMapClickListener() {
        lostDogsMap?.setOnMapClickListener {
            val marker = MarkerOptions().position(it).title(LOST_DOG_TITLE_KEY)

            lostDogMarker?.remove()
            lostDogMarker = lostDogsMap?.addMarker(marker)
        }
    }

    private fun setOnMarkerClickListener() {
        lostDogsMap?.setOnMarkerClickListener {
            if(it.title == LOST_DOG_TITLE_KEY){
                startActivity(Intent(context, AddLostDogActivity::class.java))
            }
            true
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        lostDogsMap = map

        if(locationPermissionsGranted){
            getDeviceLocation()

            if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED
                    && context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED
            ){
                return
            }
            lostDogsMap?.isMyLocationEnabled = true

            setOnMapClickListener()
            setOnMarkerClickListener()
        }
    }
}