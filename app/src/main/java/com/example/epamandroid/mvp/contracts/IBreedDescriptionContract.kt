package com.example.epamandroid.mvp.contracts

import android.content.Context
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView

interface IBreedDescriptionContract {
    interface View : IBaseView {
        fun updateBreedDescription(dogEntity: DogEntity?)
        fun getContext(): Context?
    }

    interface Presenter : IBasePresenter {
        fun loadDogByBreed(breed: String)
    }

    interface Model {
        fun getEntity(breed: String) : GsonDogEntity?
    }
}