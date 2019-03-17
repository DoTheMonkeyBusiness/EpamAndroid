package com.example.epamandroid

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi

class MyService : IntentService("MyService") {

    companion object {
        const val WHITE_COLOR_KEY: String = "#FFFFFF"
    }

    private val myIntent: Intent = Intent(Constants.CUSTOM_ACTION_EXTRA_KEY)

    private val colorList = listOf("#FFAB00", "#E64A19", "#00E676", "#00B8D4", "#01579B", "#651FFF", "#651FFF", "#26C6DA", "#F44336", "#EF6C00")

    private var serviceStatus: Boolean = false

    override fun onHandleIntent(intent: Intent) {
        serviceStatus = intent.getBooleanExtra(Constants.SERVICE_STATE_EXTRA_KEY, false)

        do {
            Thread.sleep(350)
            when {
                serviceStatus -> myIntent.putExtra(
                    Constants.BROADCAST_MESSAGE_EXTRA_KEY,
                    colorList[(Math.random() * 100 % (colorList.size - 1)).toInt()]
                )
                !serviceStatus -> myIntent.putExtra(
                    Constants.BROADCAST_MESSAGE_EXTRA_KEY,
                    WHITE_COLOR_KEY
                )
            }
            sendBroadcast(myIntent)
        } while (serviceStatus)
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceStatus = false
    }
}
