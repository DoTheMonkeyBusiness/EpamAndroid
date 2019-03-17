package com.example.epamandroid

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi

class MyService : IntentService("MyService") {

    private val myIntent: Intent = Intent(Constants.CUSTOM_ACTION_KEY)

    private val colorList: MutableList<String> = ArrayList()

    private var serviceStatus: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent: Intent) {
        serviceStatus = intent.getBooleanExtra(Constants.SERVICE_STATE_KEY, false)

        do {
            Thread.sleep(350)
            when {
                serviceStatus -> myIntent.putExtra(
                    Constants.BROADCAST_MESSAGE_KEY,
                    colorList[(Math.random() * 100 % (colorList.size - 1)).toInt()]
                )
                !serviceStatus -> myIntent.putExtra(
                    Constants.BROADCAST_MESSAGE_KEY,
                    "#FFFFFF"
                )
            }
            sendBroadcast(myIntent)
        } while (serviceStatus)
    }

    override fun onCreate() {
        super.onCreate()
        setBackgroundList()
    }

    override fun onDestroy() {
        super.onDestroy()

        colorList.clear()
        serviceStatus = false
    }

    private fun setBackgroundList() {
        colorList.add("#FFAB00")
        colorList.add("#E64A19")
        colorList.add("#00E676")
        colorList.add("#00B8D4")
        colorList.add("#01579B")
        colorList.add("#651FFF")
        colorList.add("#26C6DA")
        colorList.add("#F44336")
        colorList.add("#EF6C00")
    }
}
