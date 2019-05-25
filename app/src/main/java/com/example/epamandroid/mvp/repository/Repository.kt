package com.example.epamandroid.mvp.repository

import com.example.epamandroid.gsonmodels.GsonDogEntity
import com.example.epamandroid.gsonmodels.GsonLostDogEntity
import com.example.epamandroid.mvp.contracts.*
import java.io.File
import java.util.*

object Repository :
        IAddLostDogContract.Model<GsonLostDogEntity>,
        IChooseLostBreedContract.Model,
        IHomeContract.Model<GsonDogEntity>,
        IMainActivityContract.Model,
        IMapContract.Model<GsonLostDogEntity> {

    override fun putLostBreed(gsonLostDogEntity: GsonLostDogEntity): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun uploadImage(imageFile: File, id: UUID): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBreeds(): MutableList<String?>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEntities(startPosition: Int, endPosition: Int): HashMap<Int, GsonDogEntity>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEntitiesNearby(latitude: Double, radius: Float): HashMap<String, GsonLostDogEntity>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}