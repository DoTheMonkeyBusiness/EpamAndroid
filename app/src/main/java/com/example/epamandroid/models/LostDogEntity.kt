package com.example.epamandroid.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class LostDogEntity(
    val id: UUID?,
    val breed: String?,
    val phoneNumber: String?,
    val description: String?,
    val position: LatLng?,
    val photo: String?
) : Parcelable