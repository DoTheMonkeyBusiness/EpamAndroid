package com.example.epamandroid

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.Toast
import java.time.LocalDateTime

class MyService : IntentService("MyService") {

    private val myIntent: Intent = Intent(Constants.CUSTOM_ACTION)

    private var serviceStatus: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent: Intent) {
        serviceStatus = intent.getBooleanExtra(Constants.SERVICE_STATE, false)

        while (serviceStatus) {
            myIntent.putExtra(Constants.BROADCAST_MESSAGE, LocalDateTime.now().second.toString())
            sendBroadcast(myIntent)
            Thread.sleep(1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceStatus = false
    }
}
