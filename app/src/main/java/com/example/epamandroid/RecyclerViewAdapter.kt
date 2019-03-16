package com.example.epamandroid

import android.support.annotation.IntDef
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.epamandroid.RecyclerViewAdapter.ViewType.Companion.LOADING
import com.example.epamandroid.RecyclerViewAdapter.ViewType.Companion.STUDENT
import com.example.epamandroid.backend.entities.StudentModel
import java.util.*

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    private var isShowLastViewAsLoading = false

    private val students = ArrayList<StudentModel>()
    private lateinit var student: StudentModel

    private val items = object : ArrayList<Any>() {
        init {

            for (i in 0..20) {
                add(Any())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return when (viewType){
            STUDENT ->{
                ViewHolder(StudentView(parent.context))
            }
            LOADING ->{
                ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_progress, parent, false)
                        as FrameLayout)
            }
            else -> {
                ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.text_view, parent, false)
                        as FrameLayout)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (getItemViewType(position) == ViewType.STUDENT) {
            student = students[position]

            (viewHolder.itemView as StudentView)
                .setStudentName(student.name)
                .sethwCount(student.hwCount.toString())
                .isStudent(student.isStudent)
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
            notifyDataSetChanged()
        }
    }

    private fun deleteByIndex(i: Int) {
        items.removeAt(i)

        notifyItemRemoved(i)
        notifyItemRangeChanged(i, items.size)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }

        notifyItemMoved(fromPosition, toPosition)
    }

    fun onItemDismiss(adapterPosition: Int) {
        deleteByIndex(adapterPosition)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    @IntDef(ViewType.STUDENT, ViewType.LOADING)
    internal annotation class ViewType {
        companion object {

            const val STUDENT = 0
            const val LOADING = 1
        }
    }
}