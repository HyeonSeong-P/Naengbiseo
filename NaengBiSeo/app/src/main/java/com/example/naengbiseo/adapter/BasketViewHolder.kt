package com.example.naengbiseo.adapter

import android.app.Application
import android.view.View
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.FoodIcon
import com.example.naengbiseo.fragment.BasketFragment
import kotlinx.android.synthetic.main.basket_food_item.view.*
import kotlinx.android.synthetic.main.food_category_header_item.view.*
import kotlinx.android.synthetic.main.food_icon_item.view.*
import kotlin.coroutines.coroutineContext

class BasketViewHolder(v: View): RecyclerView.ViewHolder(v) {
    var view: View = v
    private val TYPE_CATEGORY_HEADER = 0

    fun bind(icon: FoodIcon, position: Int) {
        view.foodIcon.setImageResource(icon.getIconResource)
        view.foodNameEditText.setText(icon.getIconName)
    }
}


