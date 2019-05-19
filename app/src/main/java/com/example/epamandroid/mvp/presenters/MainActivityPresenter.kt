package com.example.epamandroid.mvp.presenters

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.contracts.IMainActivityContract
import com.example.epamandroid.mvp.core.IBasePresenter
import com.example.epamandroid.mvp.repository.MainActivityModel

class MainActivityPresenter(
    private var mainActivityView: IMainActivityContract.View
) : IBasePresenter, IMainActivityContract.Presenter {

    companion object {
        private const val TAG = "MainActivityPresenter"
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() = Unit

    override fun onDestroy() = Unit

    override fun loadDogByBreed(breed:String){
        Thread {
            val gsonDogEntity: GsonDogEntity? = MainActivityModel.getEntity(breed)
            val dogEntity: DogEntity? = DogEntity(
                    gsonDogEntity?.id,
                    gsonDogEntity?.breed,
                    gsonDogEntity?.weight,
                    gsonDogEntity?.height,
                    gsonDogEntity?.description,
                    gsonDogEntity?.isCanLiveAtHome,
                    gsonDogEntity?.isAffectionate,
                    gsonDogEntity?.breedPopularity,
                    gsonDogEntity?.cost,
                    gsonDogEntity?.lifeExpectancy,
                    gsonDogEntity?.photo)

            handler.post {
                Runnable {
                    mainActivityView.updateBreedDescription(dogEntity)
                }.run()
            }
        }.start()
    }
}