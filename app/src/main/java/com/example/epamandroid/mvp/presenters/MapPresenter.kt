package com.example.epamandroid.mvp.presenters

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.epamandroid.constants.MapConstants.MAP_FASTEST_INTERVAL
import com.example.epamandroid.constants.MapConstants.MAP_RADIUS_EXTRA_KEY
import com.example.epamandroid.constants.MapConstants.MAP_UPDATE_INTERVAL
import com.example.epamandroid.gsonmodels.GsonMapRestaurantEntity
import com.example.epamandroid.models.ClusterMarker
import com.example.epamandroid.models.MapRestaurantEntity
import com.example.epamandroid.mvp.contracts.IMapContract
import com.example.epamandroid.mvp.repository.Repository
import com.example.imageloader.Michelangelo
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

class MapPresenter(private val view: IMapContract.View) : IMapContract.Presenter {

    companion object {
        private const val TAG: String = "MapPresenter"
    }

    private val repository: IMapContract.Model = Repository
    private val handler = Handler(Looper.getMainLooper())
    private val michelangelo = Michelangelo.getInstance(view.getContext())

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            val deviceLocation = locationResult?.lastLocation

            if (deviceLocation != null) {
                Log.d(TAG, "update map location")
                findRestaurants(LatLng(deviceLocation.latitude, deviceLocation.longitude))
            }
        }
    }

    override fun onCreate() {
        val context = view.getContext()

        if (context != null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        }
    }

    override fun findMapRestaurantsNearby() {
        val locationRequestHighAccuracy = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = MAP_UPDATE_INTERVAL
            fastestInterval = MAP_FASTEST_INTERVAL
        }

        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequestHighAccuracy,
            locationCallback,
            Looper.myLooper()
        )
    }

    private fun findRestaurants(deviceLocation: LatLng) {
        Thread {
            val mapRestaurantsSet: HashSet<MapRestaurantEntity> = hashSetOf()
            val gsonMapRestaurantsMap: HashMap<String, GsonMapRestaurantEntity>? = repository
                .getEntitiesNearby(deviceLocation, MAP_RADIUS_EXTRA_KEY)

            gsonMapRestaurantsMap?.forEach {
                mapRestaurantsSet.add(
                    MapRestaurantEntity(
                        it.value.id,
                        it.value.type,
                        it.value.phoneNumber,
                        it.value.description,
                        it.value.latitude?.let { it1 -> it.value.longitude?.let { it2 -> LatLng(it1, it2) } },
                        it.value.photo
                    )
                )
            }

            removeClusterMarkers(mapRestaurantsSet)

            if (mapRestaurantsSet.isNotEmpty()) {
                createClusterMarkers(mapRestaurantsSet)
            }
        }.start()
    }

    private fun removeClusterMarkers(mapRestaurantsNearbySet: HashSet<MapRestaurantEntity>) {
        val clusterMarkersSet: HashSet<ClusterMarker> = hashSetOf()
        view.getMarkersSet().forEach { marker ->
            if (mapRestaurantsNearbySet.firstOrNull { it.id?.equals(marker.title) == true } == null) {
                clusterMarkersSet.add(marker)
            }
        }
        handler.post {
            view.removeMapMarkers(clusterMarkersSet)
        }
    }

    private fun createClusterMarkers(mapRestaurantsNearbyList: HashSet<MapRestaurantEntity>) {
        mapRestaurantsNearbyList.forEach {
            val marker = ClusterMarker(
                it.position,
                it.id.toString(),
                it.type,
                michelangelo.loadSync(it.photo),
                it
            )
            handler.post {
                view.addMapMarker(marker)
            }
        }
    }

    override fun onDestroy() {
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }
}