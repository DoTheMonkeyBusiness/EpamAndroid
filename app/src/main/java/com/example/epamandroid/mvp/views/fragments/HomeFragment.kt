package com.example.epamandroid.mvp.views.fragments

import android.content.Context
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epamandroid.R
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.contracts.IHomeContract
import com.example.epamandroid.mvp.presenters.HomePresenter
import com.example.epamandroid.mvp.views.adapters.HomeRecyclerViewAdapter
import com.example.epamandroid.mvp.views.annotationclasses.ViewType
import com.example.epamandroid.util.ItemTouchCallback
import com.example.kotlinextensions.collapseBottomSheet
import com.example.kotlinextensions.expandBottomSheet
import kotlinx.android.synthetic.main.bottom_progressbar.*
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(), IHomeContract.View {

    companion object {
        const val RECYCLER_STATE_KEY: String = "recyclerViewKey"
        const val LINEAR_LAYOUT_MANAGER_KEY: String = "linearLayoutKey"
        const val DOGS_LIST_KEY: String = "dogsListKey"
    }

    private var isLoading: Boolean = false
    private var showBottomSheetCallback: IShowBottomSheetCallback? = null
    private var isEndOfList: Boolean = false
    private var bottomProgress: BottomSheetBehavior<View>? = null
    private var saveHomeFragmentStateCallback: ISaveHomeFragmentStateCallback? = null

    private lateinit var homePresenter: HomePresenter
    private lateinit var viewAdapter: HomeRecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mainActivity: AppCompatActivity

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is IShowBottomSheetCallback) {
            showBottomSheetCallback = context
        }
        if (context is ISaveHomeFragmentStateCallback) {
            saveHomeFragmentStateCallback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homePresenter = HomePresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainActivity = (activity as AppCompatActivity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLinearLayoutManager()

        setViewAdapter()

        bottomProgress = BottomSheetBehavior.from(bottomProgressbar)

        homeFragmentRecyclerView.apply {

            layoutManager = linearLayoutManager

            adapter = viewAdapter

            addItemDecoration(DividerItemDecoration(this@HomeFragment.context, DividerItemDecoration.VERTICAL))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = linearLayoutManager.itemCount
                    val startPosition = viewAdapter.getSize()?.plus(1)
                    val visibleItemCount = linearLayoutManager.childCount
                    val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()

                    if (firstVisibleItemPosition + (PAGE_SIZE * 3) <= totalItemCount) {
                        isEndOfList = false
                    }

                    if (lastVisibleItemPosition +1 == totalItemCount)
                    {
                        bottomProgress.expandBottomSheet()
                    } else {
                        bottomProgress.collapseBottomSheet()
                    }

                    if (!isLoading
                        && (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                                && firstVisibleItemPosition >= 0
                                && totalItemCount >= PAGE_SIZE
                                && !isEndOfList)
                    ) {
                        startPosition?.let { loadMoreItems(it, startPosition + PAGE_SIZE) }
                    }
                }

            })

            post { viewAdapter.notifyDataSetChanged() }

        }

        ItemTouchCallback(homeFragmentRecyclerView, viewAdapter).let {
            ItemTouchHelper(it).attachToRecyclerView(
                homeFragmentRecyclerView
            )
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(DOGS_LIST_KEY)) {
            viewAdapter.updateDogsList(savedInstanceState.getParcelableArrayList(DOGS_LIST_KEY))

        } else {
            isLoading = true
            viewAdapter.setShowLastViewAsLoading(isLoading)
        }

        homePresenter.onCreate()

        retainInstance = true
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(DOGS_LIST_KEY)) {
            viewAdapter.updateDogsList(savedInstanceState.getParcelableArrayList(DOGS_LIST_KEY))
        }
    }

    override fun onStop() {
        super.onStop()
        saveHomeFragmentStateCallback?.onSaveHomeFragmentState(this@HomeFragment)
    }

    private fun setLinearLayoutManager() {
        linearLayoutManager = LinearLayoutManager(this@HomeFragment.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setViewAdapter() {
        viewAdapter = HomeRecyclerViewAdapter(context)

        viewAdapter.onItemClick = { dog ->
            if (dog.id?.let { viewAdapter.getItemViewType(it) } == ViewType.DOG) {
                showBottomSheetCallback?.onShowBottomSheetFromHome(viewAdapter.getEntityById(dog.id))
            }
        }
    }

    private fun loadMoreItems(startPosition: Int, endPosition: Int) {
        isLoading = true
        viewAdapter.setShowLastViewAsLoading(isLoading)
        homePresenter.getMoreItems(startPosition, endPosition)
    }

    override fun addElements(dogList: List<DogEntity>?, isFullList: Boolean) {
        viewAdapter.addItems(dogList)
        isLoading = false
        viewAdapter.setShowLastViewAsLoading(isLoading)
        isEndOfList = isFullList
    }

    override fun isEmptyRecyclerView(): Boolean {
        return viewAdapter.isEmptyDogsList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList(DOGS_LIST_KEY, viewAdapter.getDogList())
    }

    interface IShowBottomSheetCallback {
        fun onShowBottomSheetFromHome(dogEntity: DogEntity?)
    }

    interface ISaveHomeFragmentStateCallback {
        fun onSaveHomeFragmentState(fragment: HomeFragment)
    }
}