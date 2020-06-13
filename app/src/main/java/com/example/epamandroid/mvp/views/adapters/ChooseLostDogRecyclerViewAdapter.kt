package com.example.epamandroid.mvp.views.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.epamandroid.R
import com.example.epamandroid.mvp.views.annotationclasses.ViewType

class ChooseMapRestaurantRecyclerViewAdapter : RecyclerView.Adapter<ChooseMapRestaurantRecyclerViewAdapter.ViewHolder>() {
    private val typeList = ArrayList<String>()

    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.type_text_view, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val holder = viewHolder.itemView as TextView

        holder.text = typeList[position]
    }

    override fun getItemCount(): Int {
       return typeList.size
    }

    fun addItems(result: List<String>?) {
        result?.let{typeList.addAll(it)}
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                if (itemViewType == ViewType.RESTAURANT) {
                    typeList[adapterPosition].let { it1 -> onItemClick?.invoke(it1) }
                }
            }
        }
    }
}