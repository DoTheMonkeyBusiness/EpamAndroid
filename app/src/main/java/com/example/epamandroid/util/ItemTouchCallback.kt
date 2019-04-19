package com.example.epamandroid.util

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.example.epamandroid.mvp.views.adapters.HomeRecyclerViewAdapter
import com.example.epamandroid.mvp.views.annotationclasses.ViewType

class ItemTouchCallback(private val recycler: RecyclerView, private val adapter: HomeRecyclerViewAdapter) :
        ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.ACTION_STATE_IDLE,
                ItemTouchHelper.START
        ) {

    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            viewHolder1: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
//        val adapterPosition = viewHolder.adapterPosition
//
//        if (RecyclerView.NO_POSITION != adapterPosition) {
//
//            adapter.onItemDismiss(adapterPosition)
//            removeEntityCallback.onRemoveEntity(adapterPosition)
//        }
    }

    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (adapter.getItemViewType(viewHolder.adapterPosition) == ViewType.LOADING) {
            return 0
        }
        return super.getSwipeDirs(recyclerView, viewHolder)
    }
}