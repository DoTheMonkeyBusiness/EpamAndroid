package com.example.epamandroid.backend

import com.example.epamandroid.util.ICallback

interface IWebService<T> {

    fun getEntities(
            startRange: Int?,
            endRange: Int?,
            callback: ICallback<List<T>>
    )

    fun addEntitle(
            name: String,
            hwCount: String
    )

    fun removeEntitle(
            id: Int
    )

    fun getEntitiesSize(): Int

    fun editStudentInfo(
        id: Int,
        name: String?,
        hwCount: Int?
    )
}
