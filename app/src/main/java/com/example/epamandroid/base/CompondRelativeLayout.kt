package com.example.epamandroid.base

import android.content.Context
import android.util.AttributeSet
import android.view.InflateException
import android.view.View
import android.widget.RelativeLayout

abstract class CompoundRelativeLayout : RelativeLayout, ICompoundView {

    init {
        val context = context

        if (context == null) {
            throw InflateException("Context was null before inflation")
        } else {
            View.inflate(context, layoutResId, this)
            onViewInflated(context)
        }
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defAttrs: Int) : super(context, attrs, defAttrs) {}
}
