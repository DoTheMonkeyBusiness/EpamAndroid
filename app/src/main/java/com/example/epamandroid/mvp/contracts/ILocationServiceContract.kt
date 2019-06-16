package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.google.android.gms.maps.model.LatLng

interface ILocationServiceContract {
    interface Service

    interface Model {
        fun getEntitiesNearby(userPosition: LatLng, radius: Float): HashMap<String, GsonLostDogEntity>?
    }
}