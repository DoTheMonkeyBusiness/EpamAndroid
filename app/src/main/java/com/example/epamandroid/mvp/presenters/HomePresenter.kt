package com.example.epamandroid.mvp.presenters

import android.os.Bundle
import com.example.epamandroid.contracts.IHomeContract

class HomePresenter private constructor() : IHomeContract.IPresenter {

    var recyclerViewState: Bundle? = null

    companion object {
        private var instance: HomePresenter? = null
        fun getInstance(): HomePresenter? {
            if(instance == null){
                instance = HomePresenter()
            }

            return instance
        }
    }
}