package com.example.epamandroid.mvp.presenters

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.constants.SymbolConstants.DOT_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants.STORAGE_LOST_DOGS_URL_STRING_EXTRA_KEY
import com.example.epamandroid.constants.URLConstants.ALT_MEDIA_EXTRA_KEY
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.mvp.contracts.IAddLostDogContract
import com.example.epamandroid.mvp.repository.Repository
import java.io.File
import java.util.*

class AddLostDogPresenter(private val view: IAddLostDogContract.View) : IAddLostDogContract.Presenter {
    private val repository: IAddLostDogContract.Model = Repository
    private val handler = Handler(Looper.getMainLooper())
    private val imageId: UUID = UUID.randomUUID()

    override fun onCreate() = Unit

    override fun uploadLostDog(breed: String,
                               phoneNumber: String,
                               description: String,
                               latitude: Double,
                               longitude: Double,
                               imageFile: File?) {
        Thread {
            if (imageFile != null &&  repository.uploadImage(imageFile, imageId)) {

                val isPostSuccess: Boolean = repository.putLostBreed(
                    GsonLostDogEntity(
                        UUID.randomUUID(),
                        breed,
                        phoneNumber,
                        description,
                        latitude,
                        longitude,
                        Calendar.getInstance().time.toString(),
                        "$STORAGE_LOST_DOGS_URL_STRING_EXTRA_KEY$imageId$DOT_EXTRA_KEY${imageFile.extension}$ALT_MEDIA_EXTRA_KEY"
                    )
                )

                handler.post {
                    Runnable {
                        if (isPostSuccess) {
                            view.postSuccess()
                        } else {
                            view.postError()
                        }
                    }.run()
                }
            } else {
                handler.post {
                    Runnable {
                       view.imageUploadError()
                    }.run()
                }
            }
        }.start()
    }

    override fun onDestroy() = Unit
}