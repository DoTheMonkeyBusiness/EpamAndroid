package com.example.epamandroid. mvp.views.adapters

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epamandroid.R
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.views.annotationclasses.ViewType
import com.example.epamandroid.mvp.views.compoundviews.DogView
import com.example.imageloader.IMichelangelo
import com.example.imageloader.Michelangelo

class HomeRecyclerViewAdapter(context: Context?) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    private val michelangelo: IMichelangelo = Michelangelo.getInstance(context)
    private val dogsList:  ArrayList<DogEntity> = arrayListOf()

    private var dogEntity: DogEntity? = null

    var onItemClick: ((DogEntity) -> Unit)? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.dog_view_item, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (getItemViewType(position) == ViewType.DOG) {
            val holder = (viewHolder.itemView as DogView)
            dogEntity = dogsList[position]

            holder
                    .setDogBreed(dogEntity?.breed)
                    .isCanLiveAtHome(dogEntity?.isCanLiveAtHome)
            michelangelo.load(holder.getDogIcon(), dogEntity?.photo)
        }
    }

    override fun getItemCount(): Int {
            return dogsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < dogsList.size) {
            ViewType.DOG
        } else {
            ViewType.LOADING
        }
    }

    fun addItems(result: List<DogEntity>?) {
        val oldItems: ArrayList<DogEntity> = arrayListOf()

        oldItems.addAll(dogsList)
        result?.let { dogsList.addAll(it) }
        notifyChanges(oldItems, dogsList)
    }

    fun updateDogsList(list: ArrayList<DogEntity>?) {
        if (list !== null) {
            val oldItems: ArrayList<DogEntity> = arrayListOf()

            oldItems.addAll(dogsList)
            dogsList.clear()
            dogsList.addAll(list)
            notifyChanges(oldItems, dogsList)
        }
    }

    fun getDogList(): ArrayList<DogEntity>? {
        return dogsList
    }

    fun getMaxId(): Int {

        return when {
            dogsList.isNullOrEmpty() -> {
                0
            }
            else -> {
                var maxElement = 0
                dogsList.forEach {
                    if (it.id != null && it.id > maxElement){
                        maxElement = it.id
                    }
                }

                maxElement
            }
        }
    }

    fun getEntityById(id: Int): DogEntity? = dogsList[id]

    fun isEmptyDogsList(): Boolean {
        return dogsList.isEmpty()
    }

    private fun notifyChanges(oldList: ArrayList<DogEntity>, newList: ArrayList<DogEntity>) {

        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldDogEntity: DogEntity? = oldList[oldItemPosition]
                val newDogEntity: DogEntity? = newList[newItemPosition]

                return oldDogEntity == newDogEntity
            }

            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size
        })

        diff.dispatchUpdatesTo(this@HomeRecyclerViewAdapter)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                if (itemViewType == ViewType.DOG) {
                    dogsList[adapterPosition].let { it1 -> it1.let { it2 -> onItemClick?.invoke(it2) } }
                }
            }
        }
    }
}