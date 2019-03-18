package com.example.epamandroid.backend

import com.example.epamandroid.util.ICallback

interface IWebService<T> {

    fun getEntities(
        startRange: Int?,
        endRange: Int?,
        callback: ICallback<List<T>>
    )
}