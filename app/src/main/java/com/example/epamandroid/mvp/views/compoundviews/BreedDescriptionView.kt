package com.example.epamandroid.mvp.views.compoundviews

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.epamandroid.R
import com.example.epamandroid.models.RestaurantEntity
import kotlinx.android.synthetic.main.type_description_view.view.*

class TypeDescriptionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        orientation = VERTICAL
        inflate(getContext(), R.layout.type_description_view, this)
        typeDescriptionRatingBar.setIsIndicator(true)
    }

    fun getTypePhoto(): ImageView = typeDescriptionRestaurantPhoto

//    fun updateRestaurantInfo(bundle: Bundle?) {
//        when(bundle?.getString(RestaurantEntityConstants.TYPE_EXTRA_KEY)){
//            null -> {mapTypeDescriptionHeader.setText(R.string.restaurant_type)}
//            else -> {mapTypeDescriptionHeader?.text = bundle.getString(RestaurantEntityConstants.TYPE_EXTRA_KEY)}
//        }
//        when(bundle?.getString(RestaurantEntityConstants.DESCRIPTION_EXTRA_KEY)){
//            null -> {typeDescriptionText.setText(R.string.no_description)}
//            else -> {typeDescriptionText?.text = bundle.getString(RestaurantEntityConstants.DESCRIPTION_EXTRA_KEY)}
//        }
//        when(bundle?.getFloat(RestaurantEntityConstants.TYPE_POPULARITY_EXTRA_KEY)){
//            null -> {typeDescriptionRatingBar?.rating = 0F}
//            else -> {typeDescriptionRatingBar?.rating = bundle.getFloat(RestaurantEntityConstants.TYPE_POPULARITY_EXTRA_KEY)}
//        }
//
//        typeDescriptionGridView.updateRestaurantGridInfo(
//                bundle?.getString(RestaurantEntityConstants.HEIGHT_EXTRA_KEY),
//                bundle?.getString(RestaurantEntityConstants.WEIGHT_EXTRA_KEY),
//                bundle?.getString(RestaurantEntityConstants.LIFE_EXPENTANCY_EXTRA_KEY),
//                bundle?.getInt(RestaurantEntityConstants.COST_EXTRA_KEY),
//                bundle?.getBoolean(RestaurantEntityConstants.CAN_LIVE_AT_HOME_EXTRA_KEY),
//                bundle?.getBoolean(RestaurantEntityConstants.AFFECTIONATE_EXTRA_KEY)
//
//        )
//
//    }

    fun updateRestaurantInfo(restaurantEntity: RestaurantEntity?) {


        when (restaurantEntity?.type) {
            null -> typeDescriptionHeader.setText(R.string.restaurant_type)
            else -> typeDescriptionHeader.text = restaurantEntity.type
        }
        when (restaurantEntity?.description) {
            null -> typeDescriptionText?.setText(R.string.no_description)
            else -> typeDescriptionText?.text = restaurantEntity.description
        }
        when (restaurantEntity?.typePopularity) {
            null -> typeDescriptionRatingBar?.rating = 0F
            else -> typeDescriptionRatingBar?.rating = restaurantEntity.typePopularity
        }

        typeDescriptionGridView.updateRestaurantGridInfo(
                restaurantEntity?.height,
                restaurantEntity?.weight,
                restaurantEntity?.lifeExpectancy,
                restaurantEntity?.cost,
                restaurantEntity?.isCanLiveAtHome,
                restaurantEntity?.isAffectionate

        )
    }
}