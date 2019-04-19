package com.example.epamandroid.mvp.presenters

import android.graphics.Bitmap
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import com.example.epamandroid.contracts.ICameraContract
import com.example.neuralnetwork.ImageClassifier

class CameraPresenter private constructor(val view: ICameraContract.IView) : ICameraContract.IPresenter {

    companion object {
        private const val TAG: String = "CameraPresenter"
        private const val THREAD_NAME_KEY: String = "Camera Background"

        private var instance: CameraPresenter? = null
        fun getInstance(view: ICameraContract.IView): CameraPresenter? {
            if (instance == null) {
                instance = CameraPresenter(view)
            }

            return instance
        }
    }

    private var backgroundHandler: Handler? = null
    private var backgroundThread: HandlerThread? = null
    private var runClassifier: Boolean = false

    private val lock: Any = Any()

    private val periodicClassify = object : Runnable {
        override fun run() {
            synchronized(lock) {
                if (runClassifier) {
                    view.classifyFrame()
                }
            }
            backgroundHandler?.post(this)
        }
    }

    override fun startBackgroundThread() {
        backgroundThread = HandlerThread(THREAD_NAME_KEY)
        backgroundThread?.start()
        backgroundHandler = Handler(backgroundThread?.looper)
        synchronized(lock) {
            runClassifier = true
        }
        backgroundHandler?.post(periodicClassify)
    }

    override fun stopBackgroundThread() {
        backgroundThread?.quitSafely()

        try {
            backgroundThread?.join()
            backgroundThread = null
            backgroundHandler = null
            synchronized(lock) {
                runClassifier = false
            }
        } catch (e: InterruptedException) {
            Log.e(TAG, "error while stopping BackgroundThread")

        }
    }
}