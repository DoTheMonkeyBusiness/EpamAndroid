package com.example.epamandroid.mvp.presenters

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.constants.MapConstants.EARTH_RADIUS_EXTRA_KEY
import com.example.epamandroid.constants.MapConstants.RADIUS_EXTRA_KEY
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.models.ClusterMarker
import com.example.epamandroid.models.LostDogEntity
import com.example.epamandroid.mvp.contracts.IMapContract
import com.example.epamandroid.mvp.repository.Repository
import com.google.android.gms.maps.model.LatLng


class MapPresenter(private val view: IMapContract.View) : IMapContract.Presenter {

    private val repository: IMapContract.Model = Repository
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() = Unit

    private fun calculationByDistance(startPosition: LatLng, endPosition: LatLng): Double {
        val lat1 = startPosition.latitude
        val lat2 = endPosition.latitude
        val lon1 = startPosition.longitude
        val lon2 = endPosition.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2))
        val c: Double = 2 * Math.asin(Math.sqrt(a))

        return EARTH_RADIUS_EXTRA_KEY * c
    }

    override fun findLostDogsNearby(userPosition: LatLng) {
        Thread {
            val lostDogsList: ArrayList<LostDogEntity>? = arrayListOf()
            val lostDogsNearbyList: ArrayList<LostDogEntity> = arrayListOf()
            val gsonLostDogsMap: HashMap<String, GsonLostDogEntity>? = repository
                .getEntitiesNearby(userPosition.latitude, RADIUS_EXTRA_KEY)

            gsonLostDogsMap?.forEach {
                lostDogsList?.add(
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

            lostDogsList?.forEach {
                if (it.position != null
                    && calculationByDistance(userPosition, it.position) <= RADIUS_EXTRA_KEY
                ) {
                    lostDogsNearbyList.add(it)
                }
            }
            handler.post {
                Runnable {
                    view.addMapMarkers(createClusterMarkers(lostDogsNearbyList))
                }.run()
            }

        }.start()
    }

    private fun createClusterMarkers(lostDogsNearbyList: ArrayList<LostDogEntity>) : HashSet<ClusterMarker>? {
        val clusterMarkerSet: HashSet<ClusterMarker>? = hashSetOf()
        lostDogsNearbyList.forEach {
            clusterMarkerSet?.add(
                ClusterMarker(
                    it.position,
                    it.id.toString(),
                    it.breed,
                    it.photo,
                    it
                )
            )
        }
        return clusterMarkerSet
    }

    override fun onDestroy() = Unit
}