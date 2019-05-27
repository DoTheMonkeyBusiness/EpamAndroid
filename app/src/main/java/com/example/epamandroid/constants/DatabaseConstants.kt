package com.example.epamandroid.constants

import android.provider.BaseColumns
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_NAME_BREED_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.TABLE_NAME_DOG_BREEDS_TABLE_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.LastModificationTable.TABLE_NAME_LAST_MODIFICATION_TABLE_EXTRA_KEY
import com.example.epamandroid.constants.SymbolConstants.EQUAL_SIGN_EXTRA_KEY
import com.example.epamandroid.constants.SymbolConstants.QUESTION_SIGN_EXTRA_KEY
import com.example.epamandroid.constants.SymbolConstants.SPACE_EXTRA_KEY

object DatabaseConstants {
    object DogBreedsTable : BaseColumns {
        const val TABLE_NAME_DOG_BREEDS_TABLE_EXTRA_KEY: String = "DogBreedsTable"
        const val COLUMN_NAME_DOG_ID_EXTRA_KEY: String = "dogId"
        const val COLUMN_NAME_BREED_EXTRA_KEY: String = "breed"
        const val COLUMN_NAME_WEIGHT_EXTRA_KEY: String = "weight"
        const val COLUMN_NAME_HEIGHT_EXTRA_KEY: String = "height"
        const val COLUMN_NAME_DESCRIPTION_EXTRA_KEY: String = "description"
        const val COLUMN_NAME_IS_CAN_LIVE_AT_HOME_EXTRA_KEY: String = "isCanLiveAtHome"
        const val COLUMN_NAME_IS_AFFECTIONATE_EXTRA_KEY: String = "isAffectionate"
        const val COLUMN_NAME_BREED_POPULARITY_EXTRA_KEY: String = "breedPopularity"
        const val COLUMN_NAME_COST_EXTRA_KEY: String = "cost"
        const val COLUMN_NAME_LIFE_EXPECTANCY_EXTRA_KEY: String = "lifeExpectancy"
        const val COLUMN_NAME_PHOTO_EXTRA_KEY: String = "photo"
        const val COLUMN_INDEX_DOG_ID_EXTRA_KEY: Int = 1
        const val COLUMN_INDEX_BREED_EXTRA_KEY: Int = 2
        const val COLUMN_INDEX_WEIGHT_EXTRA_KEY: Int = 3
        const val COLUMN_INDEX_HEIGHT_EXTRA_KEY: Int = 4
        const val COLUMN_INDEX_DESCRIPTION_EXTRA_KEY: Int = 5
        const val COLUMN_INDEX_IS_CAN_LIVE_AT_HOME_EXTRA_KEY: Int = 6
        const val COLUMN_INDEX_IS_AFFECTIONATE_EXTRA_KEY: Int = 7
        const val COLUMN_INDEX_BREED_POPULARITY_EXTRA_KEY: Int = 8
        const val COLUMN_INDEX_COST_EXTRA_KEY: Int = 9
        const val COLUMN_INDEX_LIFE_EXPECTANCY_EXTRA_KEY: Int = 10
        const val COLUMN_INDEX_PHOTO_EXTRA_KEY: Int = 11
    }

    object LastModificationTable {
        const val TABLE_NAME_LAST_MODIFICATION_TABLE_EXTRA_KEY: String = "LastModificationTable"
        const val COLUMN_NAME_TABLE_NAME_EXTRA_KEY: String = "tableName"
        const val COLUMN_NAME_ACTION_EXTRA_KEY: String = "action"
        const val COLUMN_NAME_CHANGED_AT_EXTRA_KEY: String = "changedAt"
        const val COLUMN_INDEX_CHANGED_AT_EXTRA_KEY: Int = 2
        const val SECONDS_TO_DROP_TABLES_EXTRA_KEY: Long = 259200000
    }

    object DogBreedsTrigger {
        const val TRIGGER_NAME_DOG_BREEDS_TRIGGER_EXTRA_KEY: String = "DogBreedsTrigger"

    }

    object SqlStrings {
        const val SELECT_ALL_DOGS_SQL_STRING_EXTRA_KEY: String = "SELECT * FROM $TABLE_NAME_DOG_BREEDS_TABLE_EXTRA_KEY"
        const val SELECT_LAST_INSERT_TIME_SQL_STRING_EXTRA_KEY: String = "SELECT * FROM" +
                SPACE_EXTRA_KEY +
                TABLE_NAME_LAST_MODIFICATION_TABLE_EXTRA_KEY
        const val SELECT_DOG_BY_BREED_SQL_STRING_EXTRA_KEY: String = "SELECT * FROM" +
                SPACE_EXTRA_KEY +
                TABLE_NAME_DOG_BREEDS_TABLE_EXTRA_KEY +
                SPACE_EXTRA_KEY +
                "WHERE" +
                SPACE_EXTRA_KEY +
                COLUMN_NAME_BREED_EXTRA_KEY +
                SPACE_EXTRA_KEY +
                EQUAL_SIGN_EXTRA_KEY +
                SPACE_EXTRA_KEY +
                QUESTION_SIGN_EXTRA_KEY
    }
}