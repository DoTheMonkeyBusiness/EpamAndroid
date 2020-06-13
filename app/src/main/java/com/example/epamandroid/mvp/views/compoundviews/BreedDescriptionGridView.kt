package com.example.epamandroid.mvp.views.compoundviews

import android.content.Context
import android.util.AttributeSet
import android.widget.GridLayout
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.type_description_grid_view.view.*

class TypeDescriptionGridView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : GridLayout(context, attrs, defStyleAttr) {

    init {

        orientation = HORIZONTAL
        columnCount = 2
        inflate(getContext(), R.layout.type_description_grid_view, this)
    }

    fun updateRestaurantGridInfo(
            height: String?,
            weight: String?,
            lifeExpectancy: String?,
            cost: Int?,
            isCanLiveAtHome: Boolean?,
            isAffectionate: Boolean?
    ) {
        when (isCanLiveAtHome) {
            true -> restaurantViewCanLiveAtHomeIcon.setImageResource(R.drawable.ic_check)
            false -> restaurantViewCanLiveAtHomeIcon.setImageResource(R.drawable.ic_close)
            null -> restaurantViewCanLiveAtHomeIcon.setImageResource(R.drawable.ic_close)
        }
        when (isAffectionate) {
            true -> typeDescriptionAffectionateIcon.setImageResource(R.drawable.ic_check)
            false -> typeDescriptionAffectionateIcon.setImageResource(R.drawable.ic_close)
            null -> typeDescriptionAffectionateIcon.setImageResource(R.drawable.ic_close)
        }
        when(height){
            null -> {typeDescriptionHeightText?.setText(R.string.dash)}
            else -> {typeDescriptionHeightText?.text = height}
        }
        when(weight){
            null -> {typeDescriptionWeightText?.setText(R.string.dash)}
            else -> {typeDescriptionWeightText?.text = weight}
        }
        when(lifeExpectancy){
            null -> {typeDescriptionLifeExpectancyText?.setText(R.string.dash)}
            else -> {typeDescriptionLifeExpectancyText?.text = lifeExpectancy}
        }
        when(cost){
            null -> {typeDescriptionCostText?.setText(R.string.dash)}
            else -> {typeDescriptionCostText?.text = cost.toString()}
        }
    }

}