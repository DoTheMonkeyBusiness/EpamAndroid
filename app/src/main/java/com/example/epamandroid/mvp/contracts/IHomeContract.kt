package com.example.epamandroid.mvp.contracts

import android.content.Context
import com.example.epamandroid.gsonmodels.GsonRestaurantEntity
import com.example.epamandroid.models.RestaurantEntity
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView

interface IHomeContract {

    interface View : IBaseView {
        fun addElements(restaurantList: List<RestaurantEntity>?, isFullList: Boolean)
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
        ): HashMap<Int, GsonRestaurantEntity>?

    }
}