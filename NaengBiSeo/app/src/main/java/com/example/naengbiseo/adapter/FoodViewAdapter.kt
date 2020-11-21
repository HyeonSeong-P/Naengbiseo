package com.example.naengbiseo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.R
import com.example.naengbiseo.viewmodel.MainViewModel

class FoodViewAdapter(private val viewModel: MainViewModel, val location:Int): RecyclerView.Adapter<FoodViewHolder>() {

    override fun getItemCount(): Int {
        return when(location){
            0-> viewModel.getShelfData().size
            1-> viewModel.getCoolData().size
            else-> viewModel.getColdData().size
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return FoodViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        when(location){
            0-> holder.bind(viewModel.getShelfData()[position], position)
            1-> holder.bind(viewModel.getCoolData()[position], position)
            else -> holder.bind(viewModel.getColdData()[position], position)
        }
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