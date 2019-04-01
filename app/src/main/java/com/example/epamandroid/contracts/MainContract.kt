package com.example.epamandroid.contracts

interface MainContract {
    interface View {
        fun setMenuVisibility(isVisible: Boolean)
    }

    interface Presenter
    interface Model {
        fun loadMessage(): String
    }
}