package com.example.epamandroid.mvp.presenters

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.entities.DogEntity
import com.example.epamandroid.mvp.contracts.IMainActivityContract
import com.example.epamandroid.mvp.core.IMainActivityPresenter
import com.example.epamandroid.mvp.models.MainActivityModel

class MainActivityPresenter(
    private var mainActivityView: IMainActivityContract.View
) : IMainActivityPresenter, IMainActivityContract.Presenter {

    companion object {
        private const val TAG = "MainActivityPresenter"
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() = Unit

    override fun onDestroy() = Unit

    fun loadDogByBreed(breed:String){
        Thread {
            val dogEntity: DogEntity? = MainActivityModel.getEntity(breed)

            handler.post {
                Runnable {
                    mainActivityView.updateBreedDescription(dogEntity)
                }.run()
            }
        }.start()
    }
}