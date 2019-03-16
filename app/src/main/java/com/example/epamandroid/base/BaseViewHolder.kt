package com.example.epamandroid.base

import android.support.v7.widget.RecyclerView
import android.view.View

class BaseViewHolder<T : View>(itemView: T) : RecyclerView.ViewHolder(itemView) {

    val view: T
        get() = itemView as T
}