package com.example.epamandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.mysubmodule.Myclass

/**
 *
 */
class MainActivity : AppCompatActivity() {


    private lateinit var button1: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button1 = findViewById(R.id.button1)
        Myclass.myFunc()
    }


    fun onButtonClick(view: View) {
        button1.setText(R.string.button1_new)
    }
}
