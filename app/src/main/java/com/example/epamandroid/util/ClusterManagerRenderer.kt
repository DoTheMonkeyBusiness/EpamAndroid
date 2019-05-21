package com.example.epamandroid.util

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.ImageView
import com.example.epamandroid.R
import com.example.epamandroid.models.ClusterMarker
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator

class ClusterManagerRenderer(
    context: Context?,
    map: GoogleMap?,
    clusterManager: ClusterManager<ClusterMarker>?
) : DefaultClusterRenderer<ClusterMarker>(
    context,
    map,
    clusterManager) {

    private var iconGenerator: IconGenerator
    private var imageView: ImageView
    private var markerWidth: Int?
    private var markerHeight: Int?

    init {
        iconGenerator = IconGenerator(context?.applicationContext)
        imageView = ImageView(context?.applicationContext)
        markerWidth = context?.resources?.getDimension(R.dimen.custom_marker_image)?.toInt()
        markerHeight = context?.resources?.getDimension(R.dimen.custom_marker_image)?.toInt()
        imageView.layoutParams = markerWidth?.let { markerHeight?.let { it1 -> ViewGroup.LayoutParams(it, it1) } }
        val padding: Int? = context?.resources?.getDimension(R.dimen.custom_marker_padding)?.toInt()
        padding?.let { imageView.setPadding(it, it, it, it) }
        iconGenerator.setContentView(imageView)
    }

    override fun onBeforeClusterItemRendered(item: ClusterMarker?, markerOptions: MarkerOptions?) {
//        super.onBeforeClusterItemRendered(item, markerOptions)
        item?.iconPicture?.let { imageView.setImageResource(it) }
        val icon: Bitmap = iconGenerator.makeIcon()
        markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(icon))?.title(item?.title)

    }

    override fun shouldRenderAsCluster(cluster: Cluster<ClusterMarker>?): Boolean {
        return false
    }
}