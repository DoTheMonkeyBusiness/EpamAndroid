package com.example.epamandroid.mvp.views.compoundviews

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.epamandroid.R

class LostBreedDescriptionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        orientation = VERTICAL
        inflate(getContext(), R.layout.lost_breed_description_view, this)
    }


}