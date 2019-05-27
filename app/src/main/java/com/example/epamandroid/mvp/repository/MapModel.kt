package com.example.epamandroid.mvp.repository

import com.example.epamandroid.constants.MapConstants.KILOMETERS_IN_ONE_DEG_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants
import com.example.epamandroid.gson.GsonParser
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.mvp.contracts.IMapContract
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response

object MapModel : IMapContract.Model {

    private val client = OkHttpClient()

    override fun getEntitiesNearby(latitude: Double, radius: Float): HashMap<String, GsonLostDogEntity>? {
        var response: Response? = null
        val rangeLatitude = radius / KILOMETERS_IN_ONE_DEG_EXTRA_KEY
        val minLatitude = latitude - rangeLatitude
        val maxLatitude = latitude + rangeLatitude
        try {
            val request =
                Request
                    .Builder()
                    .url("${URLConstants.NOT_MODERATED_LOST_DOGS_URL_STRING_EXTRA_KEY}?orderBy=\"latitude\"&startAt=$minLatitude&endAt=$maxLatitude")
                    .build()
            response = client.newCall(request).execute()

            val dogs: HashMap<String, GsonLostDogEntity>? =
                GsonParser.parseLostDogEntity(response.body().string())

            response.body().close()

            return if (dogs != null
                && dogs.isNotEmpty()
            ) {
                dogs
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            response?.body()?.close()
            return null
        }
    }
}