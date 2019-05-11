package com.example.epamandroid.mvp.views.adapters

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.epamandroid.R
import com.example.epamandroid.entities.DogEntity
import com.example.epamandroid.mvp.views.annotationclasses.ViewType
import com.example.epamandroid.mvp.views.annotationclasses.ViewType.Companion.DOG
import com.example.epamandroid.mvp.views.annotationclasses.ViewType.Companion.LOADING
import com.example.epamandroid.mvp.views.compoundviews.DogView
import com.example.imageloader.IMichelangelo
import com.example.imageloader.Michelangelo
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class HomeRecyclerViewAdapter(context: Context?) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>(),
    IAutoUpdatableHomeAdapter {

    private val michelangelo: IMichelangelo = Michelangelo(context)
    private val dogsList = ArrayList<DogEntity>()

    //    var dogsList: ArrayList<DogEntity> by Delegates.observable(arrayListOf()) {
//            prop, old, new ->
//        autoNotify(old, new) { o, n -> o.id == n.id }
//    }
    private val layoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private var isShowLastViewAsLoading = false

    private lateinit var dogEntity: DogEntity

    var onItemClick: ((DogEntity) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return when (viewType) {
            DOG -> {
                ViewHolder(layoutInflater.inflate(R.layout.dog_view_item, parent, false))
            }
            LOADING -> {
                ViewHolder(
                    layoutInflater.inflate(R.layout.layout_progress, parent, false)
                            as FrameLayout
                )
            }
            else -> {
                ViewHolder(
                    layoutInflater.inflate(R.layout.error_view, parent, false)
                            as FrameLayout
                )
            }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (getItemViewType(position) == ViewType.DOG) {
            val holder = (viewHolder.itemView as DogView)
            dogEntity = dogsList[position]

            holder
                .setDogBreed(dogEntity.breed)
            michelangelo.load(holder.getDogIcon(), dogEntity.photo)
        }
    }

    override fun getItemCount(): Int {
        return if (isShowLastViewAsLoading) {
            dogsList.size + 1
        } else {
            dogsList.size
        }
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
            val oldItems: Any = dogsList.clone()
            isShowLastViewAsLoading = isShow
//            notifyDataSetChanged()
            notifyChanges(oldItems as List<DogEntity>, dogsList)
        }
    }

    fun addItems(result: List<DogEntity>?) {
        val oldItems: Any = dogsList.clone()

        result?.let { dogsList.addAll(it) }
        notifyChanges(oldItems as List<DogEntity>, dogsList)
//        notifyDataSetChanged()
    }

    fun updateDogsList(list: ArrayList<DogEntity>?) {
        if (list !== null) {
            val oldItems: Any = dogsList.clone()
            dogsList.clear()
            dogsList.addAll(list)
//            notifyDataSetChanged()
            notifyChanges(oldItems as List<DogEntity>, dogsList)
        }
    }

    fun getDogList(): ArrayList<DogEntity> {
        return dogsList
    }

    private fun deleteByIndex(i: Int) {
        dogsList.removeAt(i)
        notifyItemRemoved(i)
        notifyItemRangeChanged(i, dogsList.size - 1)
    }

    fun getMaxId(): Int? {

        return when {
            dogsList.isEmpty() -> {
                0
            }
            (dogsList.size < dogsList.maxBy { it.id }?.id ?: dogsList.size) -> {
                dogsList.size - 1
            }
            else -> {
                dogsList.maxBy { it.id }?.id
            }

        }
    }

    fun getEntityById(id: Int): DogEntity = dogsList[id]

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

    private fun notifyChanges(oldList: List<DogEntity>, newList: List<DogEntity>) {

        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].id == newList[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]

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
                    onItemClick?.invoke(dogsList[adapterPosition])
                }
            }
        }
    }
}