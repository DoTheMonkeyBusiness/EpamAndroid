package com.example.epamandroid.gson

import com.example.epamandroid.gsonmodels.GsonDogEntity
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
        fun parseLostDogEntity(responseBody: String?) : HashMap<Int, GsonDogEntity>? {
            val gson = Gson()

           return gson.fromJson(
                    responseBody,
                    object : TypeToken<Map<Int, GsonDogEntity>>() {}.type
            )
        }
    }
}