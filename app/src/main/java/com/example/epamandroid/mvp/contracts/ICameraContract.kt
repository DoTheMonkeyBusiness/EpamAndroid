package com.example.epamandroid.mvp.contracts

interface ICameraContract {

    interface IPresenter {
        fun stopBackgroundThread()
        fun startBackgroundThread()
    }

    interface IView {
        fun setBreedText(breed: String?)
        fun classifyFrame()
    }
}