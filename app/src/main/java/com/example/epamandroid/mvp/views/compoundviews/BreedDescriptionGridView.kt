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
        columnCount = 4
        inflate(getContext(), R.layout.breed_description_grid_view, this)
//        breedDescriptionCanLiveAtHome.visibility = View.GONE
//        breedDescriptionCanLiveAtHomeIcon.visibility = View.GONE
//        breedDescriptionAffectionate.visibility = View.GONE
//        breedDescriptionAffectionateIcon.visibility = View.GONE
//        breedDescriptionHeight.visibility = View.GONE
//        breedDescriptionHeightText.visibility = View.GONE
//        breedDescriptionWeight.visibility = View.GONE
//        breedDescriptionWeightText.visibility = View.GONE

    }

    fun updateDogGridInfo(
            isCanLiveAtHome: Boolean,
            isAffectionate: Boolean,
            height: String,
            weight: String,
            lifeExpectancy: String,
            cost: Int
    ) {
        when {
            isCanLiveAtHome -> breedDescriptionCanLiveAtHomeIcon.setImageResource(R.drawable.ic_check)
            else -> breedDescriptionCanLiveAtHomeIcon.setImageResource(R.drawable.ic_close)
        }

        when {
            isAffectionate -> breedDescriptionAffectionateIcon.setImageResource(R.drawable.ic_check)
            else -> breedDescriptionAffectionateIcon.setImageResource(R.drawable.ic_close)
        }

        breedDescriptionHeightText.text = height
        breedDescriptionWeightText.text = weight
        breedDescriptionLifeExpectancyText.text = lifeExpectancy
        breedDescriptionCostText.text = cost.toString()

    }

}