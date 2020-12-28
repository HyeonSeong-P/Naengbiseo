package com.example.naengbiseo.adapter

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.room.FoodData
import kotlinx.android.synthetic.main.food_icon_item.view.*
import kotlinx.android.synthetic.main.food_item.view.*
import kotlinx.android.synthetic.main.food_item_search_version.view.buy_date
import kotlinx.android.synthetic.main.food_item.view.check_box
import kotlinx.android.synthetic.main.food_item_search_version.view.d_day
import kotlinx.android.synthetic.main.food_item_search_version.view.food_icon
import kotlinx.android.synthetic.main.food_item_search_version.view.food_name
import kotlinx.android.synthetic.main.food_item_search_version.view.food_number
import kotlinx.android.synthetic.main.food_item_search_version.view.slt
import kotlinx.android.synthetic.main.food_item_search_version.view.*
import kotlinx.android.synthetic.main.main_header.view.*
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class SearchViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    companion object { // companion object는 JAVA로 치면 static
        private var checkbox_state = 0
        fun activateCheckbox() {
            checkbox_state = 1
        }

        fun inActivateCheckbox() {
            checkbox_state = 0
        }
    }

    var view: View = v

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(foodData: FoodData, position: Int) {
        if (foodData.header == 0 && foodData.Null != 1) {
            //var simpleFormat2= SimpleDateFormat("yyyy. MM. dd")
            var simpleFormat= SimpleDateFormat("yyyy년 MM월 dd일")
            var simpleFormat2= SimpleDateFormat("yyyy. MM. dd")

            var realExpDate =simpleFormat.parse(foodData.expirationDate) // 문자열로 부터 날짜 들고오기!

            var realBuyDate = simpleFormat.parse(foodData.buyDate)
            var dateString = simpleFormat2.format(realBuyDate)

            var today = Calendar.getInstance() // 현재 날짜
            var dDay = (today.time.time - realExpDate.time) / (60 * 60 * 24 * 1000)

            var dDayText: String
            if (dDay > 0) {
                dDayText = "D+" + abs(dDay).toString()
                view.d_day.setTextColor(Color.parseColor("#fb343e"))
            } else if (dDay < 0) {
                dDayText = "D-" + abs(dDay - 1).toString()
                if (abs(dDay - 1) < 4) view.d_day.setTextColor(Color.parseColor("#fb343e"))
            } else {
                dDayText = "D-day"
                view.d_day.setTextColor(Color.parseColor("#fb343e"))
            }
            when(foodData.storeLocation){
                "shelf" -> view.slt.setText("선반")
                "cool" -> view.slt.setText("냉장")
                "cold" -> view.slt.setText("냉동")
            }


            view.food_name.setText(foodData.foodName)
            view.food_number.setText(foodData.foodNumber.toString())
            view.buy_date.setText(dateString)
            view.d_day.setText(dDayText)
            view.food_icon.setImageResource(foodData.foodIcon)

        }

        if (foodData.header == 1) {
            if (foodData.foodCategory != "") {
                view.header_name.text = foodData.foodCategory
            } else if (foodData.buyDate != "") {
                view.header_name.text = foodData.buyDate
            }
        }
    }


}