package com.example.epamandroid.mvp.presenters

import com.example.epamandroid.mvp.contracts.IHomeContract
import com.example.epamandroid.mvp.models.HomeModel
import com.example.epamandroid.mvp.views.fragments.HomeFragment

class HomePresenter(
        private val view: HomeFragment) : IHomeContract.IPresenter {

    private val homeModel: HomeModel? = HomeModel.getInstance()

}