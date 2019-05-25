package com.example.epamandroid.mvp.views.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epamandroid.R
import com.example.epamandroid.constants.LostDogEntityConstants.LOST_DOG_ENTITY_EXTRA_KEY
import com.example.epamandroid.models.LostDogEntity
import com.example.epamandroid.mvp.contracts.ILostBreedDescriptionContract
import com.example.epamandroid.mvp.presenters.LostBreedDescriptionPresenter
import com.example.imageloader.IMichelangelo
import com.example.imageloader.Michelangelo
import com.example.kotlinextensions.goneView
import com.example.kotlinextensions.visibleView
import kotlinx.android.synthetic.main.lost_breed_description_fragment.*
import kotlinx.android.synthetic.main.lost_breed_description_view.*

class LostBreedDescriptionFragment
    : Fragment(),
        ILostBreedDescriptionContract.View {

    companion object {
        private const val TELEPHONE_SCHEME_KEY: String = "tel"
    }

    private lateinit var michelangelo: IMichelangelo
    private lateinit var presenter: ILostBreedDescriptionContract.Presenter

    private var lostBreedDescriptionResultCallback: ILostBreedDescriptionResultCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ILostBreedDescriptionResultCallback) {
            lostBreedDescriptionResultCallback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        michelangelo = Michelangelo.getInstance(context)
        presenter = LostBreedDescriptionPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.lost_breed_description_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val bundleLostDogEntity = bundle?.getParcelable<LostDogEntity?>(LOST_DOG_ENTITY_EXTRA_KEY)

        lostBreedDescriptionFragment.goneView()

        if (bundle != null && bundleLostDogEntity != null) {
            lostBreedDescriptionFragment.updateDogInfo(bundleLostDogEntity)
            michelangelo.load(lostBreedDescriptionFragment.getLostBreedPhoto(), bundleLostDogEntity.photo)
            lostBreedDescriptionFragment.visibleView()
            lostBreedDescriptionResultCallback?.onDescriptionConfirm()
        } else {
            lostBreedDescriptionResultCallback?.onDescriptionError()
        }

        lostBreedDescriptionPhoneNumber.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts(TELEPHONE_SCHEME_KEY, lostBreedDescriptionPhoneNumber.text.toString(), null)))
        }
    }

    interface ILostBreedDescriptionResultCallback {
        fun onDescriptionConfirm()
        fun onDescriptionLoading()
        fun onDescriptionError()
    }
}