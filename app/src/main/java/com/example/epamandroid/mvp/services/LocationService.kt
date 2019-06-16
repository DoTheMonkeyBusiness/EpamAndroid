package com.example.epamandroid.mvp.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.example.epamandroid.R
import com.example.epamandroid.constants.MapConstants
import com.example.epamandroid.constants.ServiceConstants.FASTEST_INTERVAL
import com.example.epamandroid.constants.ServiceConstants.LOST_DOG_NOTIFICATION_ID
import com.example.epamandroid.constants.ServiceConstants.MAP_NOTIFICATION_ID
import com.example.epamandroid.constants.ServiceConstants.MAP_SERVICE_CHANNEL_ID
import com.example.epamandroid.constants.ServiceConstants.MAP_SERVICE_NAME
import com.example.epamandroid.constants.ServiceConstants.UPDATE_INTERVAL
import com.example.epamandroid.constants.SymbolConstants.EMPTY_EXTRA_KEY
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.models.LostDogEntity
import com.example.epamandroid.mvp.contracts.ILocationServiceContract
import com.example.epamandroid.mvp.repository.Repository
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng


class LocationService : Service(), ILocationServiceContract.Service {

    private val repository: ILocationServiceContract.Model = Repository
    private val lostDogsList: ArrayList<LostDogEntity> = arrayListOf()

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
                    val deviceLocation = locationResult?.lastLocation

                    if (deviceLocation != null) {
                        findDogsNearby(LatLng(deviceLocation.latitude, deviceLocation.longitude))
                    }
                }
            }, Looper.myLooper())
    }

    private fun findDogsNearby(deviceLocation: LatLng){
        Thread {
            val newDogsList: ArrayList<LostDogEntity> = arrayListOf()
            val gsonLostDogsMap: HashMap<String, GsonLostDogEntity>? = repository
                    .getEntitiesNearby(deviceLocation, MapConstants.SERVICE_RADIUS_EXTRA_KEY)

            gsonLostDogsMap?.forEach {
                newDogsList.add(
                        LostDogEntity(
                                it.value.id,
                                it.value.breed,
                                it.value.phoneNumber,
                                it.value.description,
                                it.value.latitude?.let { it1 -> it.value.longitude?.let { it2 -> LatLng(it1, it2) } },
                                it.value.photo
                        )
                )
            }

            newDogsList.forEach {
                if (lostDogsList.indexOf(it) == -1) {
                    showNotification()

                    return@Thread
                }
            }

            lostDogsList.clear()
            lostDogsList.addAll(newDogsList)
        }.start()
    }

    private fun showNotification() {

        val builder = NotificationCompat.Builder(this, MAP_SERVICE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_search)
                .setContentTitle(getString(R.string.important))
                .setContentText(getString(R.string.lost_dogs_near))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(LOST_DOG_NOTIFICATION_ID, builder.build())
        }
    }
}