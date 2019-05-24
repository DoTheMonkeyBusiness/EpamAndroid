package com.example.epamandroid.mvp.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epamandroid.R
import com.example.epamandroid.constants.DogEntityConstants.DOG_ENTITY_EXTRA_KEY
import com.example.epamandroid.models.DogEntity
import com.example.imageloader.IMichelangelo
import com.example.imageloader.Michelangelo
import com.example.kotlinextensions.visibleView
import kotlinx.android.synthetic.main.breed_description_fragment.*

class BreedDescriptionFragment : Fragment() {

    private lateinit var michelangelo: IMichelangelo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.breed_description_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        michelangelo = Michelangelo.getInstance(context)

        val bundle = arguments

        if(bundle !== null && bundle.getParcelable<DogEntity?>(DOG_ENTITY_EXTRA_KEY) !== null) {
            val dog = bundle.getParcelable<DogEntity?>(DOG_ENTITY_EXTRA_KEY)
            breedDescription.updateDogInfo(dog)
            michelangelo.load(breedDescription.getDogPhoto(), dog?.photo)
            breedDescription.visibleView()
        }
    }
}