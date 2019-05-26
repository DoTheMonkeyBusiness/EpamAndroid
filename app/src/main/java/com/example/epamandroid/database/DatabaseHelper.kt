package com.example.epamandroid.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_NAME_BREED_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_NAME_BREED_POPULARITY_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_NAME_COST_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_NAME_DESCRIPTION_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_NAME_DOG_ID_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_NAME_HEIGHT_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_NAME_IS_AFFECTIONATE_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_NAME_IS_CAN_LIVE_AT_HOME_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_NAME_LIFE_EXPECTANCY_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_NAME_PHOTO_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_NAME_WEIGHT_EXTRA_KEY
import com.example.epamandroid.models.DogEntity

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    IDatabaseOperation {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_DOG_BREEDS_TABLE_EXTRA_KEY)
        db?.execSQL(SQL_CREATE_LAST_MODIFICATION_TABLE_EXTRA_KEY)
        db?.execSQL(SQL_CREATE_CREATE_TRIGGER_ON_DOG_BREEDS_TABLE_TABLE_EXTRA_KEY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun insert(tableName: String, item: DogEntity?): Boolean {
        val writableDatabase = writableDatabase
        val newRowId: Long?
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME_DOG_ID_EXTRA_KEY, item?.id)
            put(COLUMN_NAME_BREED_EXTRA_KEY, item?.breed)
            put(COLUMN_NAME_WEIGHT_EXTRA_KEY, item?.weight)
            put(COLUMN_NAME_HEIGHT_EXTRA_KEY, item?.height)
            put(COLUMN_NAME_DESCRIPTION_EXTRA_KEY, item?.description)
            put(COLUMN_NAME_IS_CAN_LIVE_AT_HOME_EXTRA_KEY, item?.isCanLiveAtHome)
            put(COLUMN_NAME_IS_AFFECTIONATE_EXTRA_KEY, item?.isAffectionate)
            put(COLUMN_NAME_BREED_POPULARITY_EXTRA_KEY, item?.breedPopularity)
            put(COLUMN_NAME_COST_EXTRA_KEY, item?.cost)
            put(COLUMN_NAME_LIFE_EXPECTANCY_EXTRA_KEY, item?.lifeExpectancy)
            put(COLUMN_NAME_PHOTO_EXTRA_KEY, item?.photo)
        }

        writableDatabase.beginTransaction()

        try {
            newRowId = writableDatabase?.insertWithOnConflict(
                tableName,
                null, contentValues, SQLiteDatabase.CONFLICT_REPLACE
            )
            writableDatabase.setTransactionSuccessful()
        } finally {
            writableDatabase.endTransaction()
        }

        return newRowId != null && newRowId != -1L
    }

    override fun query(sqlString: String, vararg params: String): Cursor? {
        val readableDatabase = readableDatabase

        readableDatabase.beginTransaction()

        val cursor: Cursor

        try {
            cursor = readableDatabase.rawQuery(sqlString, params)
            readableDatabase.setTransactionSuccessful()
        } finally {
            readableDatabase.endTransaction()
        }

        return cursor
    }

    override fun deleteAll(tableName: String): Boolean {
        val writableDatabase = writableDatabase
        val delete: Int?


        writableDatabase.beginTransaction()

        try {
            delete = writableDatabase?.delete(tableName, null, null)
            writableDatabase.setTransactionSuccessful()
        } finally {
            writableDatabase.endTransaction()
        }

        return delete != null && delete != -1    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "DogBreedsDatabase.db"
    }
}