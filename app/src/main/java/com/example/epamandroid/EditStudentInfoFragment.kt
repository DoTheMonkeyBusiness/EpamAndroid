package com.example.epamandroid

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import java.lang.NullPointerException

class EditStudentInfoFragment : DialogFragment() {

    private var callback: IEditStudentInfoCallback? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.new_student_dialog, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = activity?.let { AlertDialog.Builder(it) }
        val inflater = activity?.layoutInflater?.inflate(R.layout.edit_student_info_dialog, null)
        val studentName = inflater?.findViewById<EditText>(R.id.edit_student_info_dialog_name_editText)
        val hwCount = inflater?.findViewById<EditText>(R.id.edit_student_info_dialog_homework_count_editText)

        builder?.apply {
            setView(inflater)
            setPositiveButton(R.string.ok) { _, _ ->

                when {
                    (studentName?.text.toString().isNotEmpty()
                            && hwCount?.text.toString().isNotEmpty()) -> {
                        callback?.onEditStudentInfo(studentName?.text.toString(), hwCount?.text.toString())
                    }
                    (studentName?.text.toString().isEmpty()
                            && hwCount?.text.toString().isNotEmpty()) -> {
                        callback?.onEditStudentInfo(null, hwCount?.text.toString())
                    }
                    (studentName?.text.toString().isNotEmpty()
                            && hwCount?.text.toString().isEmpty()) -> {
                        callback?.onEditStudentInfo(studentName?.text.toString(), null)
                    }
                }

                this@EditStudentInfoFragment.dialog.cancel()
            }
            setNegativeButton(R.string.cancel) { _, _ ->

                this@EditStudentInfoFragment.dialog.cancel()
            }

        }

        if (builder != null) {
            return builder.create()
        } else{
            throw NullPointerException()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IEditStudentInfoCallback) {
            callback = context
        }
    }

    interface IEditStudentInfoCallback {
        fun onEditStudentInfo(name: String?, hwCount: String?)
    }
}