package com.example.naengbiseo.adapter

import android.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.FoodIcon
import com.example.naengbiseo.R
import com.example.naengbiseo.room.FoodData
import kotlinx.android.synthetic.main.basket_food_item.view.*
import kotlinx.android.synthetic.main.basket_toast_message.view.*
import kotlinx.android.synthetic.main.fragment_food_add.*

class BasketViewAdapter(var iconList: MutableList<FoodIcon>): RecyclerView.Adapter<BasketViewHolder>() {
    override fun getItemCount(): Int {
        return iconList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        return BasketViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.basket_food_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val icon = iconList[position]
        holder.bind(icon, position)

        /*holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }*/
        holder.view.buyButton.setOnClickListener {
            val itemView = holder.view
            var toastView = LayoutInflater.from(itemView.context).inflate(R.layout.basket_toast_message, null)
            var toast = Toast(itemView.context)
            val radio_btn_id = itemView.radio_group.checkedRadioButtonId

            toast.view = toastView
            when(radio_btn_id) {
                itemView.radio_btn1.id -> {
                    toastView.basketToastMessageTextView.text = "선반보관 해두었습니다:>"
                }
                itemView.radio_btn2.id -> {
                    toastView.basketToastMessageTextView.text = "냉장보관 해두었습니다:>"
                }
                itemView.radio_btn3.id -> {
                    toastView.basketToastMessageTextView.text = "냉동보관 해두었습니다:>"
                }
            }
            toast.duration = Toast.LENGTH_SHORT
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
        holder.view.minusBtn.setOnClickListener {
            var quantity = holder.view.quantityTextView.text.toString().toInt()
            if (quantity > 1) holder.view.quantityTextView.setText((quantity - 1).toString())
        }
        holder.view.plusBtn.setOnClickListener {
            var quantity = holder.view.quantityTextView.text.toString().toInt()
            holder.view.quantityTextView.setText((quantity + 1).toString())
        }
    }

    //    ClickListener
    /*interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }*/
}