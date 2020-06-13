package com.example.epamandroid.database

import android.provider.BaseColumns
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_NAME_TYPE_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_NAME_TYPE_POPULARITY_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_NAME_COST_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_NAME_DESCRIPTION_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_NAME_RESTAURANT_ID_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_NAME_HEIGHT_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_NAME_IS_AFFECTIONATE_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_NAME_IS_CAN_LIVE_AT_HOME_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_NAME_LIFE_EXPECTANCY_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_NAME_PHOTO_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_NAME_WEIGHT_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.TABLE_NAME_RESTAURANT_TYPES_TABLE_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTrigger.TRIGGER_NAME_RESTAURANT_TYPES_TRIGGER_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.LastModificationTable.COLUMN_NAME_ACTION_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.LastModificationTable.COLUMN_NAME_CHANGED_AT_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.LastModificationTable.COLUMN_NAME_TABLE_NAME_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.LastModificationTable.TABLE_NAME_LAST_MODIFICATION_TABLE_EXTRA_KEY
import com.example.epamandroid.constants.SymbolConstants.CLOSING_BRACKET_EXTRA_KEY
import com.example.epamandroid.constants.SymbolConstants.COMMA_EXTRA_KEY
import com.example.epamandroid.constants.SymbolConstants.OPENING_BRACKET_EXTRA_KEY
import com.example.epamandroid.constants.SymbolConstants.SEMICOLON_EXTRA_KEY
import com.example.epamandroid.constants.SymbolConstants.SINGLE_QUOTE_EXTRA_KEY
import com.example.epamandroid.constants.SymbolConstants.SPACE_EXTRA_KEY

internal const val SQL_CREATE_RESTAURANT_TYPES_TABLE_EXTRA_KEY =
    "CREATE TABLE IF NOT EXISTS $TABLE_NAME_RESTAURANT_TYPES_TABLE_EXTRA_KEY" +
            SPACE_EXTRA_KEY +
            OPENING_BRACKET_EXTRA_KEY +
            "${BaseColumns._ID} INTEGER PRIMARY KEY" +
            COMMA_EXTRA_KEY +
            "$COLUMN_NAME_RESTAURANT_ID_EXTRA_KEY INTEGER" +
            COMMA_EXTRA_KEY +
            "$COLUMN_NAME_TYPE_EXTRA_KEY TEXT" +
            COMMA_EXTRA_KEY +
            "$COLUMN_NAME_WEIGHT_EXTRA_KEY TEXT" +
            COMMA_EXTRA_KEY +
            "$COLUMN_NAME_HEIGHT_EXTRA_KEY TEXT" +
            COMMA_EXTRA_KEY +
            "$COLUMN_NAME_DESCRIPTION_EXTRA_KEY TEXT" +
            COMMA_EXTRA_KEY +
            "$COLUMN_NAME_IS_CAN_LIVE_AT_HOME_EXTRA_KEY INTEGER" +
            COMMA_EXTRA_KEY +
            "$COLUMN_NAME_IS_AFFECTIONATE_EXTRA_KEY INTEGER" +
            COMMA_EXTRA_KEY +
            "$COLUMN_NAME_TYPE_POPULARITY_EXTRA_KEY REAL" +
            COMMA_EXTRA_KEY +
            "$COLUMN_NAME_COST_EXTRA_KEY INTEGER" +
            COMMA_EXTRA_KEY +
            "$COLUMN_NAME_LIFE_EXPECTANCY_EXTRA_KEY TEXT" +
            COMMA_EXTRA_KEY +
            "$COLUMN_NAME_PHOTO_EXTRA_KEY TEXT" +
            CLOSING_BRACKET_EXTRA_KEY

internal const val SQL_CREATE_LAST_MODIFICATION_TABLE_EXTRA_KEY =
    "CREATE TABLE IF NOT EXISTS $TABLE_NAME_LAST_MODIFICATION_TABLE_EXTRA_KEY" +
            SPACE_EXTRA_KEY +
            OPENING_BRACKET_EXTRA_KEY +
            "$COLUMN_NAME_TABLE_NAME_EXTRA_KEY TEXT NOT NULL PRIMARY KEY ON CONFLICT REPLACE" +
            COMMA_EXTRA_KEY +
            "$COLUMN_NAME_ACTION_EXTRA_KEY TEXT NOT NULL" +
            COMMA_EXTRA_KEY +
            "$COLUMN_NAME_CHANGED_AT_EXTRA_KEY TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            CLOSING_BRACKET_EXTRA_KEY

internal const val SQL_CREATE_CREATE_TRIGGER_ON_RESTAURANT_TYPES_TABLE_TABLE_EXTRA_KEY =
    "CREATE TRIGGER IF NOT EXISTS $TRIGGER_NAME_RESTAURANT_TYPES_TRIGGER_EXTRA_KEY AFTER INSERT ON" +
            SPACE_EXTRA_KEY +
            TABLE_NAME_RESTAURANT_TYPES_TABLE_EXTRA_KEY +
            SPACE_EXTRA_KEY +
            "BEGIN" +
            SPACE_EXTRA_KEY +
            "INSERT INTO" +
            SPACE_EXTRA_KEY +
            TABLE_NAME_LAST_MODIFICATION_TABLE_EXTRA_KEY +
            SPACE_EXTRA_KEY +
            OPENING_BRACKET_EXTRA_KEY +
            COLUMN_NAME_TABLE_NAME_EXTRA_KEY +
            COMMA_EXTRA_KEY +
            SPACE_EXTRA_KEY +
            COLUMN_NAME_ACTION_EXTRA_KEY +
            CLOSING_BRACKET_EXTRA_KEY +
            SPACE_EXTRA_KEY +
            "VALUES" +
            SPACE_EXTRA_KEY +
            OPENING_BRACKET_EXTRA_KEY +
            SINGLE_QUOTE_EXTRA_KEY +
            TABLE_NAME_LAST_MODIFICATION_TABLE_EXTRA_KEY +
            SINGLE_QUOTE_EXTRA_KEY +
            COMMA_EXTRA_KEY +
            SINGLE_QUOTE_EXTRA_KEY +
            "INSERT" +
            SINGLE_QUOTE_EXTRA_KEY +
            CLOSING_BRACKET_EXTRA_KEY +
            SEMICOLON_EXTRA_KEY +
            SPACE_EXTRA_KEY +
            "END" +
            SEMICOLON_EXTRA_KEY

internal const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${RestaurantTypesTable.TABLE_NAME_RESTAURANT_TYPES_TABLE_EXTRA_KEY}"