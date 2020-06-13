package com.example.epamandroid.mvp.views.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epamandroid.R
import com.example.epamandroid.constants.RestaurantEntityConstants.RESTAURANT_TYPE_STRING_EXTRA_KEY
import com.example.epamandroid.constants.RestaurantEntityConstants.RESTAURANT_ENTITY_EXTRA_KEY
import com.example.epamandroid.models.RestaurantEntity
import com.example.epamandroid.mvp.contracts.ITypeDescriptionContract
import com.example.epamandroid.mvp.presenters.TypeDescriptionPresenter
import com.example.imageloader.IMichelangelo
import com.example.imageloader.Michelangelo
import com.example.kotlinextensions.goneView
import com.example.kotlinextensions.visibleView
import kotlinx.android.synthetic.main.type_description_fragment.*

class TypeDescriptionFragment : Fragment(), ITypeDescriptionContract.View {

    companion object {
        private const val TAG: String = "TypeDescriptionFragment"
        private const val RESTAURANT_TYPE_ENTITY_STATE_KEY: String = "restaurantTypeEntityStateKey"
    }

    private lateinit var michelangelo: IMichelangelo
    private lateinit var presenter: ITypeDescriptionContract.Presenter

    private var restaurantEntityState: RestaurantEntity? = null
    private var typeDescriptionResultCallback: ITypeDescriptionResultCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ITypeDescriptionResultCallback) {
            typeDescriptionResultCallback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = TypeDescriptionPresenter(this@TypeDescriptionFragment)
        michelangelo = Michelangelo.getInstance(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.type_description_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val bundleRestaurantEntity: RestaurantEntity? = bundle?.getParcelable<RestaurantEntity?>(RESTAURANT_ENTITY_EXTRA_KEY)
        val bundleRestaurantType: String? = bundle?.getString(RESTAURANT_TYPE_STRING_EXTRA_KEY)

        typeDescriptionFragment.goneView()

        if (bundle != null) {
            when {
                (bundleRestaurantEntity !== null) -> {
                    updateTypeDescription(bundleRestaurantEntity)
                }
                (savedInstanceState != null && savedInstanceState.containsKey(RESTAURANT_TYPE_ENTITY_STATE_KEY)) -> {
                    updateTypeDescription(savedInstanceState.getParcelable(RESTAURANT_TYPE_ENTITY_STATE_KEY))
                }
                (bundleRestaurantType != null) -> {
                    typeDescriptionResultCallback?.onDescriptionLoading()

                    presenter.loadRestaurantByType(bundleRestaurantType)
                }
            }
        } else {
            typeDescriptionResultCallback?.onDescriptionError()
        }
    }

    override fun updateTypeDescription(restaurantEntity: RestaurantEntity?) {
        if (restaurantEntity != null) {
            restaurantEntityState = restaurantEntity
            typeDescriptionFragment.updateRestaurantInfo(restaurantEntity)
            michelangelo.load(typeDescriptionFragment.getTypePhoto(), restaurantEntity.photo)
            typeDescriptionFragment.visibleView()
            typeDescriptionResultCallback?.onDescriptionConfirm()
        } else {
            typeDescriptionResultCallback?.onDescriptionError()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(RESTAURANT_TYPE_ENTITY_STATE_KEY, restaurantEntityState)
    }

    interface ITypeDescriptionResultCallback {
        fun onDescriptionConfirm()
        fun onDescriptionLoading()
        fun onDescriptionError()
    }
}
