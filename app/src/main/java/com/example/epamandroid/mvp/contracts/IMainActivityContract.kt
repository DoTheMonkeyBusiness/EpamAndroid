package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.entities.DogEntity

interface IMainActivityContract {
    interface View {
        fun updateBreedDescription(dogEntity: DogEntity?)
    }

    interface Presenter
    interface Model {
    }
}