package com.example.epamandroid.mvp.views.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epamandroid.R
import com.example.epamandroid.constants.MapRestaurantEntityConstants.LOST_RESTAURANT_ENTITY_EXTRA_KEY
import com.example.epamandroid.models.MapRestaurantEntity
import com.example.epamandroid.mvp.contracts.IMapTypeDescriptionContract
import com.example.epamandroid.mvp.presenters.MapTypeDescriptionPresenter
import com.example.imageloader.IMichelangelo
import com.example.imageloader.Michelangelo
import com.example.kotlinextensions.goneView
import com.example.kotlinextensions.visibleView
import kotlinx.android.synthetic.main.map_type_description_fragment.*
import kotlinx.android.synthetic.main.map_type_description_view.*

class MapTypeDescriptionFragment
    : Fragment(),
        IMapTypeDescriptionContract.View {

    companion object {
        private const val TELEPHONE_SCHEME_KEY: String = "tel"
    }

    private lateinit var michelangelo: IMichelangelo
    private lateinit var presenter: IMapTypeDescriptionContract.Presenter

    private var mapTypeDescriptionResultCallback: IMapTypeDescriptionResultCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is IMapTypeDescriptionResultCallback) {
            mapTypeDescriptionResultCallback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        michelangelo = Michelangelo.getInstance(context)
        presenter = MapTypeDescriptionPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_type_description_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val bundleMapRestaurantEntity = bundle?.getParcelable<MapRestaurantEntity?>(LOST_RESTAURANT_ENTITY_EXTRA_KEY)

        mapTypeDescriptionFragment.goneView()

        if (bundle != null && bundleMapRestaurantEntity != null) {
            mapTypeDescriptionFragment.updateRestaurantInfo(bundleMapRestaurantEntity)
            michelangelo.load(mapTypeDescriptionFragment.getMapTypePhoto(), bundleMapRestaurantEntity.photo)
            mapTypeDescriptionFragment.visibleView()
            mapTypeDescriptionResultCallback?.onDescriptionConfirm()
        } else {
            mapTypeDescriptionResultCallback?.onDescriptionError()
        }

        mapTypeDescriptionPhoneNumber.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts(TELEPHONE_SCHEME_KEY, mapTypeDescriptionPhoneNumber.text.toString(), null)))
        }
    }

    interface IMapTypeDescriptionResultCallback {
        fun onDescriptionConfirm()
        fun onDescriptionLoading()
        fun onDescriptionError()
    }
}