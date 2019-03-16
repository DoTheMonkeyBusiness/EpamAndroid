package com.example.epamandroid.base

import android.content.Context
import android.support.annotation.LayoutRes

interface ICompoundView {

    @get:LayoutRes
    val layoutResId: Int

    fun onViewInflated(pContext: Context)
}

