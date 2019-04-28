package com.example.epamandroid.threading

import android.os.Handler
import android.os.HandlerThread
import android.os.Message

class LoadDataHandlerThread(name: String) : HandlerThread(name) {

    private var handler: Handler? = null

    override fun onLooperPrepared() {
        super.onLooperPrepared()

        handler = object : Handler(looper) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
            }
        }
    }
}