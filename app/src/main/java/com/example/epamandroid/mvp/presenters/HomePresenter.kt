package com.example.epamandroid.mvp.presenters

import android.content.Context
import android.net.ConnectivityManager
import android.nfc.tech.MifareUltralight
import android.os.Handler
import android.os.Looper
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_INDEX_TYPE_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_INDEX_TYPE_POPULARITY_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_INDEX_COST_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_INDEX_DESCRIPTION_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_INDEX_RESTAURANT_ID_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_INDEX_HEIGHT_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_INDEX_IS_AFFECTIONATE_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_INDEX_IS_CAN_LIVE_AT_HOME_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_INDEX_LIFE_EXPECTANCY_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_INDEX_PHOTO_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.COLUMN_INDEX_WEIGHT_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.RestaurantTypesTable.TABLE_NAME_RESTAURANT_TYPES_TABLE_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.LastModificationTable.COLUMN_INDEX_CHANGED_AT_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.LastModificationTable.SECONDS_TO_DROP_TABLES_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.SqlStrings.SELECT_ALL_RESTAURANTS_SQL_STRING_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.SqlStrings.SELECT_LAST_INSERT_TIME_SQL_STRING_EXTRA_KEY
import com.example.epamandroid.constants.DateConstants.DATE_FORMAT_EXTRA_KEY
import com.example.epamandroid.database.DatabaseHelper
import com.example.epamandroid.gsonmodels.GsonRestaurantEntity
import com.example.epamandroid.models.RestaurantEntity
import com.example.epamandroid.mvp.contracts.IHomeContract
import com.example.epamandroid.mvp.repository.Repository
import java.text.SimpleDateFormat
import java.util.*


class HomePresenter(private val view: IHomeContract.View) : IHomeContract.Presenter {

    companion object {
        private const val TAG: String = "HomePresenter"
        private const val START_OF_RANGE_KEY: Int = 0
        private const val END_OF_RANGE_KEY: Int = MifareUltralight.PAGE_SIZE * 3
    }

    private val repository: IHomeContract.Model = Repository
    private val databaseHelper: DatabaseHelper = DatabaseHelper(view.getContext())
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        if (view.isEmptyRecyclerView()) {
            Thread {
//                checkDatabaseRelevance()

                getItemsFromDatabase()
            }.start()
        }
    }

    override fun getMoreItems(
        startPosition: Int,
        endPosition: Int
    ) {
        Thread {
            getItemsFromModel(startPosition, endPosition)
        }.start()
    }

    private fun getItemsFromModel(
        startPosition: Int,
        endPosition: Int
    ) {
        val restaurantList: ArrayList<RestaurantEntity>? = arrayListOf()
        val gsonRestaurantMap: HashMap<Int, GsonRestaurantEntity>? = repository
            .getEntities(startPosition, endPosition)

        gsonRestaurantMap?.forEach {
            restaurantList?.add(
                RestaurantEntity(
                    it.value.id,
                    it.value.type,
                    it.value.weight,
                    it.value.height,
                    it.value.description,
                    it.value.isCanLiveAtHome,
                    it.value.isAffectionate,
                    it.value.typePopularity,
                    it.value.cost,
                    it.value.lifeExpectancy,
                    it.value.photo
                )
            )
            databaseHelper.insert(
                TABLE_NAME_RESTAURANT_TYPES_TABLE_EXTRA_KEY,
                RestaurantEntity(
                    it.value.id,
                    it.value.type,
                    it.value.weight,
                    it.value.height,
                    it.value.description,
                    it.value.isCanLiveAtHome,
                    it.value.isAffectionate,
                    it.value.typePopularity,
                    it.value.cost,
                    it.value.lifeExpectancy,
                    it.value.photo
                )
            )
        }

        giveElementsToView(restaurantList?.sortedBy { it.id }, endPosition)
    }

    private fun isFoolList(restaurantList: List<RestaurantEntity>?, endPosition: Int): Boolean {

        return !(restaurantList != null &&
                restaurantList.size == endPosition)

    }

    private fun giveElementsToView(restaurantList: List<RestaurantEntity>?, endPosition: Int) {
        handler.post {
            Runnable {
                view.addElements(restaurantList, isFoolList(restaurantList, endPosition))
            }.run()
        }
    }

    private fun getItemsFromDatabase() {
//        val restaurantList: ArrayList<RestaurantEntity>? = arrayListOf()
//
//        databaseHelper.query(SELECT_ALL_RESTAURANTS_SQL_STRING_EXTRA_KEY)?.use {
//            while (it.moveToNext()) {
//                restaurantList?.add(
//                    RestaurantEntity(
//                        it.getInt(COLUMN_INDEX_RESTAURANT_ID_EXTRA_KEY),
//                        it.getString(COLUMN_INDEX_TYPE_EXTRA_KEY),
//                        it.getString(COLUMN_INDEX_WEIGHT_EXTRA_KEY),
//                        it.getString(COLUMN_INDEX_HEIGHT_EXTRA_KEY),
//                        it.getString(COLUMN_INDEX_DESCRIPTION_EXTRA_KEY),
//                        it.getInt(COLUMN_INDEX_IS_CAN_LIVE_AT_HOME_EXTRA_KEY) == 1,
//                        it.getInt(COLUMN_INDEX_IS_AFFECTIONATE_EXTRA_KEY) == 1,
//                        it.getFloat(COLUMN_INDEX_TYPE_POPULARITY_EXTRA_KEY),
//                        it.getInt(COLUMN_INDEX_COST_EXTRA_KEY),
//                        it.getString(COLUMN_INDEX_LIFE_EXPECTANCY_EXTRA_KEY),
//                        it.getString(COLUMN_INDEX_PHOTO_EXTRA_KEY)
//                    )
//                )
//            }
//        }
//
//        if (restaurantList != null && restaurantList.isNotEmpty()) {
//            giveElementsToView(restaurantList.sortedBy { it.id }, 0)
//        } else {
            getItemsFromModel(START_OF_RANGE_KEY, END_OF_RANGE_KEY)
//        }
    }

    private fun checkDatabaseRelevance() {
        var lastInsertTimeString: String? = null
        val format = SimpleDateFormat(DATE_FORMAT_EXTRA_KEY, Locale.ROOT)

        databaseHelper.query(SELECT_LAST_INSERT_TIME_SQL_STRING_EXTRA_KEY)?.use { cursor ->
            while (cursor.moveToNext()) {
                lastInsertTimeString = cursor.getString(COLUMN_INDEX_CHANGED_AT_EXTRA_KEY)
            }
        }

        if (lastInsertTimeString != null) {
            val lastInsertTimeLong = format.parse(lastInsertTimeString).time
            val timeNowLong = Calendar.getInstance().time.time

            if (timeNowLong - lastInsertTimeLong > SECONDS_TO_DROP_TABLES_EXTRA_KEY && isNetworkConnected()) {
                databaseHelper.deleteAll(TABLE_NAME_RESTAURANT_TYPES_TABLE_EXTRA_KEY)
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = view
            .getContext()
            ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        return connectivityManager?.activeNetworkInfo != null
    }

    override fun onDestroy() = Unit
}