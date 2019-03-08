package com.example.epamandroid

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import java.util.concurrent.TimeUnit

class MyService : IntentService("MyService") {

    private val myIntent: Intent = Intent(Constants.CUSTOM_ACTION)

    override fun onHandleIntent(intent: Intent) {
        val timeNow: String = intent.getStringExtra(Constants.TIME_NOW)

        myIntent.putExtra(Constants.BROADCAST_MESSAGE, timeNow)
        sendBroadcast(myIntent)
    }
}
