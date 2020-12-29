package com.example.naengbiseo.adapter

import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.room.FoodData
import kotlinx.android.synthetic.main.food_item.view.*
import kotlinx.android.synthetic.main.main_header.view.*
import java.text.SimpleDateFormat
import java.util.*

class AlarmViewHolder(v: View): RecyclerView.ViewHolder(v) {

    var view : View = v

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(foodData: FoodData, dDay: Long) {
        var dDayText:String
        if(dDay>0) {
            dDayText= "D+" + Math.abs(dDay).toString()
            view.d_day.setTextColor(Color.parseColor("#fb343e"))
        }
        else if(dDay<0) {
            dDayText= "D-" + Math.abs(dDay - 1).toString()
            if(Math.abs(dDay) <=3) view.d_day.setTextColor(Color.parseColor("#fb343e"))
        }
        else {
            dDayText="D-day"
            view.d_day.setTextColor(Color.parseColor("#fb343e"))
        }
        view.food_name.setText(foodData.foodName)
        view.food_number.setText(foodData.foodNumber.toString())
        view.buy_date.setText(foodData.buyDate)
        view.d_day.setText(dDayText)
        view.food_icon.setImageResource(foodData.foodIcon)
    }
}