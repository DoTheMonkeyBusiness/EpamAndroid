package com.example.epamandroid.mvp.contracts

import android.os.Bundle
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView

interface ICameraContract {

    interface IView : IBaseView {
        fun setBreedText(breed: String?)
        fun classifyFrame()
    }

    interface IPresenter: IBasePresenter {
        fun stopBackgroundThread()
        fun startBackgroundThread()
        fun putDogInfoInBundle(dogBreed: String): Bundle?

    }
}