package com.example.epamandroid.mvp.presenters

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.epamandroid.constants.DogEntityConstants
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
            putString(DogEntityConstants.breed, dogEntity.breed)
            putString(DogEntityConstants.weight, dogEntity.weight)
            putString(DogEntityConstants.height, dogEntity.height)
            putString(DogEntityConstants.description, dogEntity.description)
            putBoolean(DogEntityConstants.canLiveAtHome, dogEntity.isCanLiveAtHome)
            putFloat(DogEntityConstants.breedPopularity, dogEntity.breedPopularity)
            putInt(DogEntityConstants.cost, dogEntity.cost)
            putString(DogEntityConstants.lifeExpectancy, dogEntity.lifeExpectancy)
            putString(DogEntityConstants.photo, dogEntity.photo)
        }

    }
}