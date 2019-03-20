package com.example.epamandroid

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.epamandroid.backend.StudentsWebService
import com.example.epamandroid.backend.entities.StudentModel
import com.example.epamandroid.util.ICallback
import kotlinx.android.synthetic.main.activity_main.activity_main_recyclerView
import kotlinx.android.synthetic.main.activity_main.activity_main_add_new_student_button

class MainActivity : AppCompatActivity(), NewStudentFragment.INewStudentCallback {

    private val dialogFragment = NewStudentFragment()
    private val webService: StudentsWebService = StudentsWebService()

    private lateinit var viewAdapter: RecyclerViewAdapter

    private var linearLayoutManager: LinearLayoutManager? = null
    private var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

        viewAdapter = RecyclerViewAdapter()

        viewAdapter.onItemClick = { student ->
            Log.d("check", student.id.toString())
        }

        activity_main_recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = linearLayoutManager!!.itemCount
                    val startPosition = viewAdapter.getMaxStudentId() + 1

                    if (totalItemCount >= webService.getEntitiesSize()) {
                        viewAdapter.setShowLastViewAsLoading(false)

                        return
                    }

                    val visibleItemCount = linearLayoutManager!!.childCount
                    val firstVisibleItemPosition = linearLayoutManager!!.findFirstVisibleItemPosition()

                    if (!isLoading
                            && (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                                    && firstVisibleItemPosition >= 0
                                    && totalItemCount >= PAGE_SIZE)
                    ) {
                        loadMoreItems(startPosition, startPosition + PAGE_SIZE)
                    }
                }

            })

            post { viewAdapter.notifyDataSetChanged() }
        }

        activity_main_add_new_student_button.setOnClickListener {
            dialogFragment.show(supportFragmentManager, "newStudentDialog")
        }

        ItemTouchHelper(ItemTouchCallback(activity_main_recyclerView, viewAdapter, webService)).attachToRecyclerView(activity_main_recyclerView)

        loadMoreItems(0, PAGE_SIZE * 3)
    }

    private fun loadMoreItems(startPosition: Int, endPosition: Int) {
        isLoading = true
        viewAdapter.setShowLastViewAsLoading(true)

        webService.getEntities(startPosition, endPosition, object : ICallback<List<StudentModel>> {
            override fun onResult(result: List<StudentModel>) {
                viewAdapter.addItems(result)
                isLoading = false
            }
        })
    }

    override fun onStudentAdd(name: String, hwCount: String) {
        webService.addEntitle(name, hwCount)
    }
}
