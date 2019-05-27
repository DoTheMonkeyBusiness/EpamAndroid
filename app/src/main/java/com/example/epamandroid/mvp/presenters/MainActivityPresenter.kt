package com.example.epamandroid.mvp.presenters

import com.example.epamandroid.mvp.contracts.IMainActivityContract
import com.example.epamandroid.mvp.repository.Repository

class MainActivityPresenter(private var mainActivityView: IMainActivityContract.View)
    : IMainActivityContract.Presenter {

    companion object {
        private const val TAG = "MainActivityPresenter"
    }

    private val repository: IMainActivityContract.Model = Repository

    override fun onCreate() = Unit

    override fun onDestroy() = Unit
}