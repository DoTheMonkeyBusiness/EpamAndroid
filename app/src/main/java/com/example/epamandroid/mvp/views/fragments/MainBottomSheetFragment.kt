package com.example.epamandroid.mvp.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epamandroid.R
import com.example.epamandroid.constants.DogEntityConstants
import kotlinx.android.synthetic.main.breed_description_fragment.*

class MainBottomSheetFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val bundle = arguments
//
//
//        if (bundle != null && bundle.containsKey(DogEntityConstants.BREED_POPULARITY_EXTRA_KEY))
//        breedDescription.updateDogInfo(bundle)
    }
}