package com.example.epamandroid.mvp.presenters

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.example.epamandroid.constants.DogEntityConstants
import com.example.epamandroid.mvp.contracts.ICameraContract
import com.example.epamandroid.mvp.repository.Repository

class CameraPresenter(view: ICameraContract.View)
    : ICameraContract.Presenter {

    companion object {
        private const val TAG: String = "CameraPresenter"
        private const val THREAD_NAME_KEY: String = "Camera Background"
    }

    private var backgroundHandler: Handler? = null
    private var backgroundThread: HandlerThread? = null
    private var runClassifier: Boolean = false

    private val repository: ICameraContract.Model = Repository
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

    override fun onCreate() = Unit

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

    override fun putDogInfoInBundle(dogBreed: String): Bundle? {
        val bundle: Bundle? = Bundle()

        return bundle?.apply {
            putString(DogEntityConstants.BREED_FROM_CAMERA_EXTRA_KEY, dogBreed)
        }
    }

    override fun onDestroy() = Unit
}