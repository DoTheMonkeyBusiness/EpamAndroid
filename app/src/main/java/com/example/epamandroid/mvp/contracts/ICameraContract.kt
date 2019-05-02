package com.example.epamandroid.mvp.contracts

import android.os.Bundle

interface ICameraContract {

    interface IPresenter {
        fun stopBackgroundThread()
        fun startBackgroundThread()
        fun putDogInfoInBundle(dogBreed: String): Bundle?
    }

    interface IView {
        fun setBreedText(breed: String?)
        fun classifyFrame()
    }
}