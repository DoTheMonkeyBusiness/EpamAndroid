package com.example.epamandroid.mvp.models

import com.example.epamandroid.constants.URLConstants.URL_STRING_EXTRA_KEY
import com.example.epamandroid.entities.DogEntity
import com.example.epamandroid.mvp.contracts.IMainActivityContract
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import java.util.*

object MainActivityModel: IMainActivityContract.Model<DogEntity> {
    private const val TAG: String = "MainActivityModel"

    private val client = OkHttpClient()

    override fun getEntity(breed:String): DogEntity? {
        try {
            val request =
                Request
                    .Builder()
                    .url("$URL_STRING_EXTRA_KEY?orderBy=\"breed\"&equalTo=\"$breed\"")
                    .build()
            val gson = Gson()
            val dog: HashMap<Int, DogEntity>?
            val response = client.newCall(request).execute()

            dog = gson.fromJson(
                response
                    .body()
                    .string(),
                object : TypeToken<Map<Int, DogEntity>>() {}.type
            )

            return if (dog != null
                && dog.isNotEmpty()
            ) {
               dog.values.first()
            } else null
        } catch (e: Exception) {
            return null
        }
    }
}