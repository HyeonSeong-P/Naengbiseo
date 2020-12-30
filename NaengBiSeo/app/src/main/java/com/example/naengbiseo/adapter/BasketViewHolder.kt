package com.example.naengbiseo.adapter

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.FoodIcon
import com.example.naengbiseo.fragment.BasketFragment
import com.example.naengbiseo.room.FoodData
import kotlinx.android.synthetic.main.basket_food_item.view.*
import kotlinx.android.synthetic.main.food_category_header_item.view.*
import kotlinx.android.synthetic.main.food_icon_item.view.*
import kotlin.coroutines.coroutineContext

class BasketViewHolder(v: View): RecyclerView.ViewHolder(v) {
    var view: View = v
    private val TYPE_CATEGORY_HEADER = 0

    fun bind(foodData: FoodData, position: Int) {
        view.foodIcon.setImageResource(foodData.foodIcon)
        view.foodNameEditText.setText(foodData.foodName)
        view.quantityTextView.setText(foodData.foodNumber.toString())
        when (foodData.storeLocation) {
            "shelf" -> {
                view.radio_btn1.isChecked = true
            }
            "cool" -> {
                view.radio_btn2.isChecked = true
            }
            "cold" -> {
                view.radio_btn3.isChecked = true
            }
            else -> {
                Toast.makeText(view.context, "radio button error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


