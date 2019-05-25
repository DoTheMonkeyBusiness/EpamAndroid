package com.example.epamandroid.mvp.presenters

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.contracts.IMainActivityContract
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.repository.MainActivityModel

class MainActivityPresenter(private var mainActivityView: IMainActivityContract.View)
    : IMainActivityContract.Presenter {

    companion object {
        private const val TAG = "MainActivityPresenter"
    }

    override fun onCreate() = Unit

    override fun onDestroy() = Unit
}