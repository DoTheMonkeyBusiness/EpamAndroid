package com.example.epamandroid.gsonmodels

import com.example.epamandroid.constants.MapRestaurantEntityConstants.TYPE_EXTRA_KEY
import com.example.epamandroid.constants.MapRestaurantEntityConstants.DESCRIPTION_EXTRA_KEY
import com.example.epamandroid.constants.MapRestaurantEntityConstants.ID_EXTRA_KEY
import com.example.epamandroid.constants.MapRestaurantEntityConstants.LATITUDE_EXTRA_KEY
import com.example.epamandroid.constants.MapRestaurantEntityConstants.LONGITUDE_EXTRA_KEY
import com.example.epamandroid.constants.MapRestaurantEntityConstants.PHONE_NUMBER_EXTRA_KEY
import com.example.epamandroid.constants.MapRestaurantEntityConstants.PHOTO_EXTRA_KEY
import com.example.epamandroid.constants.MapRestaurantEntityConstants.PUBLICATION_DATE_EXTRA_KEY
import com.google.gson.annotations.SerializedName
import java.util.*

class GsonMapRestaurantEntity(
        @SerializedName(ID_EXTRA_KEY)
        val id: UUID?,
        @SerializedName(TYPE_EXTRA_KEY)
        val type: String?,
        @SerializedName(PHONE_NUMBER_EXTRA_KEY)
        val phoneNumber: String?,
        @SerializedName(DESCRIPTION_EXTRA_KEY)
        val description: String?,
        @SerializedName(LATITUDE_EXTRA_KEY)
        val latitude: Double?,
        @SerializedName(LONGITUDE_EXTRA_KEY)
        val longitude: Double?,
        @SerializedName(PUBLICATION_DATE_EXTRA_KEY)
        val publicationDate: String?,
        @SerializedName(PHOTO_EXTRA_KEY)
        val photo: String?
)