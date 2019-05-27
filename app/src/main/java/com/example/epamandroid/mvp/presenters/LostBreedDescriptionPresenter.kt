package com.example.epamandroid.mvp.presenters

import com.example.epamandroid.mvp.contracts.ILostBreedDescriptionContract
import com.example.epamandroid.mvp.repository.Repository

class LostBreedDescriptionPresenter(private val view: ILostBreedDescriptionContract.View)
    : ILostBreedDescriptionContract.Presenter {

    private val repository: ILostBreedDescriptionContract.Model = Repository

    override fun onCreate() = Unit

    override fun onDestroy() = Unit
}