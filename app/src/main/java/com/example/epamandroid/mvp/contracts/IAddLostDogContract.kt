package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView

interface IAddLostDogContract {

    interface View : IBaseView

    interface Presenter : IBasePresenter

    interface Model {
        fun getBreeds(
        ): List<String?>?
    }
}