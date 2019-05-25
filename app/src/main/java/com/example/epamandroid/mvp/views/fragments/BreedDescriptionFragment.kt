package com.example.epamandroid.mvp.views.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epamandroid.R
import com.example.epamandroid.constants.DogEntityConstants.DOG_BREED_STRING_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.DOG_ENTITY_EXTRA_KEY
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.contracts.IBreedDescriptionContract
import com.example.epamandroid.mvp.presenters.BreedDescriptionPresenter
import com.example.imageloader.IMichelangelo
import com.example.imageloader.Michelangelo
import com.example.kotlinextensions.goneView
import com.example.kotlinextensions.visibleView
import kotlinx.android.synthetic.main.breed_description_fragment.*

class BreedDescriptionFragment : Fragment(), IBreedDescriptionContract.View {

    private lateinit var michelangelo: IMichelangelo
    private lateinit var presenter: IBreedDescriptionContract.Presenter

    private var breedDescriptionResultCallback: IBreedDescriptionResultCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context is IBreedDescriptionResultCallback){
            breedDescriptionResultCallback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = BreedDescriptionPresenter(this@BreedDescriptionFragment)
        michelangelo = Michelangelo.getInstance(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.breed_description_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val bundleDogEntity: DogEntity? = bundle?.getParcelable<DogEntity?>(DOG_ENTITY_EXTRA_KEY)
        val bundleDogBreed: String? = bundle?.getString(DOG_BREED_STRING_EXTRA_KEY)

        breedDescriptionFragment.goneView()

        if (bundle != null) {
            if (bundleDogEntity !== null) {
                breedDescriptionFragment.updateDogInfo(bundleDogEntity)
                michelangelo.load(breedDescriptionFragment.getBreedPhoto(), bundleDogEntity.photo)
                breedDescriptionFragment.visibleView()
                breedDescriptionResultCallback?.onDescriptionConfirm()
            } else if (bundleDogBreed != null) {
                breedDescriptionResultCallback?.onDescriptionLoading()

                presenter.loadDogByBreed(bundleDogBreed)
            }
        } else {
            breedDescriptionResultCallback?.onDescriptionError()
        }
    }

    override fun updateBreedDescription(dogEntity: DogEntity?) {
        if(dogEntity != null) {
            breedDescriptionFragment.updateDogInfo(dogEntity)
            michelangelo.load(breedDescriptionFragment.getBreedPhoto(), dogEntity.photo)
            breedDescriptionFragment.visibleView()
            breedDescriptionResultCallback?.onDescriptionConfirm()
        } else {
            breedDescriptionResultCallback?.onDescriptionError()
        }
    }

    interface IBreedDescriptionResultCallback {
        fun onDescriptionConfirm()
        fun onDescriptionLoading()
        fun onDescriptionError()
    }
}
