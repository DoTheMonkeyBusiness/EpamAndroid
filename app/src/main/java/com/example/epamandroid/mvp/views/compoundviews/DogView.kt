package com.example.epamandroid.mvp.views.compoundviews

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.restaurant_view.view.*

class RestaurantView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
//        layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        inflate(getContext(), R.layout.restaurant_view, this)
    }

    fun getRestaurantIcon(): ImageView = restaurantIcon
    fun setRestaurantType(type: String?): RestaurantView {

        when (type){
            null -> {restaurantTypeTextView?.setText(R.string.error)}
            else -> {restaurantTypeTextView?.text = type}
        }

        return this
    }
    fun isCanLiveAtHome(isCanLiveAtHome: Boolean?): RestaurantView {
        when {
            isCanLiveAtHome == null -> { restaurantViewCanLiveAtHomeIcon.setImageResource(R.drawable.ic_close)}
            isCanLiveAtHome -> restaurantViewCanLiveAtHomeIcon.setImageResource(R.drawable.ic_check)
            else -> restaurantViewCanLiveAtHomeIcon.setImageResource(R.drawable.ic_close)
        }
        return this
    }

    fun isLikes(isLikes: Boolean): RestaurantView {
        when {
            isLikes -> restaurantIsLikes.setImageResource(R.drawable.ic_star)
            else -> restaurantIsLikes.setImageResource(R.drawable.ic_star_border)
        }
        return this
    }
}