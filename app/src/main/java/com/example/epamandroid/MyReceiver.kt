package com.example.epamandroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver(private val callback: ICallback) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val backgroundColor: String = intent.getStringExtra(Constants.BROADCAST_MESSAGE_EXTRA_KEY)

        callback.onUpdate(backgroundColor)
    }
}
