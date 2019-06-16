package com.example.epamandroid.mvp.contracts

import android.content.Context
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.models.ClusterMarker
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView
import com.google.android.gms.maps.model.LatLng

interface IMapContract {
    interface View : IBaseView {
        fun addMapMarker(clusterMarker: ClusterMarker)
        fun removeMapMarkers(clusterMarkers: HashSet<ClusterMarker>)
        fun getContext(): Context?
        fun getMarkersSet(): HashSet<ClusterMarker>
    }

    interface Presenter : IBasePresenter {
        fun findLostDogsNearby()
    }

    interface Model {
        fun getEntitiesNearby(userPosition: LatLng, radius: Float): HashMap<String, GsonLostDogEntity>?
    }
}