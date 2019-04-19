package com.example.epamandroid.mvp.models

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.contracts.IHomeContract
import com.example.epamandroid.entities.DogEntity
import com.example.epamandroid.util.ICallback
import com.example.epamandroid.util.IShowLastViewAsLoadingCallback
import java.util.*

class HomeModel private constructor(): IHomeContract.IModel<DogEntity> {

    companion object {
        private var instance: HomeModel? = null
        fun getInstance(): HomeModel? {
            if(instance == null){
                instance = HomeModel()
            }

            return instance
        }
    }

    private val dogsList: ArrayList<DogEntity>? = ArrayList()
    private val random = Random()
    private val handler = Handler(Looper.getMainLooper())

    init {
        for (i in 0..40) {
            val breed = "Some breed"
            val description = "some description"
            val weight= " 12 - 15"
            val height= " 12 - 15"
            val isCanLiveAtHome: Boolean = random.nextBoolean()
            val isAffectionate: Boolean = random.nextBoolean()
            val isLikes: Boolean = false
            val dogRating: Byte = random.nextInt(5).toByte()

            val dog = DogEntity(
                    i,
                    breed,
                    weight,
                    height,
                    description,
                    isCanLiveAtHome,
                    isAffectionate,
                    isLikes,
                    dogRating)

            dogsList?.add(dog)
        }
    }

    override fun getEntities(startRange: Int, endRange: Int, callback: ICallback<List<DogEntity>>, showLastViewAsLoading: IShowLastViewAsLoadingCallback) {

        when {
            (endRange < dogsList?.size?.minus(1) ?: 0) -> {
                showLastViewAsLoading.onShowLastViewAsLoadingCallback(true)
                handler.postDelayed({ dogsList?.subList(startRange, endRange)?.let { callback.onResult(it) } }, 1000)
            }
            else -> {
                handler.postDelayed({ dogsList?.subList(startRange, dogsList.size)?.let { callback.onResult(it) } }, 1000)
                showLastViewAsLoading.onShowLastViewAsLoadingCallback(false)
            }
        }
    }

    override fun getEntitiesSize(): Int? {
        return dogsList?.size
    }

}