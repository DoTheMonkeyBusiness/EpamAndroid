package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView
import java.io.File
import java.util.*

interface IAddLostDogContract {

    interface View : IBaseView {
        fun imageUploadError()
        fun postSuccess()
        fun postError()
    }

    interface Presenter : IBasePresenter {
        fun uploadLostDog(breed: String,
                          phoneNumber: String,
                          description: String,
                          latitude: Double,
                          longitude: Double,
                          imageFile: File?)
    }

    interface Model {
        fun putLostBreed(gsonLostDogEntity: GsonLostDogEntity): Boolean
        fun uploadImage(imageFile: File, id: UUID): Boolean
    }
}