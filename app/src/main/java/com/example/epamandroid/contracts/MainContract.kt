package com.example.epamandroid.contracts

interface MainContract {
    interface View {
        fun showText()
    }

    interface Presenter {
        fun onButtonWasClicked()
        fun onDestroy()
    }

    interface Repository {
        fun loadMessage(): String
    }
}