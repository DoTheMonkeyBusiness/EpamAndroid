package com.example.epamandroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.constraint.ConstraintLayout

class MyReceiver(activity_main_layout: ConstraintLayout) : BroadcastReceiver() {

    private val myLayout = activity_main_layout

    override fun onReceive(context: Context, intent: Intent) {
        val backgroundColor: String = intent.getStringExtra(Constants.BROADCAST_MESSAGE_EXTRA_KEY)

        myLayout.setBackgroundColor(Color.parseColor(backgroundColor))
    }
}
