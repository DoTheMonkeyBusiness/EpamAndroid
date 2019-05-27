package com.example.epamandroid.mvp.repository

import com.example.epamandroid.constants.MapConstants
import com.example.epamandroid.constants.ParseConstants
import com.example.epamandroid.constants.SymbolConstants
import com.example.epamandroid.constants.URLConstants
import com.example.epamandroid.gson.GsonParser
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.mvp.contracts.*
import com.google.gson.Gson
import com.squareup.okhttp.*
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

object Repository :
    IAddLostDogContract.Model,
    IChooseLostBreedContract.Model,
    IHomeContract.Model,
    IMainActivityContract.Model,
    IMapContract.Model,
    IBreedDescriptionContract.Model,
    ICameraContract.Model,
    ILostBreedDescriptionContract.Model {

    private val JSON = MediaType.parse(ParseConstants.JSON_FILE_TYPE_EXTRA_KEY)

    override fun putLostBreed(gsonLostDogEntity: GsonLostDogEntity): Boolean {
        val client = OkHttpClient()
        var response: Response? = null
        val gson = Gson()
        val lostDogEntityToJson = gson.toJson(gsonLostDogEntity)
        val body = RequestBody.create(JSON, lostDogEntityToJson)
        var isSuccessfulResponse: Boolean? = null

        try {
            val request =
                Request
                    .Builder()
                    .url(URLConstants.NOT_MODERATED_LOST_DOGS_URL_STRING_EXTRA_KEY)
                    .post(body)
                    .build()

            response = client.newCall(request).execute()
            isSuccessfulResponse = response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            response?.body()?.close()
        }

        return isSuccessfulResponse ?: false
    }

    override fun uploadImage(imageFile: File, id: UUID): Boolean {
        val client = OkHttpClient()
        var response: Response? = null
        var isSuccessfulResponse: Boolean? = null

        try {
            val req = MultipartBuilder().type(MultipartBuilder.FORM)
                .addFormDataPart(
                    id.toString(),
                    imageFile.name,
                    RequestBody.create(
                        MediaType.parse(ParseConstants.IMAGE_FILE_TYPE_EXTRA_KEY + imageFile.extension),
                        imageFile
                    )
                )
                .build()

            val request = Request.Builder()
                .url("${URLConstants.STORAGE_LOST_DOGS_URL_STRING_EXTRA_KEY}$id${SymbolConstants.DOT_EXTRA_KEY}${imageFile.extension}${URLConstants.ALT_MEDIA_STRING_EXTRA_KEY}")
                .post(req)
                .build()

            client.setConnectTimeout(URLConstants.RESPONSE_TIME_EXTRA_KEY, TimeUnit.SECONDS)

            response = client.newCall(request).execute()
            isSuccessfulResponse = response.isSuccessful

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            response?.body()?.close()
        }

        return isSuccessfulResponse ?: false
    }

    override fun getBreeds(): MutableList<String?>? {
        val client = OkHttpClient()
        var response: Response? = null
        var dogs: HashMap<Int, GsonDogEntity>? = null

        try {
            val request =
                Request
                    .Builder()
                    .url("${URLConstants.DOG_BREEDS_URL_STRING_EXTRA_KEY}?orderBy=\"id\"&startAt=0")
                    .build()

            response = client.newCall(request).execute()
            dogs = GsonParser.parseDogEntity(response.body().string())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            response?.body()?.close()
        }

        return if (dogs != null
            && dogs.isNotEmpty()
        ) {
            dogs.values.map { it.breed }.toMutableList()
        } else null
    }

    override fun getEntities(
        startPosition: Int,
        endPosition: Int
    ): HashMap<Int, GsonDogEntity>? {
        val client = OkHttpClient()
        var response: Response? = null
        var dogs: HashMap<Int, GsonDogEntity>? = null

        try {
            val request =
                Request
                    .Builder()
                    .url("${URLConstants.DOG_BREEDS_URL_STRING_EXTRA_KEY}?orderBy=\"id\"&startAt=$startPosition&endAt=$endPosition")
                    .build()

            response = client
                .newCall(request)
                .execute()
            dogs = GsonParser
                .parseDogEntity(response.body().string())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            response?.body()?.close()
        }

        return if (dogs != null
            && dogs.isNotEmpty()
        ) {
            dogs
        } else null
    }

    override fun getEntitiesNearby(latitude: Double, radius: Float): HashMap<String, GsonLostDogEntity>? {
        val client = OkHttpClient()
        var response: Response? = null
        val rangeLatitude = radius / MapConstants.KILOMETERS_IN_ONE_DEG_EXTRA_KEY
        val minLatitude = latitude - rangeLatitude
        val maxLatitude = latitude + rangeLatitude
        var lostDogsMap: HashMap<String, GsonLostDogEntity>? = null

        try {
            val request =
                Request
                    .Builder()
                    .url("${URLConstants.NOT_MODERATED_LOST_DOGS_URL_STRING_EXTRA_KEY}?orderBy=\"latitude\"&startAt=$minLatitude&endAt=$maxLatitude")
                    .build()

            response = client.newCall(request).execute()
            lostDogsMap = GsonParser.parseLostDogEntity(response.body().string())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            response?.body()?.close()
        }

        return if (lostDogsMap != null
            && lostDogsMap.isNotEmpty()
        ) {
            lostDogsMap
        } else null
    }

    override fun getEntity(breed: String): GsonDogEntity? {
        val client = OkHttpClient()
        var response: Response? = null
        var gsonDogEntity: HashMap<Int, GsonDogEntity>? = null

        try {
            val request =
                Request
                    .Builder()
                    .url("${URLConstants.DOG_BREEDS_URL_STRING_EXTRA_KEY}?orderBy=\"breed\"&equalTo=\"$breed\"")
                    .build()
            response = client.newCall(request).execute()

            gsonDogEntity = GsonParser.parseDogEntity(response.body().string())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            response?.body()?.close()
        }

        return if (gsonDogEntity != null
            && gsonDogEntity.isNotEmpty()
        ) {
            gsonDogEntity.values.first()
        } else null
    }
}
