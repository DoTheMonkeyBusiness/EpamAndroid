package com.example.epamandroid.models

import com.google.android.gms.maps.model.LatLng
import java.util.*

class LostDogEntity(
    val id: UUID?,
    val breed: String?,
    val phoneNumber: String?,
    val description: String?,
    val position: LatLng?,
    val photo: String?
)