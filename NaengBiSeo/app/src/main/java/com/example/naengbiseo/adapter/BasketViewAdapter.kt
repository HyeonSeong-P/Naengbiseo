package com.example.naengbiseo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.FoodIcon
import com.example.naengbiseo.R

class BasketViewAdapter(var iconList: MutableList<FoodIcon>): RecyclerView.Adapter<BasketViewHolder>() {
    override fun getItemCount(): Int {
        return iconList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        return BasketViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.basket_food_item, parent, false))
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val icon = iconList[position]
        holder.bind(icon, position)

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

    }

    //    ClickListener
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}