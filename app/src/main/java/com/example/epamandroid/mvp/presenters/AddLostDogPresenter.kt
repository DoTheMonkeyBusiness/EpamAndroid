package com.example.epamandroid.mvp.presenters

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.mvp.contracts.IAddLostDogContract
import com.example.epamandroid.mvp.repository.AddLostDogModel
import java.util.*

class AddLostDogPresenter(private val view: IAddLostDogContract.View) : IAddLostDogContract.Presenter {
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() = Unit

    override fun uploadLostDog(breed: String,
                               phoneNumber: String,
                               description: String,
                               latitude: Double,
                               longitude: Double,
                               photo: String) {
        Thread {
            //TODO change to date & to UUID
            val isPostSuccess: Boolean = AddLostDogModel.putLostBreed(
                            GsonLostDogEntity(
                                    UUID.randomUUID(),
                                    breed,
                                    phoneNumber,
                                    description,
                                    latitude,
                                    longitude,
                                    Calendar.getInstance().time.toString(),
                                    photo))

            handler.post{
                Runnable {
                    if(isPostSuccess) {
                        view.onPostSuccess()
                    } else {
                        view.onPostError()
                    }
                }.run()
            }
        }.start()
    }

    override fun onDestroy() = Unit
}