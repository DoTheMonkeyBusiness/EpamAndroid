package com.example.epamandroid.mvp.presenters

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.constants.MapConstants.MAP_RADIUS_EXTRA_KEY
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.models.ClusterMarker
import com.example.epamandroid.models.LostDogEntity
import com.example.epamandroid.mvp.contracts.IMapContract
import com.example.epamandroid.mvp.repository.Repository
import com.example.imageloader.Michelangelo
import com.google.android.gms.maps.model.LatLng


class MapPresenter(private val view: IMapContract.View) : IMapContract.Presenter {

    private val repository: IMapContract.Model = Repository
    private val handler = Handler(Looper.getMainLooper())
    private val michelangelo = Michelangelo.getInstance(view.getContext())

    override fun onCreate() = Unit

    override fun findLostDogsNearby(userPosition: LatLng) {
        Thread {
            val lostDogsList: ArrayList<LostDogEntity>? = arrayListOf()
            val gsonLostDogsMap: HashMap<String, GsonLostDogEntity>? = repository
                .getEntitiesNearby(userPosition, MAP_RADIUS_EXTRA_KEY)

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

            if (lostDogsList != null) {
                createClusterMarkers(lostDogsList)
            }
        }.start()
    }

    private fun createClusterMarkers(lostDogsNearbyList: ArrayList<LostDogEntity>) {
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

    override fun onDestroy() = Unit
}