package com.example.epamandroid.mvp.repository

import android.util.Log
import com.example.epamandroid.constants.DogEntityConstants.BREED_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.ID_EXTRA_KEY
import com.example.epamandroid.constants.LostDogEntityConstants.LATITUDE_EXTRA_KEY
import com.example.epamandroid.constants.MapConstants
import com.example.epamandroid.constants.ParseConstants
import com.example.epamandroid.constants.ParseConstants.IMAGE_FILE_TYPE_EXTRA_KEY
import com.example.epamandroid.constants.SymbolConstants
import com.example.epamandroid.constants.SymbolConstants.DOUBLE_QUOTES_EXTRA_KEY
import com.example.epamandroid.constants.SymbolConstants.ZERO_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants
import com.example.epamandroid.constants.URLConstants.DOG_BREEDS_URL_STRING_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants.END_AT_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants.EQUAL_TO_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants.NOT_MODERATED_LOST_DOGS_URL_STRING_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants.ORDER_BY_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants.RESPONSE_TIME_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants.START_AT_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants.STORAGE_LOST_DOGS_URL_STRING_EXTRA_KEY
import com.example.epamandroid.gson.GsonParser
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.mvp.contracts.*
import com.google.android.gms.maps.model.LatLng
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
        ILostBreedDescriptionContract.Model,
        ILocationServiceContract.Model {

    private const val TAG = "Repository"

    private val JSON = MediaType.parse(ParseConstants.JSON_FILE_TYPE_EXTRA_KEY)
    private val client = OkHttpClient()

    override fun putLostBreed(gsonLostDogEntity: GsonLostDogEntity): Boolean {
        var response: Response? = null
        val gson = Gson()
        val lostDogEntityToJson = gson.toJson(gsonLostDogEntity)
        val body = RequestBody.create(JSON, lostDogEntityToJson)
        var isSuccessfulResponse: Boolean? = null

        try {
            val request =
                    Request
                            .Builder()
                            .url(NOT_MODERATED_LOST_DOGS_URL_STRING_EXTRA_KEY)
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
        var response: Response? = null
        var isSuccessfulResponse: Boolean? = null

        try {
            val req = MultipartBuilder().type(MultipartBuilder.FORM)
                    .addFormDataPart(
                            id.toString(),
                            imageFile.name,
                            RequestBody.create(
                                    MediaType.parse(IMAGE_FILE_TYPE_EXTRA_KEY + imageFile.extension),
                                    imageFile
                            )
                    )
                    .build()

            val request = Request.Builder()
                    .url(
                            STORAGE_LOST_DOGS_URL_STRING_EXTRA_KEY +
                                    id +
                                    SymbolConstants.DOT_EXTRA_KEY +
                                    imageFile.extension +
                                    URLConstants.ALT_MEDIA_EXTRA_KEY
                    )
                    .post(req)
                    .build()

            client.setConnectTimeout(RESPONSE_TIME_EXTRA_KEY, TimeUnit.SECONDS)

            response = client.newCall(request).execute()
            Log.d(TAG, response.body().string())
            isSuccessfulResponse = response.isSuccessful

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            response?.body()?.close()
        }

        return isSuccessfulResponse ?: false
    }

    override fun getBreeds(): MutableList<String?>? {
        var response: Response? = null
        var dogs: HashMap<Int, GsonDogEntity>? = null

        try {
            val request =
                    Request
                            .Builder()
                            .url(
                                    DOG_BREEDS_URL_STRING_EXTRA_KEY +
                                            ORDER_BY_EXTRA_KEY +
                                            DOUBLE_QUOTES_EXTRA_KEY +
                                            ID_EXTRA_KEY +
                                            DOUBLE_QUOTES_EXTRA_KEY +
                                            START_AT_EXTRA_KEY +
                                            ZERO_EXTRA_KEY
                            )
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
        var response: Response? = null
        var dogs: HashMap<Int, GsonDogEntity>? = null

        try {
            val request =
                    Request
                            .Builder()
                            .url(
                                    DOG_BREEDS_URL_STRING_EXTRA_KEY +
                                            ORDER_BY_EXTRA_KEY +
                                            DOUBLE_QUOTES_EXTRA_KEY +
                                            ID_EXTRA_KEY +
                                            DOUBLE_QUOTES_EXTRA_KEY +
                                            START_AT_EXTRA_KEY +
                                            startPosition +
                                            END_AT_EXTRA_KEY +
                                            endPosition
                            )
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

    override fun getEntitiesNearby(userPosition: LatLng, radius: Float): HashMap<String, GsonLostDogEntity>? {
        var response: Response? = null
        val rangeLatitude = radius / MapConstants.KILOMETERS_IN_ONE_DEG_EXTRA_KEY
        val minLatitude = userPosition.latitude - rangeLatitude
        val maxLatitude = userPosition.latitude + rangeLatitude
        var lostDogsMap: HashMap<String, GsonLostDogEntity>? = null
        val lostDogsNearbyMap: HashMap<String, GsonLostDogEntity>? = hashMapOf()

        try {
            val request =
                    Request
                            .Builder()
                            .url(
                                    NOT_MODERATED_LOST_DOGS_URL_STRING_EXTRA_KEY +
                                            ORDER_BY_EXTRA_KEY +
                                            DOUBLE_QUOTES_EXTRA_KEY +
                                            LATITUDE_EXTRA_KEY +
                                            DOUBLE_QUOTES_EXTRA_KEY +
                                            START_AT_EXTRA_KEY +
                                            minLatitude +
                                            END_AT_EXTRA_KEY +
                                            maxLatitude
                            )
                            .build()

            response = client.newCall(request).execute()
            lostDogsMap = GsonParser.parseLostDogEntity(response.body().string())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            response?.body()?.close()
        }

        lostDogsMap?.forEach {
            val latitude = it.value.latitude
            val longitude = it.value.longitude

            if (latitude != null
                    && longitude != null
                    && calculationByDistance(userPosition, latitude, longitude) <= MapConstants.MAP_RADIUS_EXTRA_KEY
            ) {
                lostDogsNearbyMap?.put(it.key, it.value)
            }
        }

        return if (lostDogsNearbyMap != null
                && lostDogsNearbyMap.isNotEmpty()
        ) {
            lostDogsNearbyMap
        } else null
    }

    private fun calculationByDistance(startPosition: LatLng, endLatitude: Double, endLongitude: Double): Double {
        val dLat = Math.toRadians(endLatitude - startPosition.latitude)
        val dLon = Math.toRadians(endLongitude - startPosition.longitude)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + (Math.cos(Math.toRadians(startPosition.latitude))
                * Math.cos(Math.toRadians(endLatitude)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2))
        val c: Double = 2 * Math.asin(Math.sqrt(a))

        return MapConstants.EARTH_RADIUS_EXTRA_KEY * c
    }

    override fun getEntity(breed: String): GsonDogEntity? {
        var response: Response? = null
        var gsonDogEntity: HashMap<Int, GsonDogEntity>? = null

        try {
            val request =
                    Request
                            .Builder()
                            .url(
                                    DOG_BREEDS_URL_STRING_EXTRA_KEY +
                                            ORDER_BY_EXTRA_KEY +
                                            DOUBLE_QUOTES_EXTRA_KEY +
                                            BREED_EXTRA_KEY +
                                            DOUBLE_QUOTES_EXTRA_KEY +
                                            EQUAL_TO_EXTRA_KEY +
                                            DOUBLE_QUOTES_EXTRA_KEY +
                                            breed +
                                            DOUBLE_QUOTES_EXTRA_KEY
                            )
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
