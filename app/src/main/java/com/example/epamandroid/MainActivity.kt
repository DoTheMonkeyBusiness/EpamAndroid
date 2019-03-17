package com.example.epamandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.example.epamandroid.backend.StudentsWebService
import com.example.epamandroid.backend.entities.StudentModel
import com.example.epamandroid.util.ICallback
import kotlinx.android.synthetic.main.activity_main.activity_main_recyclerView

private const val MAX_VISIBLE_ITEMS = 40

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerViewAdapter

    private val webService = StudentsWebService()

    private var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        viewAdapter = RecyclerViewAdapter()
        activity_main_recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
//            addOnScrollListener(object : RecyclerView.OnScrollListener(){
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//                }
//
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//                    val totalItemCount = layoutManager!!.itemCount
//
//                    if (totalItemCount > MAX_VISIBLE_ITEMS) {
//                        RecyclerViewAdapter.setShowLastViewAsLoading(false)
//
//                        return
//                    }
//
//                    val visibleItemCount = mLayoutManager.getChildCount()
//                    val firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition()
//
//                    if (!mIsLoading) {
//                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
//                            && firstVisibleItemPosition >= 0
//                            && totalItemCount >= PAGE_SIZE
//                        ) {
//                            loadMoreItems(totalItemCount, totalItemCount + PAGE_SIZE)
//                        }
//                    }
//                }
//
//            })
        }
        ItemTouchHelper(ItemTouchCallback(activity_main_recyclerView, viewAdapter)).attachToRecyclerView(activity_main_recyclerView)

        loadMoreItems(0, 10)

    }

    private fun loadMoreItems(pStartPosition: Int, pEndPosition: Int) {
        isLoading = true
        viewAdapter.setShowLastViewAsLoading(true)
        webService.getEntities(pStartPosition, pEndPosition, object : ICallback<List<StudentModel>> {

            override fun onResult(pResult: List<StudentModel>) {
                viewAdapter.addItems(pResult)
                isLoading = false
            }
        })
    }
}
