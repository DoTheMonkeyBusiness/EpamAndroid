package com.example.epamandroid.gsonmodels

import com.example.epamandroid.constants.LostDogEntityConstants.BREED_EXTRA_KEY
import com.example.epamandroid.constants.LostDogEntityConstants.DESCRIPTION_EXTRA_KEY
import com.example.epamandroid.constants.LostDogEntityConstants.ID_EXTRA_KEY
import com.example.epamandroid.constants.LostDogEntityConstants.LATITUDE_EXTRA_KEY
import com.example.epamandroid.constants.LostDogEntityConstants.LONGITUDE_EXTRA_KEY
import com.example.epamandroid.constants.LostDogEntityConstants.PHONE_NUMBER_EXTRA_KEY
import com.example.epamandroid.constants.LostDogEntityConstants.PHOTO_EXTRA_KEY
import com.example.epamandroid.constants.LostDogEntityConstants.PUBLICATION_DATE_EXTRA_KEY
import com.google.gson.annotations.SerializedName
import java.util.*

class GsonLostDogEntity(
        @SerializedName(ID_EXTRA_KEY)
        val id: UUID?,
        @SerializedName(BREED_EXTRA_KEY)
        val breed: String?,
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