package com.example.epamandroid.mvp.presenters

import com.example.epamandroid.mvp.contracts.IMapTypeDescriptionContract
import com.example.epamandroid.mvp.repository.Repository

class MapTypeDescriptionPresenter(private val view: IMapTypeDescriptionContract.View)
    : IMapTypeDescriptionContract.Presenter {

    private val repository: IMapTypeDescriptionContract.Model = Repository

    override fun onCreate() = Unit

    override fun onDestroy() = Unit
}