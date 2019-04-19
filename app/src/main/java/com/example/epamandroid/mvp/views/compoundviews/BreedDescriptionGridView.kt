package com.example.epamandroid.mvp.views.compoundviews

import android.content.Context
import android.util.AttributeSet
import android.widget.GridLayout
import com.example.epamandroid.R

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

}