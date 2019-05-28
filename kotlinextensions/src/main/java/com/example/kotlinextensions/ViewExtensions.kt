package com.example.kotlinextensions

import android.view.View

fun View.goneView() {
    if (this.visibility != View.GONE) {
        this.visibility = View.GONE
    }
}
fun View.visibleView() {
    if (this.visibility != View.VISIBLE) {
        this.visibility = View.VISIBLE
    }
}