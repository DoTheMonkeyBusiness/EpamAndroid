package com.example.epamandroid

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class ImageNewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.new_image_item, this)
    }
}