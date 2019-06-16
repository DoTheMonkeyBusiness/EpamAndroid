package com.example.epamandroid.mvp.presenters

import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.epamandroid.constants.MapConstants.MAP_RADIUS_EXTRA_KEY
import com.example.epamandroid.constants.MapConstants.UPDATE_PAUSE_EXTRA_KEY
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.models.ClusterMarker
import com.example.epamandroid.models.LostDogEntity
import com.example.epamandroid.mvp.contracts.IMapContract
import com.example.epamandroid.mvp.repository.Repository
import com.example.imageloader.Michelangelo
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class MapPresenter(private val view: IMapContract.View) : IMapContract.Presenter {

    companion object {
        private const val TAG: String = "MapPresenter"
    }

    private val repository: IMapContract.Model = Repository
    private val handler = Handler(Looper.getMainLooper())
    private val michelangelo = Michelangelo.getInstance(view.getContext())

    private var isUpdateClusterMarkers = false

    override fun onCreate() = Unit

    override fun findLostDogsNearby() {
        Thread {
            isUpdateClusterMarkers = true

            while (isUpdateClusterMarkers) {
                getDeviceLocation()

                Thread.sleep(UPDATE_PAUSE_EXTRA_KEY)
            }
        }.start()
    }

    private fun findDogs(deviceLocation: LatLng) {
        val lostDogsSet: HashSet<LostDogEntity> = hashSetOf()
        val gsonLostDogsMap: HashMap<String, GsonLostDogEntity>? = repository
                .getEntitiesNearby(deviceLocation, MAP_RADIUS_EXTRA_KEY)

        gsonLostDogsMap?.forEach {
            lostDogsSet.add(
                    LostDogEntity(
                            it.value.id,
                            it.value.breed,
                            it.value.phoneNumber,
                            it.value.description,
                            it.value.latitude?.let { it1 -> it.value.longitude?.let { it2 -> LatLng(it1, it2) } },
                            it.value.photo
                    )
            )
        }

        if (lostDogsSet.isNotEmpty()) {
            removeClusterMarkers(lostDogsSet)
            createClusterMarkers(lostDogsSet)
        }
    }

    private fun removeClusterMarkers(lostDogsNearbySet: HashSet<LostDogEntity>) {
        val clusterMarkersSet: HashSet<ClusterMarker> = hashSetOf()
        view.getMarkersSet().forEach { marker ->
            if (lostDogsNearbySet.firstOrNull { it.id?.equals(marker.title) == true } == null) {
                clusterMarkersSet.add(marker)
            }
        }
        handler.post {
            view.removeMapMarkers(clusterMarkersSet)
        }
    }

    private fun createClusterMarkers(lostDogsNearbyList: HashSet<LostDogEntity>) {
        lostDogsNearbyList.forEach {
            val marker = ClusterMarker(
                    it.position,
                    it.id.toString(),
                    it.breed,
                    michelangelo.loadSync(it.photo),
                    it
            )
            handler.post {
                view.addMapMarker(marker)
            }
        }
    }

    private fun getDeviceLocation() {
        val fusedLocationProviderClient = view.getContext()?.let { LocationServices.getFusedLocationProviderClient(it) }

        val locationManager = view.getContext()?.getSystemService(LOCATION_SERVICE) as LocationManager
        try {
//            val location = fusedLocationProviderClient?.lastLocation
//            val currentLocation = location?.result
            val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (currentLocation != null) {
                findDogs(LatLng(currentLocation.latitude, currentLocation.longitude))
            }
//            location?.addOnCompleteListener {
//                val currentLocation = it.result
//
//                if (currentLocation != null) {
//                    findDogs(LatLng(currentLocation.latitude, currentLocation.longitude))
//                }
//            }
        } catch (e: SecurityException) {
            Log.e(TAG, e.message)
        }
    }

    override fun onDestroy() {
        isUpdateClusterMarkers = false
    }
}