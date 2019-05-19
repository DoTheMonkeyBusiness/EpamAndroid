package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView

interface IMainActivityContract {
    interface View : IBaseView{
        fun updateBreedDescription(dogEntity: DogEntity?)
    }

    interface Presenter : IBasePresenter {
        fun loadDogByBreed(breed: String)
    }

    interface Model<T> {
        fun getEntity(breed: String) : T?
    }
}