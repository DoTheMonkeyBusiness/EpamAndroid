package com.example.epamandroid

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private val myIntentFilter: IntentFilter = IntentFilter(Constants.CUSTOM_ACTION)
    private val myRec: BroadcastReceiver = MyReceiver()

    private lateinit var serviceIntent: Intent

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceIntent = Intent(this, MyService::class.java)

        activity_main_change_button.setOnClickListener {
            activity_main_change_button.setText(R.string.button1_new)
        }
        activity_main_start_service_button.setOnClickListener {
            startService(serviceIntent.putExtra(Constants.TIME_NOW, LocalDateTime.now().toString()))
        }
    }

    override fun onStart() {
        super.onStart()

        registerReceiver(myRec, myIntentFilter)
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(myRec)
    }
}
