package com.example.epamandroid

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

/**
 *
 */
class MainActivity : AppCompatActivity() {


    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }


    @SuppressLint("ShowToast")
    fun onButtonClick(view: View) {
        Toast.makeText(this, "feature_1 toast", Toast.LENGTH_LONG)
    }
}
