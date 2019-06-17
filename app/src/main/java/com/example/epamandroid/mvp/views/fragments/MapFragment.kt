package com.example.epamandroid.mvp.views.fragments

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import com.example.epamandroid.constants.MapConstants.LATITUDE_EXTRA_KEY
import com.example.epamandroid.constants.MapConstants.LONGITUDE_EXTRA_KEY
import com.example.epamandroid.constants.PermissionsConstants.LOCATION_PERMISSION_EXTRA_KEY
import com.example.epamandroid.models.ClusterMarker
import com.example.epamandroid.models.LostDogEntity
import com.example.epamandroid.mvp.contracts.IMapContract
import com.example.epamandroid.mvp.presenters.MapPresenter
import com.example.epamandroid.mvp.views.activities.AddLostDogActivity
import com.example.epamandroid.mvp.services.LocationService
import com.example.epamandroid.util.ClusterManagerRenderer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager


class MapFragment : Fragment(), OnMapReadyCallback, IMapContract.View {

    companion object {
        private const val TAG: String = "MapFragment"
        private const val LOST_DOG_TITLE_KEY: String = "addLostDog"
        private const val DEFAULT_ZOOM_KEY: Float = 15F
    }

    private var lostDogsMap: GoogleMap? = null
    private var locationPermissionsGranted: Boolean = false
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var lostDogMarker: Marker? = null
    private var clusterManager: ClusterManager<ClusterMarker>? = null
    private var clusterManagerRenderer: ClusterManagerRenderer? = null
    private var clusterMarkersSet: HashSet<ClusterMarker> = hashSetOf()
    private var showBottomSheetCallback: IShowBottomSheetCallback? = null

    private lateinit var mapPresenter: IMapContract.Presenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is IShowBottomSheetCallback) {
            showBottomSheetCallback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapPresenter = MapPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mapPresenter.onCreate()

        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLocationPermission()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !locationPermissionsGranted) {
            getLocationPermission()
        }
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragmentMapView) as SupportMapFragment

        mapFragment.getMapAsync(this@MapFragment)
    }

    private fun getLocationPermission() {
        val permissions: Array<String> =
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (context?.let {
                    ContextCompat.checkSelfPermission(
                            it,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } == PackageManager.PERMISSION_DENIED
                && context?.let {
                    ContextCompat.checkSelfPermission(
                            it,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                } == PackageManager.PERMISSION_DENIED
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, LOCATION_PERMISSION_EXTRA_KEY)
        } else {
            locationPermissionsGranted = true
            initMap()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        locationPermissionsGranted = false

        when (requestCode) {
            LOCATION_PERMISSION_EXTRA_KEY -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    locationPermissionsGranted = true
                    initMap()
                } else {
                    locationPermissionsGranted = false
                }
            }
        }

    }

    private fun getUserLocation() {
        fusedLocationProviderClient = context?.let { LocationServices.getFusedLocationProviderClient(it) }

        try {
            val location = fusedLocationProviderClient?.lastLocation
            location?.addOnCompleteListener {
                val currentLocation = it.result

                if (currentLocation != null) {
                    moveCamera(LatLng(currentLocation.latitude, currentLocation.longitude), DEFAULT_ZOOM_KEY)
                } else {
                    Log.d(TAG, "current location is null")
                    Toast.makeText(context, getString(R.string.unable_location), Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, e.message)
        }
    }

    private fun moveCamera(latLng: LatLng?, zoom: Float) {
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
        lostDogsMap?.setOnMarkerClickListener { marker ->
            if (marker.title == LOST_DOG_TITLE_KEY) {
                startActivity(Intent(context, AddLostDogActivity::class.java).apply {
                    putExtra(LATITUDE_EXTRA_KEY, marker.position.latitude)
                    putExtra(LONGITUDE_EXTRA_KEY, marker.position.longitude)
                })
            } else {
                val lostDogEntity = clusterMarkersSet.find {
                    it.title == marker.title
                }?.lostDogEntity

                showBottomSheetCallback?.onShowBottomSheetFromMap(lostDogEntity)
            }
            true
        }
    }

    override fun addMapMarker(clusterMarker: ClusterMarker) {
        if (lostDogsMap != null) {
            if (clusterManager == null) {
                clusterManager = ClusterManager(activity?.applicationContext, lostDogsMap)
            }

            val manager = clusterManager
            val appContext = activity?.applicationContext
            val googleMap = lostDogsMap

            if (clusterManagerRenderer == null) {
                clusterManagerRenderer = ClusterManagerRenderer(appContext, googleMap, manager)
                manager?.renderer = clusterManagerRenderer
            }

            clusterMarkersSet.add(clusterMarker)
            manager?.addItem(clusterMarker)

            manager?.cluster()
        }
    }

    override fun removeMapMarkers(clusterMarkers: HashSet<ClusterMarker>) {
        if (lostDogsMap != null) {
            val manager = clusterManager

            clusterMarkers.forEach{
                clusterMarkersSet.remove(it)
                manager?.removeItem(it)
            }

            manager?.cluster()
        }
    }

    override fun getMarkersSet(): HashSet<ClusterMarker> = clusterMarkersSet

    override fun onMapReady(map: GoogleMap?) {

        lostDogsMap = map

        if (locationPermissionsGranted) {
            getUserLocation()
            mapPresenter.findLostDogsNearby()

            startLocationService()

            if (context?.let {
                        ActivityCompat.checkSelfPermission(
                                it,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    } != PackageManager.PERMISSION_GRANTED
                    && context?.let {
                        ActivityCompat.checkSelfPermission(
                                it,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    } != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            lostDogsMap?.isMyLocationEnabled = true

            setOnMapClickListener()
            setOnMarkerClickListener()
        }
    }

    private fun startLocationService() {
        if (!isLocationServiceRunning(LocationService::class.java)) {
            val serviceIntent = Intent(context, LocationService::class.java)

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context?.startForegroundService(serviceIntent)
            } else {
                context?.startService(serviceIntent)
            }
        }

    }

    private fun isLocationServiceRunning(serviceClass: Class<LocationService>): Boolean {
        val activityManager: ActivityManager? = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?

        if (activityManager != null) {
            for (service: ActivityManager.RunningServiceInfo in activityManager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
        }
        return false
    }

    override fun onStop() {
        super.onStop()

        mapPresenter.onDestroy()
    }

    interface IShowBottomSheetCallback {
        fun onShowBottomSheetFromMap(lostDogEntity: LostDogEntity?)
    }
}