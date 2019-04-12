package com.example.epamandroid.views.annotationclasses

import android.support.annotation.IntDef

@IntDef(ViewType.DOG, ViewType.LOADING)
annotation class ViewType {
    companion object {
        const val DOG = 0
        const val LOADING = 1
    }
}