package com.example.epamandroid.views.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.epamandroid.R
import com.example.epamandroid.entities.DogEntity
import com.example.epamandroid.views.annotationclasses.ViewType
import com.example.epamandroid.views.annotationclasses.ViewType.Companion.DOG
import com.example.epamandroid.views.annotationclasses.ViewType.Companion.LOADING
import com.example.epamandroid.views.compoundviews.DogView
import java.util.*

class HomeRecyclerViewAdapter : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    private val dogsList = ArrayList<DogEntity>()

    private var isShowLastViewAsLoading = false

    private lateinit var dogEntity: DogEntity

    var onItemClick: ((DogEntity) -> Unit)? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        return when (viewType) {
            DOG -> {
                ViewHolder(DogView(parent.context))
            }
            LOADING -> {
                ViewHolder(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.layout_progress, parent, false)
                                as FrameLayout
                )
            }
            else -> {
                ViewHolder(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.error_view, parent, false)
                                as FrameLayout
                )
            }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (getItemViewType(position) == ViewType.DOG) {
            dogEntity = dogsList[position]

            (viewHolder.itemView as DogView)
                    .setDogBreed(dogEntity.breed)
                    .isCanLiveAtHome(dogEntity.isCanLiveAtHome)
                    .isAffectionate(dogEntity.isAffectionate)
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
            isShowLastViewAsLoading = isShow
        }
    }

    fun addItems(result: List<DogEntity>?) {
        result?.let { dogsList.addAll(it) }
        notifyDataSetChanged()
    }
    fun getItems(): ArrayList<DogEntity> {
        return dogsList
    }

    private fun deleteByIndex(i: Int) {
        dogsList.removeAt(i)
        notifyItemRemoved(i)
        notifyItemRangeChanged(i, dogsList.size - 1)
    }

    fun getMaxStudentId(): Int? {

        return when {
            dogsList.size == 0 -> {
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