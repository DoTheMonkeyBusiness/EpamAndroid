package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.entities.DogEntity

interface IMainActivityContract {
    interface View {
        fun updateBreedDescription(dogEntity: DogEntity?)
    }

    interface Presenter {
        fun loadDogByBreed(breed: String)
    }

    interface Model<T> {
        fun getEntity(breed: String) : DogEntity?
    }
}