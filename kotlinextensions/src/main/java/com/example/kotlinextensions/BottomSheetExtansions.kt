package com.example.kotlinextensions

import android.support.design.widget.BottomSheetBehavior
import android.view.View

fun BottomSheetBehavior<View>?.expandBottomSheet() {
    if (this?.state != BottomSheetBehavior.STATE_EXPANDED) {
        this?.state = BottomSheetBehavior.STATE_EXPANDED
    }
}

fun BottomSheetBehavior<View>?.collapseBottomSheet() {
    if (this?.state != BottomSheetBehavior.STATE_COLLAPSED) {
        this?.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}