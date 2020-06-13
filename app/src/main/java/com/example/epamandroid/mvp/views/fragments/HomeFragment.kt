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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epamandroid.R
import com.example.epamandroid.models.RestaurantEntity
import com.example.epamandroid.mvp.contracts.IHomeContract
import com.example.epamandroid.mvp.presenters.HomePresenter
import com.example.epamandroid.mvp.views.adapters.HomeRecyclerViewAdapter
import com.example.epamandroid.mvp.views.annotationclasses.ViewType
import com.example.kotlinextensions.collapseBottomSheet
import com.example.kotlinextensions.expandBottomSheet
import kotlinx.android.synthetic.main.bottom_progressbar.*
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(), IHomeContract.View {

    companion object {
        private const val TAG: String = "HomeFragment"
        private const val RESTAURANTS_LIST_KEY: String = "restaurantsListKey"
    }

    private var isLoading: Boolean = false
    private var showBottomSheetCallback: IShowBottomSheetCallback? = null
    private var isEndOfList: Boolean = false
    private var bottomProgress: BottomSheetBehavior<View>? = null
    private var saveHomeFragmentStateCallback: ISaveHomeFragmentStateCallback? = null

    private lateinit var homePresenter: IHomeContract.Presenter
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

        setRecycler()

        if (savedInstanceState != null && savedInstanceState.containsKey(RESTAURANTS_LIST_KEY)) {
            viewAdapter.updateRestaurantsList(savedInstanceState.getParcelableArrayList(RESTAURANTS_LIST_KEY))

        } else {
            isLoading = true
            bottomProgress.expandBottomSheet()
        }

        homePresenter.onCreate()
    }

    private fun setRecycler() {
        homeFragmentRecyclerView.apply {

            layoutManager = linearLayoutManager

            adapter = viewAdapter

            addItemDecoration(
                DividerItemDecoration(
                    this@HomeFragment.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = linearLayoutManager.itemCount
                    val startPosition = viewAdapter.getMaxId().plus(1)
                    val visibleItemCount = linearLayoutManager.childCount
                    val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                    if (firstVisibleItemPosition + (PAGE_SIZE * 3) <= totalItemCount) {
                        isEndOfList = false
                    }

                    if (!isLoading
                        && (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                                && firstVisibleItemPosition >= 0
                                && totalItemCount >= PAGE_SIZE
                                && !isEndOfList)
                    ) {
                        loadMoreItems(startPosition, startPosition + PAGE_SIZE)
                    }
                }

            })

        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(RESTAURANTS_LIST_KEY)) {
            viewAdapter.updateRestaurantsList(savedInstanceState.getParcelableArrayList(RESTAURANTS_LIST_KEY))
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

        viewAdapter.onItemClick = { restaurant ->
            if (restaurant.id?.let { viewAdapter.getItemViewType(it) } == ViewType.RESTAURANT) {
                showBottomSheetCallback?.onShowBottomSheetFromHome(viewAdapter.getEntityById(restaurant.id))
            }
        }
    }

    private fun loadMoreItems(startPosition: Int, endPosition: Int) {
        isLoading = true
        bottomProgress.expandBottomSheet()

        homePresenter.getMoreItems(startPosition, endPosition)
    }

    override fun addElements(restaurantList: List<RestaurantEntity>?, isFullList: Boolean) {
        viewAdapter.addItems(restaurantList)
        isLoading = false
        bottomProgress.collapseBottomSheet()

        isEndOfList = isFullList
    }

    override fun isEmptyRecyclerView(): Boolean {
        return viewAdapter.isEmptyRestaurantsList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList(RESTAURANTS_LIST_KEY, viewAdapter.getRestaurantList())
    }

    interface IShowBottomSheetCallback {
        fun onShowBottomSheetFromHome(restaurantEntity: RestaurantEntity?)
    }

    interface ISaveHomeFragmentStateCallback {
        fun onSaveHomeFragmentState(fragment: HomeFragment)
    }
}