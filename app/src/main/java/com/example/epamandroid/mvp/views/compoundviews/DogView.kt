package com.example.epamandroid.mvp.views.compoundviews

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.dog_view.view.*

class DogView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
//        layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        inflate(getContext(), R.layout.dog_view, this)
    }

    fun getDogIcon(): ImageView = dogIcon
    fun setDogBreed(breed: String?): DogView {

        when (breed){
            null -> {dogBreedTextView?.setText(R.string.error)}
            else -> {dogBreedTextView?.text = breed}
        }

        return this
    }
    fun isCanLiveAtHome(isCanLiveAtHome: Boolean?): DogView {
        when {
            isCanLiveAtHome == null -> { dogViewCanLiveAtHomeIcon.setImageResource(R.drawable.ic_close)}
            isCanLiveAtHome -> dogViewCanLiveAtHomeIcon.setImageResource(R.drawable.ic_check)
            else -> dogViewCanLiveAtHomeIcon.setImageResource(R.drawable.ic_close)
        }
        return this
    }

    fun isLikes(isLikes: Boolean): DogView {
        when {
            isLikes -> dogIsLikes.setImageResource(R.drawable.ic_star)
            else -> dogIsLikes.setImageResource(R.drawable.ic_star_border)
        }
        return this
    }
}