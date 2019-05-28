package com.example.epamandroid.mvp.contracts

import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.core.IBaseView

interface IMainActivityContract {
    interface View : IBaseView

    interface Presenter : IBasePresenter

    interface Model
}