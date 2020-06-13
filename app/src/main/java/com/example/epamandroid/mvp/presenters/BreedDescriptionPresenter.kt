package com.example.epamandroid.mvp.presenters

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
import com.example.epamandroid.constants.DatabaseConstants.SqlStrings.SELECT_RESTAURANT_BY_TYPE_SQL_STRING_EXTRA_KEY
import com.example.epamandroid.database.DatabaseHelper
import com.example.epamandroid.gsonmodels.GsonRestaurantEntity
import com.example.epamandroid.models.RestaurantEntity
import com.example.epamandroid.mvp.contracts.ITypeDescriptionContract
import com.example.epamandroid.mvp.repository.Repository

class TypeDescriptionPresenter(private val view: ITypeDescriptionContract.View)
    : ITypeDescriptionContract.Presenter {
    private val repository: ITypeDescriptionContract.Model = Repository
    private val handler = Handler(Looper.getMainLooper())
    private val databaseHelper: DatabaseHelper = DatabaseHelper(view.getContext())

    override fun onCreate() = Unit

    override fun loadRestaurantByType(type:String){
        Thread {
            var restaurantEntity: RestaurantEntity? = null

            databaseHelper.query(SELECT_RESTAURANT_BY_TYPE_SQL_STRING_EXTRA_KEY, type)?.use {
                while (it.moveToNext()){
                    restaurantEntity = RestaurantEntity(
                        it.getInt(COLUMN_INDEX_RESTAURANT_ID_EXTRA_KEY),
                        it.getString(COLUMN_INDEX_TYPE_EXTRA_KEY),
                        it.getString(COLUMN_INDEX_WEIGHT_EXTRA_KEY),
                        it.getString(COLUMN_INDEX_HEIGHT_EXTRA_KEY),
                        it.getString(COLUMN_INDEX_DESCRIPTION_EXTRA_KEY),
                        it.getInt(COLUMN_INDEX_IS_CAN_LIVE_AT_HOME_EXTRA_KEY) == 1,
                        it.getInt(COLUMN_INDEX_IS_AFFECTIONATE_EXTRA_KEY) == 1,
                        it.getFloat(COLUMN_INDEX_TYPE_POPULARITY_EXTRA_KEY),
                        it.getInt(COLUMN_INDEX_COST_EXTRA_KEY),
                        it.getString(COLUMN_INDEX_LIFE_EXPECTANCY_EXTRA_KEY),
                        it.getString(COLUMN_INDEX_PHOTO_EXTRA_KEY))
                }
            }

            if (restaurantEntity == null) {
                val gsonRestaurantEntity: GsonRestaurantEntity? = repository.getEntity(type)

                if (gsonRestaurantEntity != null) {
                    restaurantEntity = RestaurantEntity(
                        gsonRestaurantEntity.id,
                        gsonRestaurantEntity.type,
                        gsonRestaurantEntity.weight,
                        gsonRestaurantEntity.height,
                        gsonRestaurantEntity.description,
                        gsonRestaurantEntity.isCanLiveAtHome,
                        gsonRestaurantEntity.isAffectionate,
                        gsonRestaurantEntity.typePopularity,
                        gsonRestaurantEntity.cost,
                        gsonRestaurantEntity.lifeExpectancy,
                        gsonRestaurantEntity.photo
                    )
                }
            }

            handler.post {
                Runnable {
                    view.updateTypeDescription(restaurantEntity)
                }.run()
            }
        }.start()
    }

    override fun onDestroy() = Unit
}