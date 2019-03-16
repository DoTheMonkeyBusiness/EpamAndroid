package com.example.epamandroid

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.example.epamandroid.backend.entities.StudentModel
import kotlinx.android.synthetic.main.student_view.view.*

class StudendView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.student_view, this)
    }

    fun updateUserInfo(studentModel: StudentModel) {
        student_view_student_name_textView.text = studentModel.name
        student_view_homework_count_textView.text = studentModel.hwCount.toString()

        when {
            studentModel.isStudent -> student_view_is_student_icon.visibility = View.VISIBLE
            !studentModel.isStudent -> student_view_expelled_icon.visibility = View.VISIBLE
        }
    }
}