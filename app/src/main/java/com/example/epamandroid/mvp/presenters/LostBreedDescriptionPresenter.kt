package com.example.epamandroid.mvp.presenters

import com.example.epamandroid.mvp.contracts.ILostBreedDescriptionContract

class LostBreedDescriptionPresenter(private val view: ILostBreedDescriptionContract.View)
    : ILostBreedDescriptionContract.Presenter {

    override fun onCreate() = Unit

    override fun onDestroy() = Unit
}