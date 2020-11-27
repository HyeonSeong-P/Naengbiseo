package com.example.naengbiseo.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.FoodIcon
import kotlinx.android.synthetic.main.food_category_header_item.view.*
import kotlinx.android.synthetic.main.food_icon_item.view.*

class BasketViewHolder(v: View): RecyclerView.ViewHolder(v) {
    var view: View = v
    private val TYPE_CATEGORY_HEADER = 0

    fun bind(icon: FoodIcon, position: Int) {
        view.foodIconImageView.setImageResource(icon.getIconResource)
        view.foodIconTextView.setText(icon.getIconName)
    }
}


