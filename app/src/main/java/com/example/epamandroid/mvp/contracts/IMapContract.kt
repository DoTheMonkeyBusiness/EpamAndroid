package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.models.ClusterMarker
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView
import com.google.android.gms.maps.model.LatLng

interface IMapContract {
    interface View : IBaseView {
        fun addMapMarkers(clusterMarkerSet: HashSet<ClusterMarker>?)
    }

    interface Presenter : IBasePresenter {
        fun findLostDogsNearby(userPosition: LatLng)
    }

    interface Model {
        fun getEntitiesNearby(latitude: Double, radius: Float): HashMap<String, GsonLostDogEntity>?
    }
}