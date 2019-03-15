package com.example.epamandroid

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class ItemTouchCallback(private val recycler: RecyclerView, private val adapter: RecyclerViewAdapter) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.START or ItemTouchHelper.END
    ) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        viewHolder1: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onItemMove(viewHolder.adapterPosition, viewHolder1.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        val adapterPosition = viewHolder.adapterPosition

        if (RecyclerView.NO_POSITION != adapterPosition) {
            adapter.onItemDismiss(adapterPosition)
        }
    }
}