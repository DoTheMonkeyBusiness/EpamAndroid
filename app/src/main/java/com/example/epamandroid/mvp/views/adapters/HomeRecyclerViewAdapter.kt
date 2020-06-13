package com.example.epamandroid. mvp.views.adapters

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epamandroid.R
import com.example.epamandroid.models.RestaurantEntity
import com.example.epamandroid.mvp.views.annotationclasses.ViewType
import com.example.epamandroid.mvp.views.compoundviews.RestaurantView
import com.example.imageloader.IMichelangelo
import com.example.imageloader.Michelangelo

class HomeRecyclerViewAdapter(context: Context?) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    private val michelangelo: IMichelangelo = Michelangelo.getInstance(context)
    private val restaurantsList:  ArrayList<RestaurantEntity> = arrayListOf()

    private var restaurantEntity: RestaurantEntity? = null

    var onItemClick: ((RestaurantEntity) -> Unit)? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.restaurant_view_item, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val holder = (viewHolder.itemView as RestaurantView)
            restaurantEntity = restaurantsList[position]

            holder
                    .setRestaurantType(restaurantEntity?.type)
                    .isCanLiveAtHome(restaurantEntity?.isCanLiveAtHome)
            michelangelo.load(holder.getRestaurantIcon(), restaurantEntity?.photo)
    }

    override fun getItemCount(): Int {
            return restaurantsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < restaurantsList.size) {
            ViewType.RESTAURANT
        } else {
            ViewType.LOADING
        }
    }

    fun addItems(result: List<RestaurantEntity>?) {
        val oldItems: ArrayList<RestaurantEntity> = arrayListOf()

        oldItems.addAll(restaurantsList)
        result?.let { restaurantsList.addAll(it) }
        notifyChanges(oldItems, restaurantsList)
    }

    fun updateRestaurantsList(list: ArrayList<RestaurantEntity>?) {
        if (list !== null) {
            val oldItems: ArrayList<RestaurantEntity> = arrayListOf()

            oldItems.addAll(restaurantsList)
            restaurantsList.clear()
            restaurantsList.addAll(list)
            notifyChanges(oldItems, restaurantsList)
        }
    }

    fun getRestaurantList(): ArrayList<RestaurantEntity>? {
        return restaurantsList
    }

    fun getMaxId(): Int {

        return when {
            restaurantsList.isNullOrEmpty() -> {
                0
            }
            else -> {
                var maxElement = 0
                restaurantsList.forEach {
                    if (it.id != null && it.id > maxElement){
                        maxElement = it.id
                    }
                }

                maxElement
            }
        }
    }

    fun getEntityById(id: Int): RestaurantEntity? = restaurantsList[id]

    fun isEmptyRestaurantsList(): Boolean {
        return restaurantsList.isEmpty()
    }

    private fun notifyChanges(oldList: ArrayList<RestaurantEntity>, newList: ArrayList<RestaurantEntity>) {

        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldRestaurantEntity: RestaurantEntity? = oldList[oldItemPosition]
                val newRestaurantEntity: RestaurantEntity? = newList[newItemPosition]

                return oldRestaurantEntity == newRestaurantEntity
            }

            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size
        })

        diff.dispatchUpdatesTo(this@HomeRecyclerViewAdapter)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                if (itemViewType == ViewType.RESTAURANT) {
                    restaurantsList[adapterPosition].let { it1 -> it1.let { it2 -> onItemClick?.invoke(it2) } }
                }
            }
        }
    }
}