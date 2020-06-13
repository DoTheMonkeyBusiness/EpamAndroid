package com.example.epamandroid.models

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ClusterMarker (
    private var position: LatLng?,
    private var title: String?,
    private var snippet: String?,
    var iconPicture: Bitmap?,
    var mapRestaurantEntity: MapRestaurantEntity?
) : ClusterItem {
    override fun getSnippet(): String? = snippet
    override fun getTitle(): String? = title
    override fun getPosition(): LatLng? = position
}