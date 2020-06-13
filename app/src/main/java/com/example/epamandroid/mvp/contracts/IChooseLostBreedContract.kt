package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView

interface IChooseMapTypeContract {
    interface View : IBaseView {
        fun addElementsToRecyclerView(restaurantList: List<String>?)
    }

    interface Presenter : IBasePresenter

    interface Model {
        fun getTypes(
        ): MutableList<String?>?
    }
}