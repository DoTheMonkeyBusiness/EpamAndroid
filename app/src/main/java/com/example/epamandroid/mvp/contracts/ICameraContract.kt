package com.example.epamandroid.mvp.contracts

import android.os.Bundle
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView

interface ICameraContract {

    interface View : IBaseView {
        fun setTypeText(type: String?)
        fun classifyFrame()
    }

    interface Presenter: IBasePresenter {
        fun stopBackgroundThread()
        fun startBackgroundThread()
        fun putRestaurantInfoInBundle(restaurantType: String): Bundle?

    }

    interface Model
}