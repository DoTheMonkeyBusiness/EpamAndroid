package com.example.epamandroid.entities

import android.os.Parcel
import android.os.Parcelable
import com.example.epamandroid.constants.DogEntityConstants
import com.google.gson.annotations.SerializedName

class DogEntity(
    @SerializedName(DogEntityConstants.id)
    val id: Int,
    @SerializedName(DogEntityConstants.breed)
    val breed: String?,
    @SerializedName(DogEntityConstants.weight)
    val weight: String?,
    @SerializedName(DogEntityConstants.height)
    val height: String?,
    @SerializedName(DogEntityConstants.description)
    val description: String?,
    @SerializedName(DogEntityConstants.canLiveAtHome)
    val isCanLiveAtHome: Boolean,
    @SerializedName(DogEntityConstants.affectionate)
    val isAffectionate: Boolean,
    @SerializedName(DogEntityConstants.breedPopularity)
    val breedPopularity: Float,
    @SerializedName(DogEntityConstants.cost)
    val cost: Int,
    @SerializedName(DogEntityConstants.lifeExpectancy)
    val lifeExpectancy: String?,
    @SerializedName(DogEntityConstants.photo)
    val photo: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(breed)
        parcel.writeString(weight)
        parcel.writeString(height)
        parcel.writeString(description)
        parcel.writeByte(if (isCanLiveAtHome) 1 else 0)
        parcel.writeByte(if (isAffectionate) 1 else 0)
        parcel.writeFloat(breedPopularity)
        parcel.writeInt(cost)
        parcel.writeString(lifeExpectancy)
        parcel.writeString(photo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DogEntity> {
        override fun createFromParcel(parcel: Parcel): DogEntity {
            return DogEntity(parcel)
        }

        override fun newArray(size: Int): Array<DogEntity?> {
            return arrayOfNulls(size)
        }
    }
}