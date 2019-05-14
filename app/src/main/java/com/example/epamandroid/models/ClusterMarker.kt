package com.example.epamandroid.models

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ClusterMarker(
    position: LatLng,
    title: String,
    snippet: String,
    iconPicture: Int,
    lostDogEntity: LostDogEntity
) : ClusterItem {
    override fun getSnippet(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTitle(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPosition(): LatLng {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}