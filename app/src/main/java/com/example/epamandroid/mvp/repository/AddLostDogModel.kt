package com.example.epamandroid.mvp.repository

import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import com.example.epamandroid.constants.ParseConstants.IMAGE_FILE_TYPE_EXTRA_KEY
import com.example.epamandroid.constants.ParseConstants.JSON_FILE_TYPE_EXTRA_KEY
import com.example.epamandroid.constants.SymbolConstants.DOT_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants
import com.example.epamandroid.constants.URLConstants.ALT_MEDIA_STRING_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants.RESPONSE_TIME_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants.STORAGE_LOST_DOGS_URL_STRING_EXTRA_KEY
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.mvp.contracts.IAddLostDogContract
import com.example.epamandroid.mvp.views.activities.AddLostDogActivity
import com.google.gson.Gson
import com.squareup.okhttp.*
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

object AddLostDogModel : IAddLostDogContract.Model {

    private val client = OkHttpClient()

    private val JSON = MediaType.parse(JSON_FILE_TYPE_EXTRA_KEY)
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

    override fun uploadImage(imageFile: File, id: UUID): Boolean {
        var response: Response? = null

        try {

            val req = MultipartBuilder().type(MultipartBuilder.FORM)
                    .addFormDataPart(id.toString(), imageFile.name, RequestBody.create(MediaType.parse(IMAGE_FILE_TYPE_EXTRA_KEY+imageFile.extension), imageFile))
                    .build()

            val request = Request.Builder()
                    .url("$STORAGE_LOST_DOGS_URL_STRING_EXTRA_KEY$id$DOT_EXTRA_KEY${imageFile.extension}$ALT_MEDIA_STRING_EXTRA_KEY")
                    .post(req)
                    .build()
            val client = OkHttpClient()
            client.setConnectTimeout(RESPONSE_TIME_EXTRA_KEY, TimeUnit.SECONDS)

            response = client.newCall(request).execute()
            val isSuccessfulResponse = response.isSuccessful
            Log.d(this@AddLostDogModel.javaClass.name, response.body().string())
            response.body().close()

            return isSuccessfulResponse
        } catch (e: Exception) {
            e.printStackTrace()
            response?.body()?.close()
            return false
        }

    }
}