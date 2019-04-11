package com.example.kotlinextensions

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.changeFragmentWithBackStack(layoutId: Int, fragmentClass: Fragment, fragmentKey: String){
    this.supportFragmentManager
        .beginTransaction()
        .replace(layoutId, fragmentClass, fragmentKey)
        .addToBackStack(null)
        .commit()
}
fun AppCompatActivity.changeFragment(layoutId: Int, fragmentClass: Fragment, fragmentKey: String){
    this.supportFragmentManager
        .beginTransaction()
        .replace(layoutId, fragmentClass, fragmentKey)
        .commit()
}