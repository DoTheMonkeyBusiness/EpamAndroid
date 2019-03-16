package com.example.epamandroid.backend

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.backend.entities.StudentModel
import com.example.epamandroid.util.ICallback
import java.util.*

class StudentsWebService : IWebService<StudentModel> {

    private val students = ArrayList<StudentModel>()
    private val random = Random()
    private val handler = Handler(Looper.getMainLooper())

    init {
        for (i in 0..999) {
            val student = StudentModel(i.toLong(), i.toString(), random.nextInt(5), random.nextBoolean())
            students.add(student)
        }
    }


    override fun getEntities(
        startRange: Int?,
        endRange: Int?,
        callback: ICallback<List<StudentModel>>
    ) {
        handler.postDelayed({callback.onResult(students.subList(startRange!!, endRange!!)) }, 1000)
    }

    override fun removeEntity(id: Long?) {

    }
}
