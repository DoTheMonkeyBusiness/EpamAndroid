package com.example.epamandroid

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.epamandroid.ViewType.Companion.LOADING
import com.example.epamandroid.ViewType.Companion.STUDENT
import com.example.epamandroid.backend.entities.StudentModel
import java.util.*

class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val students = ArrayList<StudentModel>()

    private var isShowLastViewAsLoading = false

    private lateinit var student: StudentModel

    var onItemClick: ((StudentModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return when (viewType) {
            STUDENT -> {
                ViewHolder(StudentView(parent.context))
            }
            LOADING -> {
                ViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_progress, parent, false)
                        as FrameLayout)
            }
            else -> {
                ViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.error_view, parent, false)
                        as FrameLayout)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (getItemViewType(position) == ViewType.STUDENT) {
            student = students[position]

            (viewHolder.itemView as StudentView)
                    .setStudentName(student.name)
                    .setHwCount(student.hwCount.toString())
                    .isStudent(student.isStudent)
                    .setStudentIcon(student.isStudent)
        }
    }

    override fun getItemCount(): Int {
        return if (isShowLastViewAsLoading) {
            students.size + 1
        } else {
            students.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < students.size) {
            ViewType.STUDENT
        } else {
            ViewType.LOADING
        }
    }

    fun setShowLastViewAsLoading(isShow: Boolean) {
        if (isShow != isShowLastViewAsLoading) {
            isShowLastViewAsLoading = isShow
        }
    }

    fun addItems(result: List<StudentModel>) {
        students.addAll(result)
        notifyDataSetChanged()
    }

    private fun deleteByIndex(i: Int) {
        students.removeAt(i)

        notifyItemRemoved(i)
        notifyItemRangeChanged(i, students.size - 1)
    }

    fun getMaxStudentId(): Int {
        var maxId = 0
        return when {
            students.size == 0 -> maxId
            else -> {
                for (i in students.indices) {
                    if (i != 0
                            && (students[i].id > students[i - 1].id)) {
                        maxId = students[i].id
                    }
                }
                maxId
            }
        }
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(students, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(students, i, i - 1)
            }
        }

        notifyItemMoved(fromPosition, toPosition)
    }

    fun onItemDismiss(adapterPosition: Int) {
        if (getItemViewType(adapterPosition) == ViewType.STUDENT) {
            deleteByIndex(adapterPosition)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        init{
            view.setOnClickListener {
                onItemClick?.invoke(students[adapterPosition])
            }
        }
    }
}