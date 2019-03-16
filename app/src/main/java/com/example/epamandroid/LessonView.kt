package com.example.epamandroid

import android.content.Context
import android.support.annotation.UiThread
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import com.example.epamandroid.base.CompoundRelativeLayout

class LessonView : CompoundRelativeLayout {

    private var dateView: TextView? = null
    private var themeView: TextView? = null
    private var avatarView: ImageView? = null

    private var attributeDate: String? = null
    private var attributeTheme: String? = null

    val layoutResId: Int
        get() = R.layout.view_lesson

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        parseAttributes(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defAttrs: Int) : super(context, attrs, defAttrs) {

        parseAttributes(context, attrs)
    }


    fun onViewInflated(pContext: Context) {
        dateView = findViewById(R.id.dateView)
        themeView = findViewById(R.id.themeView)
        avatarView = findViewById(R.id.avatarView)
    }

    @UiThread
    fun setLessonDate(pDate: String): LessonView {
        dateView!!.text = pDate
        return this
    }

    @UiThread
    fun setLessonTheme(pTheme: String): LessonView {
        themeView!!.text = pTheme
        return this
    }

    private fun parseAttributes(pContext: Context, pAttrs: AttributeSet) {
        val theme = pContext.theme
        val styledAttributes = theme.obtainStyledAttributes(pAttrs, R.styleable.LessonView, 0, 0)

        try {
            attributeDate = styledAttributes.getString(R.styleable.LessonView_lessonDate)
            attributeTheme = styledAttributes.getString(R.styleable.LessonView_lessonTheme)

            dateView!!.text = attributeDate
            themeView!!.text = attributeTheme
        } finally {
            styledAttributes.recycle()
        }
    }
}
