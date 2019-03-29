package com.example.epamandroid.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.custom_action_bar_layout.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(customActionBarLayout)
    }


}
