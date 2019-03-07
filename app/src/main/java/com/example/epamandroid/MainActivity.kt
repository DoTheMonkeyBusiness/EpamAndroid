package com.example.epamandroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.activity_main_change_button

/**
 *
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newIntent = Intent(this, MyService::class.java)

        activity_main_change_button.setOnClickListener{
            activity_main_change_button.setText(R.string.button1_new)
        }
    }
}
