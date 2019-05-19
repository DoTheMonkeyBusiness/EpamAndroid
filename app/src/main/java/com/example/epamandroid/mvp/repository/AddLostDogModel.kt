package com.example.epamandroid.mvp.repository

import android.util.Log
import com.example.epamandroid.constants.URLConstants
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.contracts.IAddLostDogContract
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.*

object AddLostDogModel : IAddLostDogContract.Model<GsonLostDogEntity> {

    private val client = OkHttpClient()
    private val JSON = MediaType.parse("application/json; charset=utf-8")

    override fun putLostBreed(gsonLostDogEntity: GsonLostDogEntity): Boolean {
        try {
            val gson = Gson()
            val lostDogEntityToJson = gson.toJson(gsonLostDogEntity)
            val body = RequestBody.create(JSON, lostDogEntityToJson)
            val request =
                    Request
                            .Builder()
                            .url(URLConstants.NOT_MODERATED_LOST_DOGS_URL_STRING_EXTRA_KEY)
                            .post(body)
                            .build()
            client.newCall(request).execute()

            return true
        } catch (e: Exception) {
            e.printStackTrace()

            return false
        }
    }
}