package com.example.epamandroid.backend

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.epamandroid.backend.entities.StudentModel
import com.example.epamandroid.util.ICallback
import java.util.*

class StudentsWebService : IWebService<StudentModel> {

    private val students = ArrayList<StudentModel>()
    private val random = Random()
    private val handler = Handler(Looper.getMainLooper())
    private var hwCount: Int = 0
    private var isStudent: Boolean = false

    init {
        for (i in 0..50) {
            this.hwCount = random.nextInt(6)
            when {
                hwCount > 1 -> {
                    this.isStudent = true
                }
                else -> {
                    this.isStudent = false
                }
            }
            val student = StudentModel(
                    i,
                    i.toString(),
                    hwCount,
                    isStudent)

            students.add(student)
        }
    }

    override fun getEntities(
            startRange: Int?,
            endRange: Int?,
            callback: ICallback<List<StudentModel>>
    ) {
        when {
            (endRange!! < students.size) -> {
                handler.postDelayed({ callback.onResult(students.subList(startRange!!, endRange)) }, 1000)
            }
            else -> {
                handler.postDelayed({ callback.onResult(students.subList(startRange!!, students.size)) }, 1000)
            }
        }

    }

    override fun addEntitle(
            name: String,
            hwCount: Int
    ) {

    }
}
