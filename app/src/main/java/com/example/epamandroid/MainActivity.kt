package com.example.epamandroid

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import kotlinx.android.synthetic.main.activity_main.activity_main_start_service_button
import kotlinx.android.synthetic.main.activity_main.activity_main_stop_service_button
import kotlinx.android.synthetic.main.activity_main.activity_main_dance_gif
import kotlinx.android.synthetic.main.activity_main.activity_main_dance_1_gif
import kotlinx.android.synthetic.main.activity_main.activity_main_layout


class MainActivity : AppCompatActivity() {

    private val myIntentFilter: IntentFilter = IntentFilter(Constants.CUSTOM_ACTION)

    private lateinit var myRec: MyReceiver
    private lateinit var serviceIntent: Intent
    private lateinit var mediaPlayer: MediaPlayer

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceIntent = Intent(this, MyService::class.java)
        myRec = MyReceiver(activity_main_layout)

        activity_main_start_service_button.setOnClickListener {
            startService(serviceIntent.putExtra(Constants.SERVICE_STATE, true))

            activity_main_start_service_button.isEnabled = false
            activity_main_stop_service_button.isEnabled = true
            activity_main_dance_gif.visibility = View.VISIBLE
            activity_main_dance_1_gif.visibility = View.VISIBLE
            mediaPlayer = MediaPlayer.create(this, R.raw.epic_sax_guy)
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
        activity_main_stop_service_button.setOnClickListener {
            stopService(serviceIntent.putExtra(Constants.SERVICE_STATE, false))

            activity_main_start_service_button.isEnabled = true
            activity_main_stop_service_button.isEnabled = false
            activity_main_dance_gif.visibility = View.INVISIBLE
            activity_main_dance_1_gif.visibility = View.INVISIBLE
            mediaPlayer.stop()
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