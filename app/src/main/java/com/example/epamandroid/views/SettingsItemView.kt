package com.example.epamandroid.views

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.settings_item_compound_view.view.*

class SettingsItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    init {
        setPadding(64, 16, 64, 16)
        inflate(getContext(), R.layout.settings_item_compound_view, this)
        attrs?.let { parseAttributes(context, it) }
    }

    private fun parseAttributes(context: Context, attrs: AttributeSet) {
        val theme = context.theme
        val styledAttributes = theme.obtainStyledAttributes(attrs, R.styleable.SettingsItemView, 0, 0)

        try {
            settingsItemText.text = styledAttributes
                    .getString(
                            R
                                    .styleable
                                    .SettingsItemView_itemText
                    )
            settingsItemIcon
                    .setImageDrawable(
                            styledAttributes
                                    .getDrawable(
                                            R
                                                    .styleable
                                                    .SettingsItemView_itemIcon)
                    )

        } finally {
            styledAttributes.recycle()
        }
    }
}