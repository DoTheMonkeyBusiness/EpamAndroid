package com.example.epamandroid

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val items = object : ArrayList<Any>() {
        init {

            for (i in 0..20) {
                add(Any())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerViewAdapter.ViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_view, parent, false)
                as TextView
        return ViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        (viewHolder.itemView as TextView).text = "View #$position"
    }

    fun deleteByIndex(i: Int) {
        items.removeAt(i)
        notifyItemRemoved(i)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }

        notifyItemMoved(fromPosition, toPosition)
    }

    fun onItemDismiss(adapterPosition: Int) {
        deleteByIndex(adapterPosition)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}