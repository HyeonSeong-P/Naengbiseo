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
import kotlinx.android.synthetic.main.food_item.view.buy_date
import kotlinx.android.synthetic.main.food_item.view.check_box
import kotlinx.android.synthetic.main.food_item.view.d_day
import kotlinx.android.synthetic.main.food_item.view.food_icon
import kotlinx.android.synthetic.main.food_item.view.food_name
import kotlinx.android.synthetic.main.food_item.view.food_number
import kotlinx.android.synthetic.main.food_item_search_version.view.*
import kotlinx.android.synthetic.main.main_header.view.*
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class SearchViewHolder(v: View):RecyclerView.ViewHolder(v) {

    companion object { // companion object는 JAVA로 치면 static
        private var checkbox_state = 0
        fun activateCheckbox() {
            checkbox_state = 1
        }
        fun inActivateCheckbox() {
            checkbox_state = 0
        }
    }
    var view : View = v

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(foodData: FoodData, position: Int) {
        if(foodData.header == 0 && foodData.Null != 1){
            //var simpleFormat2= SimpleDateFormat("yyyy. MM. dd")
            var simpleFormat= SimpleDateFormat("yyyy년 MM월 dd일")
            var realExpDate =simpleFormat.parse(foodData.expirationDate) // 문자열로 부터 날짜 들고오기!

            var today = Calendar.getInstance() // 현재 날짜
            var dDay = (today.time.time - realExpDate.time) / (60 * 60 * 24 * 1000)

            var dDayText:String
            if(dDay>0) {
                dDayText= "D+" + abs(dDay).toString()
                view.d_day.setTextColor(Color.parseColor("#fb343e"))
            }
            else if(dDay<0) {
                dDayText= "D-" + abs(dDay-1).toString()
                if(abs(dDay)<=3) view.d_day.setTextColor(Color.parseColor("#fb343e"))
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
            view.store_location_text.setText(foodData.storeLocation)

            // 체크박스 관련
            if(checkbox_state == 1) {
                view.check_box.visibility = View.VISIBLE
                view.check_box.setChecked(false)
            }
            else {
                view.check_box.visibility = View.GONE
            }
        }

        if(foodData.header == 1){
            if(foodData.foodCategory != ""){
                view.header_name.text = foodData.foodCategory
            }
            else if(foodData.buyDate != ""){
                view.header_name.text = foodData.buyDate
            }
        }
    }


}