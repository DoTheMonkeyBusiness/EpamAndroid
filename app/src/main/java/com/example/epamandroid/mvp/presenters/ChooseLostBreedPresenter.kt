package com.example.epamandroid.mvp.presenters

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.mvp.contracts.IChooseMapTypeContract
import com.example.epamandroid.mvp.repository.Repository

class ChooseMapTypePresenter(private val view: IChooseMapTypeContract.View) : IChooseMapTypeContract.Presenter {

    companion object {
        private const val TAG: String = "ChooseMapTypePresenter"
    }

    private val repository: IChooseMapTypeContract.Model = Repository
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        Thread {
            val restaurantList: MutableList<String>? = repository
                .getTypes()
                ?.filterNotNull()
                ?.toMutableList()

            restaurantList?.sort()

            handler.post {
                Runnable {
                    view.addElementsToRecyclerView(restaurantList)
                }.run()
            }
        }.start()
    }

    override fun onDestroy() = Unit
}