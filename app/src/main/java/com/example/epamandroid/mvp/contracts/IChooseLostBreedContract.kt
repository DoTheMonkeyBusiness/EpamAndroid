package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView

interface IChooseLostBreedContract {
    interface View : IBaseView {
        fun addElementsToRecyclerView(dogList: List<String>?)
    }

    interface Presenter : IBasePresenter

    interface Model {
        fun getBreeds(
        ): MutableList<String?>?
    }
}