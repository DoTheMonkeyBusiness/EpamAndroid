package com.example.epamandroid.presenters

import com.example.epamandroid.contracts.MainContract

class MainPresenter(private var view: MainContract.View) : MainContract.Presenter {

    private lateinit var model:MainContract.Model

}