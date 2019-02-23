package com.example.epamandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

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


    }


    fun onButtonClick(view: View) {
        button1.setText(R.string.button1_new)
    }
}
