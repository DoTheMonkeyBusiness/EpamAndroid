package com.example.epamandroid.mvp.repository

import com.example.epamandroid.constants.URLConstants.DOG_BREEDS_URL_STRING_EXTRA_KEY
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.mvp.contracts.IMainActivityContract
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import java.util.*

object MainActivityModel: IMainActivityContract.Model<GsonDogEntity> {
    private const val TAG: String = "MainActivityModel"

    private val client = OkHttpClient()

    override fun getEntity(breed:String): GsonDogEntity? {
        try {
            val request =
                Request
                    .Builder()
                    .url("$DOG_BREEDS_URL_STRING_EXTRA_KEY?orderBy=\"breed\"&equalTo=\"$breed\"")
                    .build()
            val gson = Gson()
            val dog: HashMap<Int, GsonDogEntity>?
            val response = client.newCall(request).execute()

            dog = gson.fromJson(
                response
                    .body()
                    .string(),
                object : TypeToken<Map<Int, GsonDogEntity>>() {}.type
            )

            return if (dog != null
                && dog.isNotEmpty()
            ) {
               dog.values.first()
            } else null
        } catch (e: Exception) {
            return null
        }
    }
}