package com.example.epamandroid.mvp.contracts

import android.content.Context
import com.example.epamandroid.gsonmodels.GsonRestaurantEntity
import com.example.epamandroid.models.RestaurantEntity
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView

interface ITypeDescriptionContract {
    interface View : IBaseView {
        fun updateTypeDescription(restaurantEntity: RestaurantEntity?)
        fun getContext(): Context?
    }

    interface Presenter : IBasePresenter {
        fun loadRestaurantByType(type: String)
    }

    interface Model {
        fun getEntity(type: String) : GsonRestaurantEntity?
    }
}