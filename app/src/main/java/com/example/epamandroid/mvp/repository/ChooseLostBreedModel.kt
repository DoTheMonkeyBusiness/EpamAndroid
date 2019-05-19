package com.example.epamandroid.mvp.repository

import com.example.epamandroid.constants.URLConstants
import com.example.epamandroid.gson.GsonParser
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.contracts.IChooseLostBreedContract
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response

object ChooseLostBreedModel : IChooseLostBreedContract.Model {

    private val client = OkHttpClient()

    override fun getBreeds(): MutableList<String?>? {
        var response: Response? = null

        try {
            val request =
                    Request
                            .Builder()
                            .url("${URLConstants.DOG_BREEDS_URL_STRING_EXTRA_KEY}?orderBy=\"id\"&startAt=0")
                            .build()
            response = client.newCall(request).execute()

            val dogs: HashMap<Int, GsonDogEntity>? = GsonParser.parseDogEntity(response.body().string())
            response.body().close()

            return if (dogs != null
                    && dogs.isNotEmpty()
            ) {
                dogs.values.map { it.breed }.toMutableList()
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            response?.body()?.close()
            return null
        }
    }
}