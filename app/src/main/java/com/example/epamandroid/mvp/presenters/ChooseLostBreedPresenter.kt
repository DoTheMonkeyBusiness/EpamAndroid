package com.example.epamandroid.mvp.presenters

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.mvp.contracts.IChooseLostBreedContract
import com.example.epamandroid.mvp.repository.ChooseLostBreedModel
import com.example.epamandroid.mvp.repository.Repository

class ChooseLostBreedPresenter(private val view: IChooseLostBreedContract.View) : IChooseLostBreedContract.Presenter {

    companion object {
        private const val TAG: String = "ChooseLostBreedPresenter"
    }

    private val repository: IChooseLostBreedContract.Model = Repository
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        Thread {
            val dogList: MutableList<String>? = repository
                .getBreeds()
                ?.filterNotNull()
                ?.toMutableList()

            dogList?.sort()

            handler.post {
                Runnable {
                    view.addElementsToRecyclerView(dogList)
                }.run()
            }
        }.start()
    }

    override fun onDestroy() = Unit
}