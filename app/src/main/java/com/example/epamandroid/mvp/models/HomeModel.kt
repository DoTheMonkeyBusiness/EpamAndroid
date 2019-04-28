package com.example.epamandroid.mvp.models

import com.example.epamandroid.entities.DogEntity
import com.example.epamandroid.mvp.contracts.IHomeContract
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException
import java.util.concurrent.Executors
import kotlin.collections.HashMap

object HomeModel : IHomeContract.IModel<DogEntity> {

    private const val TAG: String = "HomeModel"
    private const val urlString = "https://dogbreeds-60b2e.firebaseio.com/dogBreeds.json"
    private val client = OkHttpClient()


    @Synchronized
    override fun getEntities(
        startPosition: Int,
        endPosition: Int
    ): ArrayList<DogEntity>? {
        try {
            val request =
                Request
                    .Builder()
                    .url("$urlString?orderBy=\"id\"&startAt=$startPosition&endAt=$endPosition")
                    .build()
            val gson = Gson()
            val dogs: HashMap<Int, DogEntity>?
            val response = client.newCall(request).execute()

            dogs = gson.fromJson(
                response
                    .body()
                    .string(),
                object : TypeToken<Map<Int, DogEntity>>() {}.type
            )

            return if (dogs != null
                && dogs.isNotEmpty()
            ) {
                ArrayList(dogs.values)
            } else null
        } catch (e: Exception) {
            return null
        }
    }
}