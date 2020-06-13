package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.gsonmodels.GsonMapRestaurantEntity
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView
import java.io.File
import java.util.*

interface IAddMapRestaurantContract {

    interface View : IBaseView {
        fun imageUploadError()
        fun postSuccess()
        fun postError()
    }

    interface Presenter : IBasePresenter {
        fun uploadMapRestaurant(type: String,
                          phoneNumber: String,
                          description: String,
                          latitude: Double,
                          longitude: Double,
                          imageFile: File?)
    }

    interface Model {
        fun putMapType(gsonMapRestaurantEntity: GsonMapRestaurantEntity): Boolean
        fun uploadImage(imageFile: File, id: UUID): Boolean
    }
}