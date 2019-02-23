package com.example.epamandroid

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast


/**
 *
 */
class MainActivity : AppCompatActivity() {


    private val button1: Button = findViewById(R.id.button1)
    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("test stash", "test stash")
        Log.d("test stash", "test stash")

    }


    @SuppressLint("ShowToast")
    fun onButtonClick(view: View) {
        button1.setText(R.string.button1_new )
        Toast.makeText(this, "feature_2 toast", Toast.LENGTH_LONG)
    }

    @SuppressLint("ShowToast")
    fun onSecondButtonClick(view: View) {
        Toast.makeText(this, "test_cherry-pick toast", Toast.LENGTH_LONG)
    }

}
