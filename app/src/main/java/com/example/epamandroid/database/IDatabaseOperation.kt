package com.example.epamandroid.database

import android.database.Cursor
import com.example.epamandroid.models.DogEntity

interface IDatabaseOperation {
    fun query(sqlString: String, vararg params: String): Cursor?

    fun insert(tableName: String, item: DogEntity?): Boolean

    fun deleteAll(tableName: String): Boolean
}