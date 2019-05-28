package com.example.epamandroid.util

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.ImageView
import com.example.epamandroid.R
import com.example.epamandroid.models.ClusterMarker
import com.example.imageloader.IMichelangelo
import com.example.imageloader.Michelangelo
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
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

    private var iconGenerator: IconGenerator = IconGenerator(context?.applicationContext)
    private var imageView: ImageView = ImageView(context?.applicationContext)
    private var markerWidth: Int? = context?.resources?.getDimension(R.dimen.custom_marker_image)?.toInt()
    private var markerHeight: Int? = context?.resources?.getDimension(R.dimen.custom_marker_image)?.toInt()
    private var michelangelo: IMichelangelo = Michelangelo.getInstance(context)


    init {
        imageView.layoutParams = markerWidth?.let { markerHeight?.let { it1 -> ViewGroup.LayoutParams(it, it1) } }
        val padding: Int? = context?.resources?.getDimension(R.dimen.custom_marker_padding)?.toInt()
        padding?.let { imageView.setPadding(it, it, it, it) }
        iconGenerator.setContentView(imageView)
    }

    override fun onBeforeClusterItemRendered(item: ClusterMarker?, markerOptions: MarkerOptions?) {
        imageView.setImageBitmap(item?.iconPicture)
        val icon: Bitmap = iconGenerator.makeIcon()
        markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(icon))?.title(item?.title)

    }

    override fun shouldRenderAsCluster(cluster: Cluster<ClusterMarker>?): Boolean {
        return false
    }
}