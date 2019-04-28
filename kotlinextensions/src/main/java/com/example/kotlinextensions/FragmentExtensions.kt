package com.example.kotlinextensions

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.changeFragmentWithBackStack(layoutId: Int, fragmentClass: Fragment, fragmentKey: String) {
    this.supportFragmentManager
            .beginTransaction()
            .replace(layoutId, fragmentClass, fragmentKey)
            .addToBackStack(null)
            .commit()
}

fun AppCompatActivity.changeFragment(layoutId: Int, fragmentClass: Fragment, fragmentKey: String) {
    this.supportFragmentManager
            .beginTransaction()
            .replace(layoutId, fragmentClass, fragmentKey)
            .commit()
}

fun AppCompatActivity.changeFragmentWithBackStackAndAnimation(
        layoutId: Int,
        fragmentClass: Fragment,
        fragmentKey: String,
        startAnimation: Int,
        endAinmation: Int) {
    this.supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(startAnimation, endAinmation)
            .replace(layoutId, fragmentClass, fragmentKey)
            .commit()
}