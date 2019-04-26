package com.example.epamandroid.mvp.contracts

interface IHomeContract {
    interface IPresenter

    interface IModel<T> {

        fun getEntities(
            startPosition: Int,
            endPosition: Int
        ): ArrayList<T>?

    }
}