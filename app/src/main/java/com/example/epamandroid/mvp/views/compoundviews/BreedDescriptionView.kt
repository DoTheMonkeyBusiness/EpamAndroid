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

    fun getDogPhoto(): ImageView = breedDescriptionDogPhoto

    fun updateDogInfo(bundle: Bundle?) {
        when(bundle?.getString(DogEntityConstants.breed)){
            null -> {breedDescriptionHeader.setText(R.string.dog_breed)}
            else -> {breedDescriptionHeader?.text = bundle.getString(DogEntityConstants.breed)}
        }
        when(bundle?.getString(DogEntityConstants.description)){
            null -> {breedDescriptionText.setText(R.string.no_description)}
            else -> {breedDescriptionText?.text = bundle.getString(DogEntityConstants.description)}
        }
        when(bundle?.getFloat(DogEntityConstants.breedPopularity)){
            null -> {breedDescriptionRatingBar?.rating = 0F}
            else -> {breedDescriptionRatingBar?.rating = bundle.getFloat(DogEntityConstants.breedPopularity)}
        }

        breedDescriptionGridView.updateDogGridInfo(
                bundle?.getString(DogEntityConstants.height),
                bundle?.getString(DogEntityConstants.weight),
                bundle?.getString(DogEntityConstants.lifeExpectancy),
                bundle?.getInt(DogEntityConstants.cost),
                bundle?.getBoolean(DogEntityConstants.canLiveAtHome),
                bundle?.getBoolean(DogEntityConstants.affectionate)

        )

    }
    fun updateDogInfo(dogEntity: DogEntity?) {


        when(dogEntity?.breed){
            null -> {breedDescriptionHeader.setText(R.string.dog_breed)}
            else -> {breedDescriptionHeader.text = dogEntity.breed}
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