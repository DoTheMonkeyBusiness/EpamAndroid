package com.example.epamandroid.mvp.contracts

import android.content.Context
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView

interface IHomeContract {

    interface View : IBaseView {
        fun addElements(dogList: List<DogEntity>?, ifFullList: Boolean)
        fun isEmptyRecyclerView() : Boolean
        fun getContext(): Context?
    }

    interface Presenter : IBasePresenter {
        fun getMoreItems(
                startPosition: Int,
                endPosition: Int
        )
    }

    interface Model {
        fun getEntities(
                startPosition: Int,
                endPosition: Int
        ): HashMap<Int, GsonDogEntity>?

    }
}