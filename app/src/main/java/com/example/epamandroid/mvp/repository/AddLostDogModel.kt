package com.example.epamandroid.mvp.repository

import com.example.epamandroid.constants.URLConstants
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.mvp.contracts.IAddLostDogContract
import com.google.gson.Gson
import com.squareup.okhttp.*

object AddLostDogModel : IAddLostDogContract.Model<GsonLostDogEntity> {

    private val client = OkHttpClient()
    private val JSON = MediaType.parse("application/json; charset=utf-8")

    override fun putLostBreed(gsonLostDogEntity: GsonLostDogEntity): Boolean {
        var response: Response? = null

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

            response = client.newCall(request).execute()
            val isSuccessfulResponse = response.isSuccessful

            response.body().close()

            return isSuccessfulResponse

        } catch (e: Exception) {
            e.printStackTrace()
            response?.body()?.close()

            return false
        }
    }
}