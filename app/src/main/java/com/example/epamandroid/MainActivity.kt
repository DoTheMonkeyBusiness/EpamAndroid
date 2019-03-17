package com.example.epamandroid

import android.content.Intent
import android.content.IntentFilter
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

    companion object {
        const val ACTIVITY_MAIN_START_SERVICE_BUTTON_KEY: String = "activity_main_start_service_button"
        const val ACTIVITY_MAIN_STOP_SERVICE_BUTTON_KEY: String = "activity_main_stop_service_button"
        const val ACTIVITY_MAIN_DANCE_GIF_KEY: String = "activity_main_dance_gif"
        const val ACTIVITY_MAIN_DANCE_1_GIF_KEY: String = "activity_main_dance_1_gif"
        const val MEDIA_PLAYER_KEY: String = "mediaPlayer"
    }

    private val myIntentFilter: IntentFilter = IntentFilter(Constants.CUSTOM_ACTION_EXTRA_KEY)
    private var mediaPlayer: MediaPlayer? = null
    private var isMediaPlayerPlaying: Boolean = false

    private lateinit var myRec: MyReceiver
    private lateinit var serviceIntent: Intent

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       applySavedInstanceState(savedInstanceState)

        serviceIntent = Intent(this, MyService::class.java)
        myRec = MyReceiver(activity_main_layout)

        activity_main_start_service_button.setOnClickListener {
            startService(serviceIntent.putExtra(Constants.SERVICE_STATE_EXTRA_KEY, true))

            activity_main_start_service_button.isEnabled = false
            activity_main_stop_service_button.isEnabled = true
            activity_main_dance_gif.visibility = View.VISIBLE
            activity_main_dance_1_gif.visibility = View.VISIBLE

            mediaPlayer = MediaPlayer
                    .create(this, R.raw.epic_sax_guy)
                    .apply {
                        isLooping = true
                        start()
                    }
            isMediaPlayerPlaying = true
        }

        activity_main_stop_service_button.setOnClickListener {
            stopService(serviceIntent.putExtra(Constants.SERVICE_STATE_EXTRA_KEY, false))

            activity_main_start_service_button.isEnabled = true
            activity_main_stop_service_button.isEnabled = false
            activity_main_dance_gif.visibility = View.INVISIBLE
            activity_main_dance_1_gif.visibility = View.INVISIBLE

            mediaPlayer?.apply {
                stop()
                release()
                mediaPlayer = null
            }
            isMediaPlayerPlaying = false
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putBoolean(ACTIVITY_MAIN_START_SERVICE_BUTTON_KEY, activity_main_start_service_button.isEnabled)
        outState?.putBoolean(ACTIVITY_MAIN_STOP_SERVICE_BUTTON_KEY, activity_main_stop_service_button.isEnabled)
        outState?.putBoolean(MEDIA_PLAYER_KEY, isMediaPlayerPlaying)
        outState?.putInt(ACTIVITY_MAIN_DANCE_GIF_KEY, activity_main_dance_gif.visibility)
        outState?.putInt(ACTIVITY_MAIN_DANCE_1_GIF_KEY, activity_main_dance_1_gif.visibility)

        if(isMediaPlayerPlaying) {
            mediaPlayer?.apply {
                stop()
                release()
                mediaPlayer = null
            }
        }
    }

    private fun applySavedInstanceState(savedInstanceState: Bundle?){
        if (savedInstanceState != null) {
            activity_main_start_service_button.isEnabled = savedInstanceState.getBoolean(ACTIVITY_MAIN_START_SERVICE_BUTTON_KEY)
            activity_main_stop_service_button.isEnabled = savedInstanceState.getBoolean(ACTIVITY_MAIN_STOP_SERVICE_BUTTON_KEY)
            activity_main_dance_gif.visibility = savedInstanceState.getInt(ACTIVITY_MAIN_DANCE_GIF_KEY)
            activity_main_dance_1_gif.visibility = savedInstanceState.getInt(ACTIVITY_MAIN_DANCE_1_GIF_KEY)

            if (savedInstanceState.getBoolean(MEDIA_PLAYER_KEY))
            mediaPlayer = MediaPlayer
                    .create(this, R.raw.epic_sax_guy)
                    .apply {
                        isLooping = true
                        start()
                    }
        }
    }
}