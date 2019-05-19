package com.example.epamandroid.mvp.repository

import com.example.epamandroid.constants.URLConstants.DOG_BREEDS_URL_STRING_EXTRA_KEY
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.mvp.contracts.IHomeContract
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlin.collections.HashMap

object HomeModel : IHomeContract.Model<GsonDogEntity> {

    private const val TAG: String = "HomeModel"
    private val client = OkHttpClient()

    override fun getEntities(
        startPosition: Int,
        endPosition: Int
    ): HashMap<Int, GsonDogEntity>?{
        try {
            val request =
                Request
                    .Builder()
                    .url("$DOG_BREEDS_URL_STRING_EXTRA_KEY?orderBy=\"id\"&startAt=$startPosition&endAt=$endPosition")
                    .build()
            val gson = Gson()
            val dogs: HashMap<Int, GsonDogEntity>?
            val response = client.newCall(request).execute()

            dogs = gson.fromJson(
                response
                    .body()
                    .string(),
                object : TypeToken<Map<Int, GsonDogEntity>>() {}.type
            )

            return if (dogs != null
                && dogs.isNotEmpty()
            ) {
                dogs
            } else null
        } catch (e: Exception) {
            return null
        }
    }
}