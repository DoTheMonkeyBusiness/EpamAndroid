package com.example.epamandroid

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.concurrent.TimeUnit

class MyService : IntentService("MyService") {

    private val SERVICE_ACTION: String = "myAction"

    override fun onHandleIntent(intent: Intent?) {
        val endTime = System.currentTimeMillis() + 5*1000
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                TimeUnit.MILLISECONDS.sleep(endTime - System.currentTimeMillis());
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        val myIntent = Intent()
        myIntent.setAction(SERVICE_ACTION)
    }
}
