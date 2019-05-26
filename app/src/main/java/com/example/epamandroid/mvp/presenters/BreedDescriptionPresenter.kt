package com.example.epamandroid.mvp.presenters

import android.os.Handler
import android.os.Looper
import com.example.epamandroid.constants.DatabaseConstants
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
import com.example.epamandroid.constants.DatabaseConstants.SqlStrings.SELECT_DOG_BY_BREED_SQL_STRING_EXTRA_KEY
import com.example.epamandroid.database.DatabaseHelper
import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.contracts.IBreedDescriptionContract
import com.example.epamandroid.mvp.repository.BreedDescriptionModel

class BreedDescriptionPresenter(private val view: IBreedDescriptionContract.View) : IBreedDescriptionContract.Presenter {
    private val handler = Handler(Looper.getMainLooper())
    private val databaseHelper: DatabaseHelper = DatabaseHelper(view.getContext())

    override fun onCreate() = Unit

    override fun loadDogByBreed(breed:String){
        Thread {
            var dogEntity: DogEntity? = null

            databaseHelper.query(SELECT_DOG_BY_BREED_SQL_STRING_EXTRA_KEY, breed)?.use {
                while (it.moveToNext()){
                    dogEntity = DogEntity(
                        it.getInt(COLUMN_INDEX_DOG_ID_EXTRA_KEY),
                        it.getString(COLUMN_INDEX_BREED_EXTRA_KEY),
                        it.getString(COLUMN_INDEX_WEIGHT_EXTRA_KEY),
                        it.getString(COLUMN_INDEX_HEIGHT_EXTRA_KEY),
                        it.getString(COLUMN_INDEX_DESCRIPTION_EXTRA_KEY),
                        it.getInt(COLUMN_INDEX_IS_CAN_LIVE_AT_HOME_EXTRA_KEY) == 1,
                        it.getInt(COLUMN_INDEX_IS_AFFECTIONATE_EXTRA_KEY) == 1,
                        it.getFloat(COLUMN_INDEX_BREED_POPULARITY_EXTRA_KEY),
                        it.getInt(COLUMN_INDEX_COST_EXTRA_KEY),
                        it.getString(COLUMN_INDEX_LIFE_EXPECTANCY_EXTRA_KEY),
                        it.getString(COLUMN_INDEX_PHOTO_EXTRA_KEY))
                }
            }

            if (dogEntity == null) {
                val gsonDogEntity: GsonDogEntity? = BreedDescriptionModel.getEntity(breed)

                if (gsonDogEntity != null) {
                    dogEntity = DogEntity(
                        gsonDogEntity.id,
                        gsonDogEntity.breed,
                        gsonDogEntity.weight,
                        gsonDogEntity.height,
                        gsonDogEntity.description,
                        gsonDogEntity.isCanLiveAtHome,
                        gsonDogEntity.isAffectionate,
                        gsonDogEntity.breedPopularity,
                        gsonDogEntity.cost,
                        gsonDogEntity.lifeExpectancy,
                        gsonDogEntity.photo
                    )
                }
            }

            handler.post {
                Runnable {
                    view.updateBreedDescription(dogEntity)
                }.run()
            }
        }.start()
    }

    override fun onDestroy() = Unit
}