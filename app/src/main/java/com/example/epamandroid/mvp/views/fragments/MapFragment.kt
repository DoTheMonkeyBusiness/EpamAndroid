package com.example.epamandroid.mvp.views.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epamandroid.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng


class MapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private const val TAG: String = "MapFragment"
        private const val LOCATION_PERMISSION_KEY: Int = 1778
    }

    private var lostDogsMap: GoogleMap? = null
    private var locationPermissionsGranted: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLocationPermission()
    }

    private fun initMap(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragmentMapView) as SupportMapFragment

        mapFragment.getMapAsync(this@MapFragment)
    }

    private fun getLocationPermission(){
        val permissions: Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_DENIED
                && context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } == PackageManager.PERMISSION_DENIED
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, LOCATION_PERMISSION_KEY)
        } else {
            locationPermissionsGranted = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       locationPermissionsGranted = false

        when(requestCode) {
            LOCATION_PERMISSION_KEY -> {
                if (grantResults.isNotEmpty() && grantResults.all{ it == PackageManager.PERMISSION_GRANTED}){
                    locationPermissionsGranted = true
                    initMap()
                } else {
                    locationPermissionsGranted = false
                }
            }
        }

    }

    override fun onMapReady(map: GoogleMap?) {
        lostDogsMap = map

    }
}