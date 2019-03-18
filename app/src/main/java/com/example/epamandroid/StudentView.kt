package com.example.epamandroid

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.student_view.view.*

class StudentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.student_view, this)
    }

    fun setStudentName(name: String): StudentView {
        Log.d("initStud", name)
        student_view_student_name_textView.text = name
        return this
    }
    fun setHwCount(count: String): StudentView {
        student_view_homework_count_textView.text = count
        return this
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun isStudent(isStudent: Boolean): StudentView {
        when{
            isStudent -> student_view_is_student_icon.setImageResource(R.drawable.ic_studying_true_green)
            !isStudent -> student_view_is_student_icon.setImageResource(R.drawable.ic_studying_false_red)
        }
        return this
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun setStudentIcon(isStudent: Boolean): StudentView {
        when{
            isStudent -> student_view_icon.setImageResource(R.drawable.ic_child_care_black)
            !isStudent -> student_view_icon.setImageResource(R.drawable.ic_sentiment_dissatisfied_black)
        }
        return this
    }


}