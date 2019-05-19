package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView

interface IAddLostDogContract {

    interface View : IBaseView {
        fun onPostSuccess()
        fun onPostError()
    }

    interface Presenter : IBasePresenter {
        fun uploadLostDog(breed: String,
                          phoneNumber: String,
                          description: String,
                          latitude: Double,
                          longitude: Double,
                          photo: String)
    }

    interface Model<T> {
        fun putLostBreed(gsonLostDogEntity: T): Boolean
    }
}