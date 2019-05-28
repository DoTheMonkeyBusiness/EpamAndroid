package com.example.epamandroid.mvp.views.compoundviews

import android.content.Context
import android.util.AttributeSet
import android.widget.GridLayout
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.breed_description_grid_view.view.*

class BreedDescriptionGridView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : GridLayout(context, attrs, defStyleAttr) {

    init {

        orientation = HORIZONTAL
        columnCount = 2
        inflate(getContext(), R.layout.breed_description_grid_view, this)
    }

    fun updateDogGridInfo(
            height: String?,
            weight: String?,
            lifeExpectancy: String?,
            cost: Int?,
            isCanLiveAtHome: Boolean?,
            isAffectionate: Boolean?
    ) {
        when (isCanLiveAtHome) {
            true -> dogViewCanLiveAtHomeIcon.setImageResource(R.drawable.ic_check)
            false -> dogViewCanLiveAtHomeIcon.setImageResource(R.drawable.ic_close)
            null -> dogViewCanLiveAtHomeIcon.setImageResource(R.drawable.ic_close)
        }
        when (isAffectionate) {
            true -> breedDescriptionAffectionateIcon.setImageResource(R.drawable.ic_check)
            false -> breedDescriptionAffectionateIcon.setImageResource(R.drawable.ic_close)
            null -> breedDescriptionAffectionateIcon.setImageResource(R.drawable.ic_close)
        }
        when(height){
            null -> {breedDescriptionHeightText?.setText(R.string.dash)}
            else -> {breedDescriptionHeightText?.text = height}
        }
        when(weight){
            null -> {breedDescriptionWeightText?.setText(R.string.dash)}
            else -> {breedDescriptionWeightText?.text = weight}
        }
        when(lifeExpectancy){
            null -> {breedDescriptionLifeExpectancyText?.setText(R.string.dash)}
            else -> {breedDescriptionLifeExpectancyText?.text = lifeExpectancy}
        }
        when(cost){
            null -> {breedDescriptionCostText?.setText(R.string.dash)}
            else -> {breedDescriptionCostText?.text = cost.toString()}
        }
    }

}