package com.example.epamandroid.views.compoundviews

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.dog_view.view.*

class DogView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.dog_view, this)
    }

    fun setDogBreed(breed: String): DogView {
        dogBreedTextView.text = breed
        return this
    }
    fun isCanLiveAtHome(isCanLiveAtHome: Boolean): DogView {
        when {
            isCanLiveAtHome -> isCanLiveAtHomeIcon.setImageResource(R.drawable.ic_check)
            else -> isCanLiveAtHomeIcon.setImageResource(R.drawable.ic_close)
        }
        return this
    }

    fun isAffectionate(isAffectionate: Boolean): DogView {
        when {
            isAffectionate -> isCanLiveAtHomeIcon.setImageResource(R.drawable.ic_check)
            else -> isCanLiveAtHomeIcon.setImageResource(R.drawable.ic_close)
        }
        return this
    }
}