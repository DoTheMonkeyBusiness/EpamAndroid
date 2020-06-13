package com.example.epamandroid.gson

import com.example.epamandroid.gsonmodels.GsonRestaurantEntity
import com.example.epamandroid.gsonmodels.GsonMapRestaurantEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GsonParser {
    companion object {
        fun parseRestaurantEntity(responseBody: String?) : HashMap<Int, GsonRestaurantEntity>? {
            val gson = Gson()

           return gson.fromJson(
                    responseBody,
                    object : TypeToken<Map<Int, GsonRestaurantEntity>>() {}.type
            )
        }

        fun parseMapRestaurantEntity(responseBody: String?) : HashMap<String, GsonMapRestaurantEntity>? {
            val gson = Gson()

           return gson.fromJson(
                    responseBody,
                    object : TypeToken<HashMap<String, GsonMapRestaurantEntity>>() {}.type
            )
        }
    }
}