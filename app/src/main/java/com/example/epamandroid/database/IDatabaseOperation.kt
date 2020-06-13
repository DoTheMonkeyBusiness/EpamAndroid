package com.example.epamandroid.database

import android.database.Cursor
import com.example.epamandroid.models.RestaurantEntity

interface IDatabaseOperation {
    fun query(sqlString: String, vararg params: String): Cursor?

    fun insert(tableName: String, item: RestaurantEntity?): Boolean

    fun deleteAll(tableName: String): Boolean
}