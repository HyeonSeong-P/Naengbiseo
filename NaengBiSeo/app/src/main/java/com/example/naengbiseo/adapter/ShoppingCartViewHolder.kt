package com.example.naengbiseo.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.FoodIcon
import com.example.naengbiseo.R
import com.example.naengbiseo.room.FoodData
import kotlinx.android.synthetic.main.food_category_header_item.view.*
import kotlinx.android.synthetic.main.food_icon_item.view.*

class ShoppingCartViewHolder(v: View):RecyclerView.ViewHolder(v) {
    var view : View = v
    private val TYPE_CATEGORY_HEADER = 0

    fun bind(icon: FoodIcon, position: Int) {
        // header가 들어올때
        if (icon.getIconResource == TYPE_CATEGORY_HEADER) {
            view.foodCategoryName.setText(icon.getIconName)
        }
        // icon이 들어올때
        else if (icon.getIconName != "footer"){
            view.foodIconImageView.setImageResource(icon.getIconResource)
            view.foodIconTextView.setText(icon.getIconName)
        }
    }


}