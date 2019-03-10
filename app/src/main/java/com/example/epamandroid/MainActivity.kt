package com.example.epamandroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.button1
import kotlinx.android.synthetic.main.activity_main.second_activity_button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button1.setOnClickListener {
            button1.setText(R.string.button1_new)
        }
        second_activity_button.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }
}