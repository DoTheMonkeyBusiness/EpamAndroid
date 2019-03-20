package com.example.epamandroid

import android.nfc.tech.MifareUltralight.PAGE_SIZE
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
import kotlinx.android.synthetic.main.activity_main.activity_main_add_new_student_button

class MainActivity : AppCompatActivity(), NewStudentFragment.INewStudentCallback,
    EditStudentInfoFragment.IEditStudentInfoCallback {

    companion object {
        const val NEW_STUDENT_DIALOG_KEY: String = "newStudentDialog"
        const val EDIT_STUDENT_INFO_STUDENT_DIALOG_KEY: String = "editStudentInfoDialog"
    }

    private val dialogFragment = NewStudentFragment()
    private val editStudentInfoFragment = EditStudentInfoFragment()
    private val webService: StudentsWebService = StudentsWebService()

    private var linearLayoutManager: LinearLayoutManager? = null

    private var isLoading: Boolean = false
    private var studentId: Int? = null

    private lateinit var viewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLinearLayoutManager()

        setViewAdapter()

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
            dialogFragment.show(supportFragmentManager, NEW_STUDENT_DIALOG_KEY)
        }

        ItemTouchHelper(ItemTouchCallback(activity_main_recyclerView, viewAdapter, webService)).attachToRecyclerView(
            activity_main_recyclerView
        )

        loadStartItems()
    }

    private fun setLinearLayoutManager() {
        linearLayoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
    }

    private fun setViewAdapter() {
        viewAdapter = RecyclerViewAdapter()

        viewAdapter.onItemClick = { student ->
            studentId = student.id
            if (viewAdapter.getItemViewType(studentId!!) == ViewType.STUDENT) {
                editStudentInfoFragment.show(supportFragmentManager, EDIT_STUDENT_INFO_STUDENT_DIALOG_KEY)
            }
        }
    }

    private fun loadStartItems() {
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

    override fun onEditStudentInfo(name: String, hwCount: String) {
        studentId?.let { webService.editStudentInfo(it, name, hwCount) }
        viewAdapter.onItemChanged()
    }
}
