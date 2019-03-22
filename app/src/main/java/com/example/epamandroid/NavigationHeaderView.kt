package com.example.epamandroid

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.navigation_header_view.view.*

class NavigationHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    init {
        inflate(getContext(), R.layout.navigation_header_view, this)
    }

    fun updateIconColor(colorHex: String?) {
        navigationHeaderAndroidIcon
            .setColorFilter(
                Color
                    .parseColor(colorHex)
            )
    }
}