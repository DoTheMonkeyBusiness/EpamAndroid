package com.example.epamandroid

import android.support.annotation.IntDef

@IntDef(ViewType.STUDENT, ViewType.LOADING)
internal annotation class ViewType {
    companion object {

        const val STUDENT = 0
        const val LOADING = 1
    }
}