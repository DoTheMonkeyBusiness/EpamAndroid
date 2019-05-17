package com.example.epamandroid.mvp.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epamandroid.R
import com.example.epamandroid.mvp.contracts.IChooseLostBreedContract
import com.example.epamandroid.mvp.presenters.ChooseLostBreedPresenter
import com.example.epamandroid.mvp.views.adapters.ChooseLostDogRecyclerViewAdapter
import kotlinx.android.synthetic.main.choose_lost_breed_fragment.*

class ChooseLostBreedFragment : Fragment(), IChooseLostBreedContract.View {

    companion object {
        private const val TAG: String = "ChooseLostBreedFragment"
    }

    private lateinit var chooseLostBreedPresener: IChooseLostBreedContract.Presenter
    private lateinit var viewAdapter: ChooseLostDogRecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chooseLostBreedPresener = ChooseLostBreedPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.choose_lost_breed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLinearLayoutManager()

        setViewAdapter()

        setRecycler()

        chooseLostBreedPresener.onCreate()
    }

    private fun setLinearLayoutManager() {
        linearLayoutManager = LinearLayoutManager(this@ChooseLostBreedFragment.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setViewAdapter() {
        viewAdapter = ChooseLostDogRecyclerViewAdapter()

        viewAdapter.onItemClick = {
            //TODO callback to activity here
//            showBottomSheetCallback?.onShowBottomSheetFromHome(viewAdapter.getEntityById(dog.id))
        }
    }

    private fun setRecycler() {
        chooseLostBreedFragmentRecyclerView.apply {
            layoutManager = linearLayoutManager

            adapter = viewAdapter

            addItemDecoration(DividerItemDecoration(this@ChooseLostBreedFragment.context,
                    DividerItemDecoration.VERTICAL))

        }

    }

    override fun addElementsToRecyclerView(dogList: List<String>?) {
        viewAdapter.addItems(dogList)
    }
}