package com.example.epamandroid.presenters

import com.example.epamandroid.contracts.IMainContract

class MainPresenter(private var view: IMainContract.View) : IMainContract.Presenter {

    private lateinit var model:IMainContract.Model

}