package com.example.epamandroid.gsonmodels

import com.example.epamandroid.constants.DogEntityConstants.AFFECTIONATE_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.BREED_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.BREED_POPULARITY_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.CAN_LIVE_AT_HOME_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.COST_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.DESCRIPTION_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.HEIGHT_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.ID_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.LIFE_EXPENTANCY_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.PHOTO_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.WEIGHT_EXTRA_KEY
import com.google.gson.annotations.SerializedName

class GsonDogEntity(
        @SerializedName(ID_EXTRA_KEY)
        val id: Int?,
        @SerializedName(BREED_EXTRA_KEY)
        val breed: String?,
        @SerializedName(WEIGHT_EXTRA_KEY)
        val weight: String?,
        @SerializedName(HEIGHT_EXTRA_KEY)
        val height: String?,
        @SerializedName(DESCRIPTION_EXTRA_KEY)
        val description: String?,
        @SerializedName(CAN_LIVE_AT_HOME_EXTRA_KEY)
        val isCanLiveAtHome: Boolean?,
        @SerializedName(AFFECTIONATE_EXTRA_KEY)
        val isAffectionate: Boolean?,
        @SerializedName(BREED_POPULARITY_EXTRA_KEY)
        val breedPopularity: Float?,
        @SerializedName(COST_EXTRA_KEY)
        val cost: Int?,
        @SerializedName(LIFE_EXPENTANCY_EXTRA_KEY)
        val lifeExpectancy: String?,
        @SerializedName(PHOTO_EXTRA_KEY)
        val photo: String?
)
