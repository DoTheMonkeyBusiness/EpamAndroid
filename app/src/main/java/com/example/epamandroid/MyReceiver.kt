package com.example.epamandroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val currentTime: String = intent.getStringExtra(Constants.BROADCAST_MESSAGE)

        Toast.makeText(context, currentTime, Toast.LENGTH_SHORT).show()
    }
}
