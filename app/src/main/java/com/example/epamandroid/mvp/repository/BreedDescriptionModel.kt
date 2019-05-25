package com.example.epamandroid.mvp.repository

import com.example.epamandroid.constants.URLConstants
import com.example.epamandroid.gson.GsonParser
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.mvp.contracts.IBreedDescriptionContract
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.util.HashMap

object BreedDescriptionModel : IBreedDescriptionContract.Model {
    private const val TAG: String = "BreedDescriptionModel"

    private val client = OkHttpClient()

    override fun getEntity(breed:String): GsonDogEntity? {
        var response: Response? = null

        try {
            val request =
                    Request
                            .Builder()
                            .url("${URLConstants.DOG_BREEDS_URL_STRING_EXTRA_KEY}?orderBy=\"breed\"&equalTo=\"$breed\"")
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