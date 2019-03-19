package com.example.epamandroid

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog



class NewStudentFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = activity?.let { AlertDialog.Builder(it)}
        val inflater = activity?.layoutInflater

        builder?.apply {
            setView(inflater?.inflate(R.layout.new_student_dialog, null))
            setPositiveButton(R.string.ok) { _, _ ->

                this@NewStudentFragment.dialog.cancel()
            }
            setNegativeButton(R.string.cancel) { _, _ ->
                this@NewStudentFragment.dialog.cancel()
            }

        }

        return builder?.create()!!
    }
}