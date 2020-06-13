package com.example.epamandroid.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RestaurantEntity(
        val id: Int?,
        val type: String?,
        val weight: String?,
        val height: String?,
        val description: String?,
        val isCanLiveAtHome: Boolean?,
        val isAffectionate: Boolean?,
        val typePopularity: Float?,
        val cost: Int?,
        val lifeExpectancy: String?,
        val photo: String?
) : Parcelable