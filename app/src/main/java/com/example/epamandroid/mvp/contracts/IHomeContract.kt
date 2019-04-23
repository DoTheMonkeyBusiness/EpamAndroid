package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.util.IShowLastViewAsLoadingCallback
import com.example.epamandroid.util.ICallback

interface IHomeContract {
    interface IPresenter

    interface IModel<T> {

        fun getEntities(
                startRange: Int,
                endRange: Int,
                callback: ICallback<List<T>>,
                showLastViewAsLoading: IShowLastViewAsLoadingCallback
        )

        fun getEntitiesSize(): Int?
    }
}