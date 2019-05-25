package com.example.epamandroid.mvp.views.compoundviews

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.epamandroid.R
import com.example.epamandroid.models.LostDogEntity
import kotlinx.android.synthetic.main.lost_breed_description_view.view.*

class LostBreedDescriptionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        orientation = VERTICAL
        inflate(getContext(), R.layout.lost_breed_description_view, this)
    }

    fun getLostBreedPhoto(): ImageView = lostBreedDescriptionDogPhoto

    fun updateDogInfo(lostDogEntity: LostDogEntity?) {

        when (lostDogEntity?.breed) {
            null -> lostBreedDescriptionHeader.setText(R.string.dog_breed)
            else -> lostBreedDescriptionHeader.text = lostDogEntity.breed
        }

        when (lostDogEntity?.phoneNumber) {
            null -> lostBreedDescriptionPhoneNumber?.setText(R.string.phone_number)
            else -> lostBreedDescriptionPhoneNumber?.text = lostDogEntity.phoneNumber
        }

        when (lostDogEntity?.description) {
            null -> lostBreedDescriptionText?.setText(R.string.no_description)
            else -> lostBreedDescriptionText?.text = lostDogEntity.description
        }
    }

}