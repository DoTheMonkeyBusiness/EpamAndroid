package com.example.epamandroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.activity_main_vk_button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity_main_vk_button.setOnClickListener {
            startActivity(Intent(this@MainActivity, VkHomeActivity::class.java))
        }
    }
}
