package com.example.epamandroid.gson

import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GsonParser {
    companion object {
        fun parseDogEntity(responseBody: String?) : HashMap<Int, GsonDogEntity>? {
            val gson = Gson()

           return gson.fromJson(
                    responseBody,
                    object : TypeToken<Map<Int, GsonDogEntity>>() {}.type
            )
        }

        fun parseLostDogEntity(responseBody: String?) : HashMap<String, GsonLostDogEntity>? {
            val gson = Gson()

           return gson.fromJson(
                    responseBody,
                    object : TypeToken<HashMap<String, GsonLostDogEntity>>() {}.type
            )
        }
    }

    //TODO doesn't work
//    fun parseDogEntity(responseBody: String?) : HashMap<Int, T>? {
//        val gson = Gson()
//
//        return gson.fromJson(
//            responseBody,
//            object : TypeToken<Map<Int, T>>() {}.type
//        )
//    }
}