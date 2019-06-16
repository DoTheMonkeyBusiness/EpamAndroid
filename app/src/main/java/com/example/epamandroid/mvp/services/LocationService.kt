package com.example.epamandroid.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.example.epamandroid.constants.ServiceConstants.MAP_NOTIFICATION_ID
import com.example.epamandroid.constants.ServiceConstants.MAP_SERVICE_CHANNEL_ID
import com.example.epamandroid.constants.ServiceConstants.MAP_SERVICE_NAME
import com.example.epamandroid.constants.SymbolConstants.EMPTY_EXTRA_KEY
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.os.Looper
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationCallback
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.example.epamandroid.constants.ServiceConstants.FASTEST_INTERVAL
import com.example.epamandroid.constants.ServiceConstants.UPDATE_INTERVAL
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng


class LocationService : Service() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(MAP_SERVICE_CHANNEL_ID,
                    MAP_SERVICE_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT)

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(this, MAP_SERVICE_CHANNEL_ID)
                    .setContentTitle(EMPTY_EXTRA_KEY)
                    .setContentText(EMPTY_EXTRA_KEY).build()

            startForeground(MAP_NOTIFICATION_ID, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getLocation()

        return START_NOT_STICKY
    }

    private fun getLocation() {
        val locationRequestHighAccuracy = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_INTERVAL
        }

        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            stopSelf()

            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequestHighAccuracy, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                val location = locationResult?.lastLocation

                if (location != null) {
                    val geoPoint = LatLng(location.latitude, location.longitude)
                }
            }
        }, Looper.myLooper())
    }
}