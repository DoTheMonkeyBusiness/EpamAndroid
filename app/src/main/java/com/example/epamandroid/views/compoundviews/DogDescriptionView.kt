package com.example.epamandroid.views.compoundviews

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.breed_description_view.view.*

class DogDescriptionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.breed_description_view, this)
    }

    fun setDogBreed(breed: String): DogDescriptionView {
        breedDescriptionHeader.text = breed
        return this
    }
    fun isCanLiveAtHome(isCanLiveAtHome: Boolean): DogDescriptionView {
        when {
            isCanLiveAtHome -> breedDescriptionCanLiveAtHomeIcon.setImageResource(R.drawable.ic_check)
            else -> breedDescriptionCanLiveAtHomeIcon.setImageResource(R.drawable.ic_close)
        }
        return this
    }

    fun isAffectionate(isAffectionate: Boolean): DogDescriptionView {
        when {
            isAffectionate -> breedDescriptionAffectionateIcon.setImageResource(R.drawable.ic_check)
            else -> breedDescriptionAffectionateIcon.setImageResource(R.drawable.ic_close)
        }
        return this
    }

}