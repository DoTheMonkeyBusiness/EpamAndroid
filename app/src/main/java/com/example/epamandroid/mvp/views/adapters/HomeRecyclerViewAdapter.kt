package com.example.epamandroid.mvp.views.adapters

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.epamandroid.R
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.views.annotationclasses.ViewType
import com.example.epamandroid.mvp.views.annotationclasses.ViewType.Companion.DOG
import com.example.epamandroid.mvp.views.annotationclasses.ViewType.Companion.LOADING
import com.example.epamandroid.mvp.views.compoundviews.DogView
import com.example.imageloader.IMichelangelo
import com.example.imageloader.Michelangelo
import java.util.*
import kotlin.collections.ArrayList

class HomeRecyclerViewAdapter(context: Context?) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    private val michelangelo: IMichelangelo = Michelangelo.getInstance(context)
    private val dogsList = ArrayList<DogEntity?>()

    private var isShowLastViewAsLoading = false

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

    fun setShowLastViewAsLoading(isShow: Boolean) {
        if (isShow != isShowLastViewAsLoading) {
            val oldItems: ArrayList<DogEntity?> = dogsList.clone() as ArrayList<DogEntity?>
            isShowLastViewAsLoading = isShow
//            notifyDataSetChanged()
            notifyChanges(oldItems, dogsList)
        }
    }

    fun addItems(result: List<DogEntity>?) {
        val oldItems: ArrayList<DogEntity?> = dogsList.clone() as ArrayList<DogEntity?>

        result?.let { dogsList.addAll(it) }
        notifyChanges(oldItems, dogsList)
//        notifyDataSetChanged()
    }

    fun updateDogsList(list: ArrayList<DogEntity>?) {
        if (list !== null) {
            val oldItems: ArrayList<DogEntity?> = dogsList.clone() as ArrayList<DogEntity?>
            dogsList.clear()
            dogsList.addAll(list)
//            notifyDataSetChanged()
            notifyChanges(oldItems, dogsList)
        }
    }

    fun getDogList(): ArrayList<DogEntity?> {
        return dogsList
    }

    private fun deleteByIndex(i: Int) {
        dogsList.removeAt(i)
        notifyItemRemoved(i)
        notifyItemRangeChanged(i, dogsList.size - 1)
    }

//    fun getMaxId(): Int? {
//
//        dogsList.maxBy { it?.id }
//
//        return when {
//            dogsList.isEmpty() -> {
//                0
//            }
//            (dogsList.size < dogsList.maxBy { it?.id }?.id ?: dogsList.size) -> {
//                dogsList.size - 1
//            }
//            else -> {
//                dogsList.maxBy { it?.id }?.id
//            }
//
//        }
//    }

    fun getSize(): Int? = dogsList.size

    fun getEntityById(id: Int): DogEntity? = dogsList[id]

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(dogsList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(dogsList, i, i - 1)
            }
        }

        notifyItemMoved(fromPosition, toPosition)
    }

    fun onItemDismiss(adapterPosition: Int) {
        if (getItemViewType(adapterPosition) == ViewType.DOG) {
            deleteByIndex(adapterPosition)
        }
    }

    fun isEmptyDogsList(): Boolean {
        return dogsList.isEmpty()
    }

    private fun notifyChanges(oldList: ArrayList<DogEntity?>, newList: ArrayList<DogEntity?>) {

        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//                return getItemViewType(oldItemPosition) == getItemViewType(newItemPosition)
                return oldList[oldItemPosition] == newList[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldDogEntity: DogEntity? = oldList[oldItemPosition]
                val newDogEntity: DogEntity? = newList[newItemPosition]

                return oldDogEntity == newDogEntity
//                return oldList[oldItemPosition] == newList[newItemPosition]

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
                    dogsList[adapterPosition]?.let { it1 -> onItemClick?.invoke(it1) }
                }
            }
        }
    }
}