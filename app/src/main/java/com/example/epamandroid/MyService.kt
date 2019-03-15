package com.example.epamandroid

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.Toast
import java.time.LocalDateTime

class MyService : IntentService("MyService") {

    private val myIntent: Intent = Intent(Constants.CUSTOM_ACTION)

    private val colorList: MutableList<String> = ArrayList()

    private var serviceStatus: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent: Intent) {
        serviceStatus = intent.getBooleanExtra(Constants.SERVICE_STATE, false)

        do {
            Thread.sleep(350)
            when {
                serviceStatus -> myIntent.putExtra(
                    Constants.BROADCAST_MESSAGE,
                    colorList[(Math.random() * 100 % (colorList.size - 1)).toInt()]
                )
                !serviceStatus -> myIntent.putExtra(
                    Constants.BROADCAST_MESSAGE,
                    "#FFFFFF")
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
