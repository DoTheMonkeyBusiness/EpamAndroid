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

object MainActivityModel: IMainActivityContract.Model<GsonDogEntity> {
    private const val TAG: String = "MainActivityModel"

    private val client = OkHttpClient()

    override fun getEntity(breed:String): GsonDogEntity? {
        var response: Response? = null

        try {
            val request =
                Request
                    .Builder()
                    .url("$DOG_BREEDS_URL_STRING_EXTRA_KEY?orderBy=\"breed\"&equalTo=\"$breed\"")
                    .build()
            response = client.newCall(request).execute()

            val dog: HashMap<Int, GsonDogEntity>? = GsonParser.parseDogEntity(response.body().string())


            response.body().close()

            return if (dog != null
                && dog.isNotEmpty()
            ) {
               dog.values.first()
            } else null
        } catch (e: Exception) {
            response?.body()?.close()

            return null
        }
    }
}