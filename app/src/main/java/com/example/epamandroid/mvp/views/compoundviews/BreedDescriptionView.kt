package com.example.epamandroid.mvp.views.compoundviews

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.epamandroid.R
import com.example.epamandroid.constants.DogEntitieConstants
import kotlinx.android.synthetic.main.breed_description_view.view.*

class BreedDescriptionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        orientation = VERTICAL
        inflate(getContext(), R.layout.breed_description_view, this)
        breedDescriptionRatingBar.setIsIndicator(true)
    }

    fun updateDogInfo(bundle: Bundle?) {
        if (bundle != null) {
            breedDescriptionHeader.text = bundle.getString(DogEntitieConstants.breed)
            breedDescriptionRatingBar.rating = bundle.getFloat(DogEntitieConstants.breedPopularity)
            breedDescriptionText.text = bundle.getString(DogEntitieConstants.description)

            breedDescriptionGridView.updateDogGridInfo(
                    bundle.getBoolean(DogEntitieConstants.canLiveAtHome),
                    bundle.getBoolean(DogEntitieConstants.affectionate),
                    bundle.getString(DogEntitieConstants.height),
                    bundle.getString(DogEntitieConstants.weight),
                    bundle.getString(DogEntitieConstants.lifeExpectancy),
                    bundle.getInt(DogEntitieConstants.cost)

            )
        }
    }
}