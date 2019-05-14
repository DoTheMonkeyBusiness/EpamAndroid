package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView

interface IHomeContract {

    interface View : IBaseView {
        fun addElements(dogList: List<DogEntity>?, ifFullList: Boolean)
        fun isEmptyRecyclerView() : Boolean
    }

    interface Presenter : IBasePresenter {
        fun getMoreItems(
                startPosition: Int,
                endPosition: Int
        )
    }

    interface Model<T> {
        fun getEntities(
                startPosition: Int,
                endPosition: Int
        ): ArrayList<T>?

    }
}