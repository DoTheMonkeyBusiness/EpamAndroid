package com.example.epamandroid.views.fragments

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.epamandroid.R
import com.example.epamandroid.entities.DogEntity
import com.example.epamandroid.models.HomeModel
import com.example.epamandroid.presenters.HomePresenter
import com.example.epamandroid.util.ICallback
import com.example.epamandroid.util.IShowLastViewAsLoadingCallback
import com.example.epamandroid.views.adapters.HomeRecyclerViewAdapter
import com.example.epamandroid.views.annotationclasses.ViewType
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    companion object {
        const val NEW_STUDENT_DIALOG_KEY: String = "newStudentDialog"
        const val EDIT_STUDENT_INFO_STUDENT_DIALOG_KEY: String = "editStudentInfoDialog"
        const val RECYCLER_STATE_KEY: String = "recyclerViewKey"
        const val LINEAR_LAYOUT_MANAGER_KEY: String = "linearLayoutKey"
        const val STUDENT_LIST_KEY: String = "studentListKey"
    }

    private val webService: HomeModel? = HomeModel.getInstance()
    private val homePresenter: HomePresenter? = HomePresenter.getInstance()

    private var isLoading: Boolean = false
    private var studentId: Int = 0

    private lateinit var viewAdapter: HomeRecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLinearLayoutManager()

        setViewAdapter()

        homeFragmentRecyclerView.apply {

            layoutManager = linearLayoutManager

            adapter = viewAdapter

            addItemDecoration(DividerItemDecoration(this@HomeFragment.context, DividerItemDecoration.VERTICAL))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = linearLayoutManager.itemCount
                    val startPosition = viewAdapter.getMaxStudentId()?.plus(1)

                    if (totalItemCount >= webService?.getEntitiesSize() ?: 0) {
                        viewAdapter.setShowLastViewAsLoading(false)

                        return
                    }

                    val visibleItemCount = linearLayoutManager.childCount
                    val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading
                            && (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                                    && firstVisibleItemPosition >= 0
                                    && totalItemCount >= PAGE_SIZE)
                    ) {
                        startPosition?.let { loadMoreItems(it, startPosition + PAGE_SIZE) }
                    }
                }

            })

            post { viewAdapter.notifyDataSetChanged() }

        }
    }

    override fun onResume() {
        super.onResume()
        restoringState()
    }

    private fun restoringState() {
        if (homePresenter?.recyclerViewState != null) {
            homeFragmentRecyclerView
                    .layoutManager
                    ?.onRestoreInstanceState(
                            homePresenter.recyclerViewState?.getParcelable(
                                    RECYCLER_STATE_KEY
                            )
                    )
            linearLayoutManager
                    .onRestoreInstanceState(homePresenter.recyclerViewState
                            ?.getParcelable(LINEAR_LAYOUT_MANAGER_KEY))
            viewAdapter
                    .addItems(homePresenter.recyclerViewState
                            ?.getParcelableArrayList(STUDENT_LIST_KEY))
        } else {
            loadStartItems()
        }
    }

    private fun setLinearLayoutManager() {
        linearLayoutManager = LinearLayoutManager(this@HomeFragment.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setViewAdapter() {
        viewAdapter = HomeRecyclerViewAdapter()

        viewAdapter.onItemClick = { student ->
            studentId = student.id
            if (viewAdapter.getItemViewType(studentId) == ViewType.DOG) {
//                editStudentInfoFragment.show(supportFragmentManager, EDIT_STUDENT_INFO_STUDENT_DIALOG_KEY)
            }
        }
    }

    private fun loadStartItems() {
        loadMoreItems(0, PAGE_SIZE * 3)
    }

    private fun loadMoreItems(startPosition: Int, endPosition: Int) {
        isLoading = true

        webService?.getEntities(startPosition, endPosition,
                object : ICallback<List<DogEntity>> {
                    override fun onResult(result: List<DogEntity>) {
                        viewAdapter.addItems(result)
                        isLoading = false
                    }
                }, object : IShowLastViewAsLoadingCallback {
            override fun onShowLastViewAsLoadingCallback(isShow: Boolean) {
                viewAdapter.setShowLastViewAsLoading(isShow)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        homePresenter?.recyclerViewState = Bundle()

        homePresenter?.recyclerViewState?.putParcelable(RECYCLER_STATE_KEY, homeFragmentRecyclerView.layoutManager?.onSaveInstanceState())
        homePresenter?.recyclerViewState?.putParcelable(LINEAR_LAYOUT_MANAGER_KEY, linearLayoutManager.onSaveInstanceState())
        homePresenter?.recyclerViewState?.putParcelableArrayList(STUDENT_LIST_KEY, viewAdapter.getItems())
    }
}