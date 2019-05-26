package com.example.epamandroid.mvp.presenters

import android.nfc.tech.MifareUltralight
import android.os.Handler
import android.os.Looper
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_INDEX_BREED_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_INDEX_BREED_POPULARITY_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_INDEX_COST_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_INDEX_DESCRIPTION_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_INDEX_DOG_ID_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_INDEX_HEIGHT_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_INDEX_IS_AFFECTIONATE_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_INDEX_IS_CAN_LIVE_AT_HOME_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_INDEX_LIFE_EXPECTANCY_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_INDEX_PHOTO_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.COLUMN_INDEX_WEIGHT_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.DogBreedsTable.TABLE_NAME_DOG_BREEDS_TABLE_EXTRA_KEY
import com.example.epamandroid.constants.DatabaseConstants.SqlStrings.SELECT_ALL_DOGS_SQL_STRING_EXTRA_KEY
import com.example.epamandroid.database.DatabaseHelper
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.contracts.IHomeContract
import com.example.epamandroid.mvp.repository.HomeModel

class HomePresenter(private val view: IHomeContract.View) : IHomeContract.Presenter {

    companion object {
        private const val TAG: String = "HomePresenter"
        private const val START_OF_RANGE_KEY: Int = 0
        private const val END_OF_RANGE_KEY: Int = MifareUltralight.PAGE_SIZE * 3
    }

    private val databaseHelper: DatabaseHelper = DatabaseHelper(view.getContext())
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        if (view.isEmptyRecyclerView()) {
            getItemsFromDatabase()
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
        Thread {
            val dogList: ArrayList<DogEntity>? = arrayListOf()
            val gsonDogMap: HashMap<Int, GsonDogEntity>? = HomeModel
                .getEntities(startPosition, endPosition)

            gsonDogMap?.forEach {
                dogList?.add(
                    DogEntity(
                        it.value.id,
                        it.value.breed,
                        it.value.weight,
                        it.value.height,
                        it.value.description,
                        it.value.isCanLiveAtHome,
                        it.value.isAffectionate,
                        it.value.breedPopularity,
                        it.value.cost,
                        it.value.lifeExpectancy,
                        it.value.photo
                    )
                )
                databaseHelper.insert(
                    TABLE_NAME_DOG_BREEDS_TABLE_EXTRA_KEY,
                    DogEntity(
                        it.value.id,
                        it.value.breed,
                        it.value.weight,
                        it.value.height,
                        it.value.description,
                        it.value.isCanLiveAtHome,
                        it.value.isAffectionate,
                        it.value.breedPopularity,
                        it.value.cost,
                        it.value.lifeExpectancy,
                        it.value.photo
                    )
                )
            }

            giveElementsToView(dogList?.sortedBy { it.id }, endPosition)
        }.start()
    }

    private fun isFoolList(dogList: List<DogEntity>?, endPosition: Int): Boolean {

        return !(dogList != null &&
                dogList.size == endPosition)

    }

    private fun giveElementsToView(dogList: List<DogEntity>?, endPosition: Int) {
        handler.post {
            Runnable {
                view.addElements(dogList, isFoolList(dogList, endPosition))
            }.run()
        }
    }

    private fun getItemsFromDatabase() {
        Thread {
            val dogList: ArrayList<DogEntity>? = arrayListOf()

            databaseHelper.query(SELECT_ALL_DOGS_SQL_STRING_EXTRA_KEY)?.use { cursor ->
                cursor.moveToFirst()
                while (cursor.moveToNext()) {
                    dogList?.add(
                        DogEntity(
                            cursor.getInt(COLUMN_INDEX_DOG_ID_EXTRA_KEY),
                            cursor.getString(COLUMN_INDEX_BREED_EXTRA_KEY),
                            cursor.getString(COLUMN_INDEX_WEIGHT_EXTRA_KEY),
                            cursor.getString(COLUMN_INDEX_HEIGHT_EXTRA_KEY),
                            cursor.getString(COLUMN_INDEX_DESCRIPTION_EXTRA_KEY),
                            cursor.getInt(COLUMN_INDEX_IS_CAN_LIVE_AT_HOME_EXTRA_KEY) == 1,
                            cursor.getInt(COLUMN_INDEX_IS_AFFECTIONATE_EXTRA_KEY) == 1,
                            cursor.getFloat(COLUMN_INDEX_BREED_POPULARITY_EXTRA_KEY),
                            cursor.getInt(COLUMN_INDEX_COST_EXTRA_KEY),
                            cursor.getString(COLUMN_INDEX_LIFE_EXPECTANCY_EXTRA_KEY),
                            cursor.getString(COLUMN_INDEX_PHOTO_EXTRA_KEY)
                        )
                    )
                }
            }

            if (dogList != null && dogList.isNotEmpty()) {
                giveElementsToView(dogList.sortedBy { it.id }, 0)
            } else {
                getItemsFromModel(START_OF_RANGE_KEY, END_OF_RANGE_KEY)
            }
        }.start()
    }

    override fun onDestroy() = Unit
}