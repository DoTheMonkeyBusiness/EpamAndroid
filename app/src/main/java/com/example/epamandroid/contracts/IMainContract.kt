package com.example.epamandroid.contracts

interface IMainContract {
    interface View {
        fun setMenuVisibility(isVisible: Boolean)
    }

    interface Presenter
    interface Model {
        fun loadMessage(): String
    }
}