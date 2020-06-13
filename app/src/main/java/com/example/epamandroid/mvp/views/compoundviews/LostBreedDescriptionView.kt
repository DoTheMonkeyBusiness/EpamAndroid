package com.example.epamandroid.mvp.views.compoundviews

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.epamandroid.R
import com.example.epamandroid.models.MapRestaurantEntity
import kotlinx.android.synthetic.main.map_type_description_view.view.*

class MapTypeDescriptionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        orientation = VERTICAL
        inflate(getContext(), R.layout.map_type_description_view, this)
    }

    fun getMapTypePhoto(): ImageView = mapTypeDescriptionRestaurantPhoto

    fun updateRestaurantInfo(mapRestaurantEntity: MapRestaurantEntity?) {

        when (mapRestaurantEntity?.type) {
            null -> mapTypeDescriptionHeader.setText(R.string.restaurant_type)
            else -> mapTypeDescriptionHeader.text = mapRestaurantEntity.type
        }

        when (mapRestaurantEntity?.phoneNumber) {
            null -> mapTypeDescriptionPhoneNumber?.setText(R.string.phone_number)
            else -> mapTypeDescriptionPhoneNumber?.text = mapRestaurantEntity.phoneNumber
        }

        when (mapRestaurantEntity?.description) {
            null -> mapTypeDescriptionText?.setText(R.string.no_description)
            else -> mapTypeDescriptionText?.text = mapRestaurantEntity.description
        }
    }

}