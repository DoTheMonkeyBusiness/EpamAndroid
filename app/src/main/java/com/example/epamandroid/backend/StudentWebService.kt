package com.example.epamandroid.backend

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.Constants.MINIMUM_HOMEWORK_COUNT_EXTRA_KEY
import com.example.epamandroid.backend.entities.StudentModel
import com.example.epamandroid.util.ICallback
import com.example.epamandroid.util.IShowLastViewAsLoadingCallback
import java.util.*
import kotlin.collections.ArrayList

class StudentsWebService private constructor(): IWebService<StudentModel> {

    companion object {
        private var instance: StudentsWebService? = null
        fun getInstance(): StudentsWebService? {
            if(instance == null){
                instance = StudentsWebService()
            }

            return instance
        }
    }

    private val students: ArrayList<StudentModel>? = ArrayList()
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

            students?.add(student)
        }
    }

    override fun getEntities(
        startRange: Int,
        endRange: Int,
        callback: ICallback<List<StudentModel>>,
        showLastViewAsLoading: IShowLastViewAsLoadingCallback

    ) {
        when {
            (endRange < students?.size?.minus(1) ?: 0) -> {
                showLastViewAsLoading.onShowLastViewAsLoadingCallback(true)
                handler.postDelayed({ students?.subList(startRange, endRange)?.let { callback.onResult(it) } }, 1000)
            }
            else -> {
                showLastViewAsLoading.onShowLastViewAsLoadingCallback(false)
                handler.postDelayed({ students?.subList(startRange, students.size)?.let { callback.onResult(it) } }, 1000)
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
        val student = students?.size?.let {
            StudentModel(
                it,
                name,
                hwCount.toInt(),
                isStudent
            )
        }

        student?.let { students?.add(it) }

    }

    override fun removeEntitle(id: Int?) {
        id?.let { students?.removeAt(it) }
    }

    override fun getEntitiesSize(): Int? {
        return students?.size
    }

    override fun editStudentInfo(
        id: Int,
        name: String?,
        hwCount: String?
    ) {
        students?.get(id)?.let {
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
