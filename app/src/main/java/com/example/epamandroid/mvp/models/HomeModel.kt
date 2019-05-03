package com.example.epamandroid.mvp.models

import com.example.epamandroid.constants.URLConstants.URL_STRING_EXTRA_KEY
import com.example.epamandroid.entities.DogEntity
import com.example.epamandroid.mvp.contracts.IHomeContract
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlin.collections.HashMap

object HomeModel : IHomeContract.IModel<DogEntity> {

    private const val TAG: String = "HomeModel"
    private val client = OkHttpClient()

    override fun getEntities(
        startPosition: Int,
        endPosition: Int
    ): ArrayList<DogEntity>? {
        try {
            val request =
                Request
                    .Builder()
                    .url("$URL_STRING_EXTRA_KEY?orderBy=\"id\"&startAt=$startPosition&endAt=$endPosition")
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