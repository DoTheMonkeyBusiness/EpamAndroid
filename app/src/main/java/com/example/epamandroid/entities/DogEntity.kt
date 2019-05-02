package com.example.epamandroid.entities

import com.example.epamandroid.constants.DogEntityConstants
import com.google.gson.annotations.SerializedName

class DogEntity(
    @SerializedName(DogEntityConstants.id)
    val id: Int,
    @SerializedName(DogEntityConstants.breed)
    val breed: String,
    @SerializedName(DogEntityConstants.weight)
    val weight: String,
    @SerializedName(DogEntityConstants.height)
    val height: String,
    @SerializedName(DogEntityConstants.description)
    val description: String,
    @SerializedName(DogEntityConstants.canLiveAtHome)
    val isCanLiveAtHome: Boolean,
    @SerializedName(DogEntityConstants.affectionate)
    val isAffectionate: Boolean,
    @SerializedName(DogEntityConstants.breedPopularity)
    val breedPopularity: Float,
    @SerializedName(DogEntityConstants.cost)
    val cost: Int,
    @SerializedName(DogEntityConstants.lifeExpectancy)
    val lifeExpectancy: String,
    @SerializedName(DogEntityConstants.photo)
    val photo: String

)