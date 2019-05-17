package com.example.epamandroid.mvp.repository

import com.example.epamandroid.constants.URLConstants
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.contracts.IAddLostDogContract
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

object AddLostDogModel : IAddLostDogContract.Model {

    private val client = OkHttpClient()

    override fun getBreeds(): List<String?>? {
        try {
            val request =
                    Request
                            .Builder()
                            .url(URLConstants.URL_STRING_EXTRA_KEY)
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
               dogs.values.toList().map { it.breed }.toMutableList()
            } else null
        } catch (e: Exception) {
            return null
        }
    }
}