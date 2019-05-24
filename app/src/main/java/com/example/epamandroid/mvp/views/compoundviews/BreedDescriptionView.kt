package com.example.epamandroid.mvp.views.compoundviews

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.epamandroid.R
import com.example.epamandroid.constants.DogEntityConstants
import com.example.epamandroid.models.DogEntity
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

    fun getDogPhoto(): ImageView = lostBreedDescriptionDogPhoto

    fun updateDogInfo(bundle: Bundle?) {
        when(bundle?.getString(DogEntityConstants.BREED_EXTRA_KEY)){
            null -> {lostBreedDescriptionHeader.setText(R.string.dog_breed)}
            else -> {lostBreedDescriptionHeader?.text = bundle.getString(DogEntityConstants.BREED_EXTRA_KEY)}
        }
        when(bundle?.getString(DogEntityConstants.DESCRIPTION_EXTRA_KEY)){
            null -> {breedDescriptionText.setText(R.string.no_description)}
            else -> {breedDescriptionText?.text = bundle.getString(DogEntityConstants.DESCRIPTION_EXTRA_KEY)}
        }
        when(bundle?.getFloat(DogEntityConstants.BREED_POPULARITY_EXTRA_KEY)){
            null -> {breedDescriptionRatingBar?.rating = 0F}
            else -> {breedDescriptionRatingBar?.rating = bundle.getFloat(DogEntityConstants.BREED_POPULARITY_EXTRA_KEY)}
        }

        breedDescriptionGridView.updateDogGridInfo(
                bundle?.getString(DogEntityConstants.HEIGHT_EXTRA_KEY),
                bundle?.getString(DogEntityConstants.WEIGHT_EXTRA_KEY),
                bundle?.getString(DogEntityConstants.LIFE_EXPENTANCY_EXTRA_KEY),
                bundle?.getInt(DogEntityConstants.COST_EXTRA_KEY),
                bundle?.getBoolean(DogEntityConstants.CAN_LIVE_AT_HOME_EXTRA_KEY),
                bundle?.getBoolean(DogEntityConstants.AFFECTIONATE_EXTRA_KEY)

        )

    }
    fun updateDogInfo(dogEntity: DogEntity?) {


        when(dogEntity?.breed){
            null -> {lostBreedDescriptionHeader.setText(R.string.dog_breed)}
            else -> {lostBreedDescriptionHeader.text = dogEntity.breed}
        }
        when(dogEntity?.description){
            null -> {breedDescriptionText?.setText(R.string.no_description)}
            else -> {breedDescriptionText?.text = dogEntity.description}
        }
        when(dogEntity?.breedPopularity){
            null -> {breedDescriptionRatingBar?.rating = 0F}
            else -> {breedDescriptionRatingBar?.rating = dogEntity.breedPopularity}
        }

        breedDescriptionGridView.updateDogGridInfo(
                dogEntity?.height,
                dogEntity?.weight,
                dogEntity?.lifeExpectancy,
                dogEntity?.cost,
                dogEntity?.isCanLiveAtHome,
                dogEntity?.isAffectionate

        )
    }
}