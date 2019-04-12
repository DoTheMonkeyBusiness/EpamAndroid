package com.example.epamandroid.entities

import android.os.Parcel
import android.os.Parcelable

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DogEntity(
        val id: Int,
        val breed: String,
        val description: String,
        val isCanLiveAtHome: Boolean,
        val isAffectionate: Boolean,
        val dogRating: Byte) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(breed)
        parcel.writeString(description)
        parcel.writeByte(if (isCanLiveAtHome) 1 else 0)
        parcel.writeByte(if (isAffectionate) 1 else 0)
        parcel.writeByte(dogRating)
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