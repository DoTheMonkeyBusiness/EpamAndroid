package com.example.epamandroid.mvp.presenters

import android.nfc.tech.MifareUltralight
import android.os.Handler
import android.os.Looper
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.contracts.IHomeContract
import com.example.epamandroid.mvp.repository.HomeModel

class HomePresenter(private val view: IHomeContract.View) : IHomeContract.Presenter {

    companion object {
        private const val TAG: String = "HomePresenter"
        private const val START_OF_RANGE_KEY: Int = 0
        private const val END_OF_RANGE_KEY: Int = MifareUltralight.PAGE_SIZE * 3
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        if (view.isEmptyRecyclerView()) {
            getItemsFromModel(START_OF_RANGE_KEY, END_OF_RANGE_KEY)
        }
    }

    override fun getMoreItems(
            startPosition: Int,
            endPosition: Int) {
        Thread {
           getItemsFromModel(startPosition, endPosition)
        }.start()
    }

    private fun getItemsFromModel(startPosition: Int,
                                  endPosition: Int) {
        Thread {
            val dogList: ArrayList<DogEntity>? = arrayListOf()
            val gsonDogMap: HashMap<Int, GsonDogEntity>? = HomeModel
                    .getEntities(startPosition, endPosition)

            gsonDogMap?.forEach { dogList?.add(
                    DogEntity(
                            it.value.id,
                            it.value.breed,
                            it.value.weight,
                            it.value.height,
                            it.value.description,
                            it.value.isCanLiveAtHome,
                            it.value.isAffectionate,
                            it.value.breedPopularity,
                            it.value.cost,
                            it.value.lifeExpectancy,
                            it.value.photo)) }

            giveElementsToView(dogList?.sortedBy { it.breed }, endPosition)
        }.start()
    }

    private fun isFoolList(dogList: List<DogEntity>?, endPosition: Int): Boolean {

        return !(dogList != null &&
                dogList.size == endPosition)

    }

    private fun giveElementsToView(dogList: List<DogEntity>?, endPosition: Int) {
        handler.post {
            Runnable {
                view.addElements(dogList, isFoolList(dogList, endPosition))
            }.run()
        }
    }

    override fun onDestroy() = Unit
}