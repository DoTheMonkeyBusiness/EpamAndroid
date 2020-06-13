package com.example.epamandroid.mvp.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.example.epamandroid.R
import com.example.epamandroid.constants.MapConstants
import com.example.epamandroid.constants.ServiceConstants.SERVICE_FASTEST_INTERVAL
import com.example.epamandroid.constants.ServiceConstants.LOST_RESTAURANT_NOTIFICATION_ID
import com.example.epamandroid.constants.ServiceConstants.MAP_NOTIFICATION_ID
import com.example.epamandroid.constants.ServiceConstants.MAP_SERVICE_CHANNEL_ID
import com.example.epamandroid.constants.ServiceConstants.MAP_SERVICE_NAME
import com.example.epamandroid.constants.ServiceConstants.SERVICE_UPDATE_INTERVAL
import com.example.epamandroid.constants.SymbolConstants.EMPTY_EXTRA_KEY
import com.example.epamandroid.gsonmodels.GsonMapRestaurantEntity
import com.example.epamandroid.models.MapRestaurantEntity
import com.example.epamandroid.mvp.contracts.ILocationServiceContract
import com.example.epamandroid.mvp.repository.Repository
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng


class LocationService : Service(), ILocationServiceContract.Service {

    private val repository: ILocationServiceContract.Model = Repository
    private val mapRestaurantsList: ArrayList<MapRestaurantEntity> = arrayListOf()
    private val handler = Handler(Looper.getMainLooper())


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
            interval = SERVICE_UPDATE_INTERVAL
            fastestInterval = SERVICE_FASTEST_INTERVAL
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
                    findRestaurantsNearby(LatLng(deviceLocation.latitude, deviceLocation.longitude))
                }
            }
        }, Looper.myLooper())
    }

    private fun findRestaurantsNearby(deviceLocation: LatLng) {
        Thread {
            val newRestaurantsList: ArrayList<MapRestaurantEntity> = arrayListOf()
            val gsonMapRestaurantsMap: HashMap<String, GsonMapRestaurantEntity>? = repository
                    .getEntitiesNearby(deviceLocation, MapConstants.SERVICE_RADIUS_EXTRA_KEY)

            gsonMapRestaurantsMap?.forEach {
                newRestaurantsList.add(
                        MapRestaurantEntity(
                                it.value.id,
                                it.value.type,
                                it.value.phoneNumber,
                                it.value.description,
                                it.value.latitude?.let { it1 -> it.value.longitude?.let { it2 -> LatLng(it1, it2) } },
                                it.value.photo
                        )
                )
            }

            run loop@{
                newRestaurantsList.forEach {
                    if (mapRestaurantsList.indexOf(it) == -1) {
                        showNotification()

                        return@loop
                    }
                }
            }

            mapRestaurantsList.clear()
            mapRestaurantsList.addAll(newRestaurantsList)
        }.start()
    }

    private fun showNotification() {
        handler.post {
            Runnable {
                val builder = NotificationCompat.Builder(this, MAP_SERVICE_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_search)
                        .setContentTitle(getString(R.string.important))
                        .setContentText(getString(R.string.map_restaurants_near))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                with(NotificationManagerCompat.from(this)) {
                    notify(LOST_RESTAURANT_NOTIFICATION_ID, builder.build())
                }
            }.run()
        }
    }
}