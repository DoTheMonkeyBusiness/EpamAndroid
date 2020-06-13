package com.example.epamandroid.gsonmodels

import com.example.epamandroid.constants.RestaurantEntityConstants.AFFECTIONATE_EXTRA_KEY
import com.example.epamandroid.constants.RestaurantEntityConstants.TYPE_EXTRA_KEY
import com.example.epamandroid.constants.RestaurantEntityConstants.TYPE_POPULARITY_EXTRA_KEY
import com.example.epamandroid.constants.RestaurantEntityConstants.CAN_LIVE_AT_HOME_EXTRA_KEY
import com.example.epamandroid.constants.RestaurantEntityConstants.COST_EXTRA_KEY
import com.example.epamandroid.constants.RestaurantEntityConstants.DESCRIPTION_EXTRA_KEY
import com.example.epamandroid.constants.RestaurantEntityConstants.HEIGHT_EXTRA_KEY
import com.example.epamandroid.constants.RestaurantEntityConstants.ID_EXTRA_KEY
import com.example.epamandroid.constants.RestaurantEntityConstants.LIFE_EXPENTANCY_EXTRA_KEY
import com.example.epamandroid.constants.RestaurantEntityConstants.PHOTO_EXTRA_KEY
import com.example.epamandroid.constants.RestaurantEntityConstants.WEIGHT_EXTRA_KEY
import com.google.gson.annotations.SerializedName

class GsonRestaurantEntity(
        @SerializedName(ID_EXTRA_KEY)
        val id: Int?,
        @SerializedName(TYPE_EXTRA_KEY)
        val type: String?,
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
        @SerializedName(TYPE_POPULARITY_EXTRA_KEY)
        val typePopularity: Float?,
        @SerializedName(COST_EXTRA_KEY)
        val cost: Int?,
        @SerializedName(LIFE_EXPENTANCY_EXTRA_KEY)
        val lifeExpectancy: String?,
        @SerializedName(PHOTO_EXTRA_KEY)
        val photo: String?
)
