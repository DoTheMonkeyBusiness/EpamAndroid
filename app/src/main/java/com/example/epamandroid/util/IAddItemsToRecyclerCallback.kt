package com.example.epamandroid.util

interface IAddItemsToRecyclerCallback<T> {
    fun onResult(result: T?)
    fun onShowLastViewAsLoading(isShow: Boolean = false)
}