package com.example.epamandroid

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.new_student_dialog.new_student_dialog_name_editText
import kotlinx.android.synthetic.main.new_student_dialog.new_student_dialog_homework_count_editText


class NewStudentFragment : DialogFragment() {

    private var callback: INewStudentCallback? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.new_student_dialog, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = activity?.let { AlertDialog.Builder(it) }
        val inflater = activity?.layoutInflater?.inflate(R.layout.new_student_dialog, null)
        val studentName = inflater?.findViewById<EditText>(R.id.new_student_dialog_name_editText)
        val hwCount = inflater?.findViewById<EditText>(R.id.new_student_dialog_homework_count_editText)

        builder?.apply {
            setView(inflater)
            setPositiveButton(R.string.ok) { _, _ ->

                callback?.onStudentAdd(studentName?.text.toString(), hwCount?.text.toString())

                this@NewStudentFragment.dialog.cancel()
            }
            setNegativeButton(R.string.cancel) { _, _ ->

//                this@NewStudentFragment.dialog.cancel()
            }

        }

        return builder?.create()!!
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is INewStudentCallback){
            callback = context
        }
    }

    interface INewStudentCallback {
        fun onStudentAdd(name: String, hwCount: String)
    }
}