package com.example.epamandroid.backend

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.Constants.MINIMUM_HOMEWORK_COUNT_EXTRA_KEY
import com.example.epamandroid.backend.entities.StudentModel
import com.example.epamandroid.util.ICallback
import java.util.*

class StudentsWebService : IWebService<StudentModel> {

    private val students = ArrayList<StudentModel>()
    private val random = Random()
    private val handler = Handler(Looper.getMainLooper())

    init {
        for (i in 0..40) {
            val hwCount: Int = random.nextInt(6)
            val isStudent = when {
                hwCount > MINIMUM_HOMEWORK_COUNT_EXTRA_KEY -> {
                    true
                }
                else -> {
                    false
                }
            }
            val student = StudentModel(
                i,
                i.toString(),
                hwCount,
                isStudent
            )

            students.add(student)
        }
    }

    override fun getEntities(
        startRange: Int,
        endRange: Int,
        callback: ICallback<List<StudentModel>>
    ) {
        when {
            (endRange < students.size) -> {
                handler.postDelayed({ callback.onResult(students.subList(startRange, endRange)) }, 1000)
            }
            else -> {
                handler.postDelayed({ callback.onResult(students.subList(startRange, students.size)) }, 1000)
            }
        }

    }

    override fun addEntitle(
        name: String,
        hwCount: String
    ) {
        val isStudent: Boolean = when {
            hwCount.toInt() > MINIMUM_HOMEWORK_COUNT_EXTRA_KEY -> {
                true
            }
            else -> {
                false
            }
        }
        val student = StudentModel(
            students.size,
            name,
            hwCount.toInt(),
            isStudent
        )

        students.add(student)

    }

    override fun removeEntitle(id: Int) {
        students.removeAt(id)
    }

    override fun getEntitiesSize(): Int {
        return students.size
    }

    override fun editStudentInfo(
        id: Int,
        name: String?,
        hwCount: String?
    ) {
        students[id].let {
            if (name != null) {
                it.name = name
            }
            if (hwCount != null) {
                it.hwCount = hwCount.toInt()
                when {
                    (hwCount.toInt() > MINIMUM_HOMEWORK_COUNT_EXTRA_KEY) -> it.isStudent = true
                    else -> it.isStudent = false
                }
            }
        }

    }
}
