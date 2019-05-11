package com.example.epamandroid.mvp.presenters

import android.nfc.tech.MifareUltralight
import android.os.Handler
import android.os.Looper
import com.example.epamandroid.entities.DogEntity
import com.example.epamandroid.mvp.contracts.IHomeContract
import com.example.epamandroid.mvp.models.HomeModel

class HomePresenter(private val view: IHomeContract.View) : IHomeContract.Presenter {

    companion object {
        private const val TAG: String = "HomePresenter"
        private const val START_OF_RANGE_KEY: Int = 0
        private const val END_OF_RANGE_KEY: Int = MifareUltralight.PAGE_SIZE * 3
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        if (view.isEmptyRecyclerView()) {
            Thread {
                val dogList: List<DogEntity>? =
                        HomeModel
                                .getEntities(START_OF_RANGE_KEY, END_OF_RANGE_KEY)
                                ?.sortedBy { it.id }

                giveElementsToView(dogList, END_OF_RANGE_KEY)
            }.start()
        }
    }

    override fun getMoreItems(
            startPosition: Int,
            endPosition: Int) {
        Thread {
            val dogList: List<DogEntity>? =
                    HomeModel
                            .getEntities(startPosition, endPosition)
                            ?.sortedBy { it.id }

            giveElementsToView(dogList, endPosition)
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