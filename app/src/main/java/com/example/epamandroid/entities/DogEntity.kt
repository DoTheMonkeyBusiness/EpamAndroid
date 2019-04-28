package com.example.epamandroid.entities

import com.example.epamandroid.constants.DogEntitieConstants
import com.google.gson.annotations.SerializedName

class DogEntity(
    @SerializedName(DogEntitieConstants.id)
    val id: Int,
    @SerializedName(DogEntitieConstants.breed)
    val breed: String,
    @SerializedName(DogEntitieConstants.weight)
    val weight: String,
    @SerializedName(DogEntitieConstants.height)
    val height: String,
    @SerializedName(DogEntitieConstants.description)
    val description: String,
    @SerializedName(DogEntitieConstants.canLiveAtHome)
    val isCanLiveAtHome: Boolean,
    @SerializedName(DogEntitieConstants.affectionate)
    val isAffectionate: Boolean,
    @SerializedName(DogEntitieConstants.breedPopularity)
    val breedPopularity: Float,
    @SerializedName(DogEntitieConstants.cost)
    val cost: Int,
    @SerializedName(DogEntitieConstants.lifeExpectancy)
    val lifeExpectancy: String,
    @SerializedName(DogEntitieConstants.photo)
    val photo: String

)