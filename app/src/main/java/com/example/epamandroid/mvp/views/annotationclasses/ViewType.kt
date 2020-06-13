package com.example.epamandroid.mvp.views.annotationclasses

import android.support.annotation.IntDef

@IntDef(ViewType.RESTAURANT, ViewType.LOADING)
annotation class ViewType {
    companion object {
        const val RESTAURANT = 0
        const val LOADING = 1
    }
}