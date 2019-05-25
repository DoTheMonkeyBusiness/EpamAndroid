package com.example.epamandroid.mvp.presenters

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.contracts.IBreedDescriptionContract
import com.example.epamandroid.mvp.repository.BreedDescriptionModel

class BreedDescriptionPresenter(private val view: IBreedDescriptionContract.View) : IBreedDescriptionContract.Presenter {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() = Unit

    override fun loadDogByBreed(breed:String){
        Thread {
            val gsonDogEntity: GsonDogEntity? = BreedDescriptionModel.getEntity(breed)
            var dogEntity: DogEntity? = null
            if(gsonDogEntity != null) {
                dogEntity = DogEntity(
                        gsonDogEntity.id,
                        gsonDogEntity.breed,
                        gsonDogEntity.weight,
                        gsonDogEntity.height,
                        gsonDogEntity.description,
                        gsonDogEntity.isCanLiveAtHome,
                        gsonDogEntity.isAffectionate,
                        gsonDogEntity.breedPopularity,
                        gsonDogEntity.cost,
                        gsonDogEntity.lifeExpectancy,
                        gsonDogEntity.photo
                )
            }

            handler.post {
                Runnable {
                    view.updateBreedDescription(dogEntity)
                }.run()
            }
        }.start()
    }

    override fun onDestroy() = Unit
}