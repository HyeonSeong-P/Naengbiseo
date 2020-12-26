package com.example.naengbiseo.adapter

import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.naengbiseo.viewmodel.BasketViewModel
import com.example.naengbiseo.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.basket_empty_layout.view.*
import kotlinx.android.synthetic.main.basket_food_item.view.*
import kotlinx.android.synthetic.main.basket_toast_message.view.*
import kotlinx.android.synthetic.main.fragment_food_add.*

class BasketViewAdapter(private val viewModel: BasketViewModel): RecyclerView.Adapter<BasketViewHolder>() {
    private val TYPE_BASKET_EMPTY = 0
    private val TYPE_BASKET_NOT_EMPTY = 1
    override fun getItemCount(): Int {
        if (viewModel.basketFoodList.isEmpty()) {
            return 1
        }
        return viewModel.basketFoodList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (viewModel.basketFoodList.isEmpty()) {
            return TYPE_BASKET_EMPTY
        }
        return TYPE_BASKET_NOT_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        var myLayout: Int
        when (viewType) {
            TYPE_BASKET_EMPTY -> myLayout = R.layout.basket_empty_layout
            TYPE_BASKET_NOT_EMPTY -> myLayout = R.layout.basket_food_item
            else -> myLayout = R.layout.fragment_error
        }
        return BasketViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        if (viewModel.basketFoodList.isEmpty()) {
            holder.view.addFoodButtonInBasket.setOnClickListener{
                viewModel.addFoodButtonClicked(1)
            }
        }
        else {
            val foodData = viewModel.getBasketFoodAt(position)
            holder.bind(foodData, position)

            holder.view.buyButton.setOnClickListener {
                val itemView = holder.view
                var toastView = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.basket_toast_message, null)
                var toast = Toast(itemView.context)
                val radio_btn_id = itemView.radio_group.checkedRadioButtonId

                toast.view = toastView
                when (radio_btn_id) {
                    itemView.radio_btn1.id -> {
                        toastView.basketToastMessageTextView.text = "선반보관 해두었습니다:>"
                    }
                    itemView.radio_btn2.id -> {
                        toastView.basketToastMessageTextView.text = "냉장보관 해두었습니다:>"
                    }
                    itemView.radio_btn3.id -> {
                        toastView.basketToastMessageTextView.text = "냉동보관 해두었습니다:>"
                    }
                    else -> {
                        toastView.basketToastMessageTextView.text = "보관 Error"
                    }
                }

                viewModel.basketFoodList[position].purchaseStatus = 1
                for (foodData in viewModel.basketFoodList) { // 지금까지 수정했던 basketFoodList를 가지고 db수정
                    viewModel.updateData(foodData)
                }

                toast.duration = Toast.LENGTH_SHORT
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
            holder.view.foodNameEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    viewModel.basketFoodList[position].foodName = p0.toString()
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })
            holder.view.minusBtn.setOnClickListener {
                val itemView = holder.view
                var quantity = itemView.quantityTextView.text.toString().toInt()
                if (quantity > 1) {
                    quantity--
                    itemView.quantityTextView.setText((quantity).toString())
                }
                viewModel.basketFoodList[position].foodNumber = quantity
            }
            holder.view.plusBtn.setOnClickListener {
                val itemView = holder.view
                var quantity = itemView.quantityTextView.text.toString().toInt()
                quantity++
                itemView.quantityTextView.setText((quantity).toString())
                viewModel.basketFoodList[position].foodNumber = quantity
            }
            holder.view.radio_group.setOnCheckedChangeListener { radioGroup, checkedId ->
                val itemView = holder.view
                val storeLocation: String
                when (checkedId) {
                    itemView.radio_btn1.id -> {
                        storeLocation = "shelf"
                    }
                    itemView.radio_btn2.id -> {
                        storeLocation = "cool"
                    }
                    itemView.radio_btn3.id -> {
                        storeLocation = "cold"
                    }
                    else -> {
                        storeLocation = "error"
                    }
                }
                viewModel.basketFoodList[position].storeLocation = storeLocation
            }
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