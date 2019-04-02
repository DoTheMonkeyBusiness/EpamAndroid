package com.example.epamandroid

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.example.epamandroid.backend.StudentsWebService
import com.example.epamandroid.backend.entities.StudentModel
import com.example.epamandroid.util.ICallback
import com.example.epamandroid.util.IRemoveEntityCallback
import com.example.epamandroid.util.IShowLastViewAsLoadingCallback
import kotlinx.android.synthetic.main.activity_main.activity_main_recyclerView
import kotlinx.android.synthetic.main.activity_main.activity_main_add_new_student_button

class MainActivity : AppCompatActivity(), NewStudentFragment.INewStudentCallback,
    EditStudentInfoFragment.IEditStudentInfoCallback {

    companion object {
        const val NEW_STUDENT_DIALOG_KEY: String = "newStudentDialog"
        const val EDIT_STUDENT_INFO_STUDENT_DIALOG_KEY: String = "editStudentInfoDialog"
        const val RECYCLER_STATE_KEY: String = "recyclerViewKey"
        const val LINEAR_LAYOUT_MANAGER_KEY: String = "linearLayoutKey"
        const val STUDENT_LIST_KEY: String = "studentListKey"
    }

    private val dialogFragment = NewStudentFragment()
    private val editStudentInfoFragment = EditStudentInfoFragment()
    private val webService: StudentsWebService? = StudentsWebService.getInstance()

    private var isLoading: Boolean = false
    private var studentId: Int = 0

    private lateinit var viewAdapter: RecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLinearLayoutManager()

        if(savedInstanceState != null){
            val pars: Parcelable? = savedInstanceState.getParcelable(RECYCLER_STATE_KEY)
            linearLayoutManager.onRestoreInstanceState(pars)
        }

        setViewAdapter()

        activity_main_recyclerView.apply {

            layoutManager = linearLayoutManager

            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
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

        activity_main_add_new_student_button.setOnClickListener {
            dialogFragment.show(supportFragmentManager, NEW_STUDENT_DIALOG_KEY)
        }

         ItemTouchCallback(activity_main_recyclerView, viewAdapter, object : IRemoveEntityCallback{
             override fun onRemoveEntity(id: Int?){
                 webService?.removeEntitle(id)
             }
         }).let {
            ItemTouchHelper(it).attachToRecyclerView(
                activity_main_recyclerView
            )
        }
        if(savedInstanceState != null){
            activity_main_recyclerView.layoutManager?.onRestoreInstanceState(savedInstanceState.getParcelable(RECYCLER_STATE_KEY))
            linearLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LINEAR_LAYOUT_MANAGER_KEY))
            viewAdapter.addItems(savedInstanceState.getParcelableArrayList(STUDENT_LIST_KEY))
        } else {
            loadStartItems()
        }
    }

    private fun setLinearLayoutManager() {
        linearLayoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
    }

    private fun setViewAdapter() {
        viewAdapter = RecyclerViewAdapter()

        viewAdapter.onItemClick = { student ->
            studentId = student.id
            if (viewAdapter.getItemViewType(studentId) == ViewType.STUDENT) {
                editStudentInfoFragment.show(supportFragmentManager, EDIT_STUDENT_INFO_STUDENT_DIALOG_KEY)
            }
        }
    }

    private fun loadStartItems() {
        loadMoreItems(0, PAGE_SIZE * 3)
    }

    private fun loadMoreItems(startPosition: Int, endPosition: Int) {
        isLoading = true

        webService?.getEntities(startPosition, endPosition,
            object : ICallback<List<StudentModel>> {
                override fun onResult(result: List<StudentModel>) {
                    viewAdapter.addItems(result)
                    isLoading = false
                }
            }, object : IShowLastViewAsLoadingCallback {
                override fun onShowLastViewAsLoadingCallback(isShow: Boolean) {
                    viewAdapter.setShowLastViewAsLoading(isShow)
                }
            })
    }

    override fun onStudentAdd(name: String, hwCount: String) {
        webService?.addEntitle(name, hwCount)
    }

    override fun onEditStudentInfo(name: String?, hwCount: String?) {
        webService?.editStudentInfo(studentId, name, hwCount)
        viewAdapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putParcelable(RECYCLER_STATE_KEY, activity_main_recyclerView.layoutManager?.onSaveInstanceState())
        outState?.putParcelable(LINEAR_LAYOUT_MANAGER_KEY, linearLayoutManager.onSaveInstanceState())
        outState?.putParcelableArrayList(STUDENT_LIST_KEY, viewAdapter.getItems())
    }

}
