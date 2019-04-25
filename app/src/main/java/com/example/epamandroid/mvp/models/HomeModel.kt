package com.example.epamandroid.mvp.models

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.entities.DogEntity
import com.example.epamandroid.mvp.contracts.IHomeContract
import com.example.epamandroid.util.ICallback
import com.example.epamandroid.util.IShowLastViewAsLoadingCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

object HomeModel : IHomeContract.IModel<DogEntity> {

    private const val TAG: String = "HomeModel"

    private var dogsList: HashMap<Int, DogEntity>? = hashMapOf()
    private val random = Random()
    private val handler = Handler(Looper.getMainLooper())

    private val client = OkHttpClient()
    private val executor = Executors.newSingleThreadExecutor()
    private val urlString = "https://dogbreeds-60b2e.firebaseio.com/dogBreeds.json"

    init {
//        for (i in 0..40) {
//            val breed = "Some breed"
//            val description = "some description"
//            val weight= " 12 - 15"
//            val height= " 12 - 15"
//            val isCanLiveAtHome: Boolean = random.nextBoolean()
//            val isAffectionate: Boolean = random.nextBoolean()
//            val isLikes: Boolean = false
//            val dogRating: Byte = random.nextInt(5).toByte()
//
//            val dog = DogEntity(
//                    i,
//                    breed,
//                    weight,
//                    height,
//                    description,
//                    isCanLiveAtHome,
//                    isAffectionate,
//                    isLikes,
//                    dogRating)
//
//            dogsList?.add(dog)
//        }
    }

    override fun getEntities(
        startRange: Int,
        endRange: Int,
        callback: ICallback<List<DogEntity>>,
        showLastViewAsLoading: IShowLastViewAsLoadingCallback
    ) {

//        when {
//            (endRange < dogsList?.size?.minus(1) ?: 0) -> {
//                showLastViewAsLoading.onShowLastViewAsLoadingCallback(true)
//                handler.postDelayed({ dogsList?.subList(startRange, endRange)?.let { callback.onResult(it) } }, 1000)
//            }
//            else -> {
//                handler.postDelayed({ dogsList?.subList(startRange, dogsList.size)?.let { callback.onResult(it) } }, 1000)
//                showLastViewAsLoading.onShowLastViewAsLoadingCallback(false)
//            }
//        }

        try {
            Thread {
                Runnable {
                    showLastViewAsLoading.onShowLastViewAsLoadingCallback(true)
                    val request =
                        Request.Builder().url("$urlString?orderBy=\"id\"&startAt=$startRange&endAt=$endRange").build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(request: Request?, e: IOException?) {
                            // pResult.onResult(Collections.<String>emptyList());
                        }

                        override fun onResponse(response: Response?) {
                            if (response?.isSuccessful == true) {
                                val gson = Gson()
                                val dogs: ConcurrentHashMap<Int, DogEntity>? = gson.fromJson(
                                    response
                                        .body()
                                        .string(),
                                    object : TypeToken<Map<Int, DogEntity>>() {}.type
                                )

                                dogs?.let { dogsList?.putAll(it) }
                            }
                        }
                    })
                }.run()
            }


        } catch (e: Exception) {
//            pResult.onResult(Collections.<String>emptyList());
        }


    }

    override fun getEntitiesSize(): Int? {
        return dogsList?.size
    }

}