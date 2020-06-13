package com.example.epamandroid.mvp.views.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epamandroid.R
import com.example.epamandroid.mvp.contracts.IChooseMapTypeContract
import com.example.epamandroid.mvp.presenters.ChooseMapTypePresenter
import com.example.epamandroid.mvp.views.adapters.ChooseMapRestaurantRecyclerViewAdapter
import kotlinx.android.synthetic.main.choose_map_type_fragment.*

class ChooseMapTypeFragment : Fragment(), IChooseMapTypeContract.View {

    companion object {
        private const val TAG: String = "ChooseMapTypeFragment"
    }

    private lateinit var chooseMapTypePresener: IChooseMapTypeContract.Presenter
    private lateinit var viewAdapter: ChooseMapRestaurantRecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var setMapTypeCallback: ISetMapTypeCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ISetMapTypeCallback) {
            setMapTypeCallback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chooseMapTypePresener = ChooseMapTypePresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.choose_map_type_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLinearLayoutManager()

        setViewAdapter()

        setRecycler()

        chooseMapTypePresener.onCreate()
    }

    private fun setLinearLayoutManager() {
        linearLayoutManager = LinearLayoutManager(this@ChooseMapTypeFragment.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setViewAdapter() {
        viewAdapter = ChooseMapRestaurantRecyclerViewAdapter()

        viewAdapter.onItemClick = {
            setMapTypeCallback?.onSelectRestaurantType(it)
//            showBottomSheetCallback?.onShowBottomSheetFromHome(viewAdapter.getEntityById(restaurant.id))
        }
    }

    private fun setRecycler() {
        chooseMapTypeFragmentRecyclerView.apply {
            layoutManager = linearLayoutManager

            adapter = viewAdapter

            addItemDecoration(DividerItemDecoration(this@ChooseMapTypeFragment.context,
                    DividerItemDecoration.VERTICAL))

        }

    }

    override fun addElementsToRecyclerView(restaurantList: List<String>?) {
        viewAdapter.addItems(restaurantList)
    }

    interface ISetMapTypeCallback {
        fun onSelectRestaurantType(type: String)
    }
}