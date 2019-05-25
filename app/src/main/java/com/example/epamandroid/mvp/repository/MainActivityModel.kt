package com.example.epamandroid.mvp.repository

import com.example.epamandroid.constants.URLConstants.DOG_BREEDS_URL_STRING_EXTRA_KEY
import com.example.epamandroid.gson.GsonParser
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.mvp.contracts.IMainActivityContract
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.util.*

object MainActivityModel: IMainActivityContract.Model {
    private const val TAG: String = "MainActivityModel"

}