package com.example.epamandroid.mvp.presenters

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.epamandroid.constants.DogEntitieConstants
import com.example.epamandroid.entities.DogEntity
import com.example.epamandroid.mvp.contracts.IHomeContract
import com.example.epamandroid.mvp.models.HomeModel
import com.example.epamandroid.mvp.views.fragments.HomeFragment
import com.example.epamandroid.util.IAddItemsToRecyclerCallback

class HomePresenter(view: HomeFragment) : IHomeContract.IPresenter {

    companion object {
        private const val TAG = "HomePresenter"
    }

    private val handler = Handler(Looper.getMainLooper())

    fun getMoreItems(
            startPosition: Int,
            endPosition: Int,
            callback: IAddItemsToRecyclerCallback<List<DogEntity>>
    ) {
        Thread {
            val dogList: List<DogEntity>? =
                    HomeModel.getEntities(startPosition, endPosition)
                            ?.sortedBy { it.id }

            handler.post {
                Runnable {
                    callback.onResult(dogList)
                    callback.onShowLastViewAsLoading(dogList?.last()?.id == endPosition)
                }.run()
            }
        }.start()
    }

    fun putDogInfoInBundle(dogEntity: DogEntity): Bundle? {
        val bundle: Bundle? = Bundle()

        return bundle?.apply {
            putString(DogEntitieConstants.breed, dogEntity.breed)
            putString(DogEntitieConstants.weight, dogEntity.weight)
            putString(DogEntitieConstants.height, dogEntity.height)
            putString(DogEntitieConstants.description, dogEntity.description)
            putBoolean(DogEntitieConstants.canLiveAtHome, dogEntity.isCanLiveAtHome)
            putFloat(DogEntitieConstants.breedPopularity, dogEntity.breedPopularity)
            putInt(DogEntitieConstants.cost, dogEntity.cost)
            putString(DogEntitieConstants.lifeExpectancy, dogEntity.lifeExpectancy)
            putString(DogEntitieConstants.photo, dogEntity.photo)
        }

    }
}