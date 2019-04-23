package com.example.epamandroid.entities

import com.google.gson.annotations.SerializedName

class DogEntity(
    @SerializedName("id")
    val id: Int,
    @SerializedName("breed")
    val breed: String,
    @SerializedName("weight")
    val weight: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("canLiveAtHome")
    val isCanLiveAtHome: Boolean,
    @SerializedName("affectionate")
    val isAffectionate: Boolean,
    @SerializedName("breedPopularity")
    val breedPopularity: Float,
    @SerializedName("cost")
    val cost: Int,
    @SerializedName("lifeExpectancy")
    val lifeExpectancy: String,
    @SerializedName("photo")
    val photo: String

) {

}