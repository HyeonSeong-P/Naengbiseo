package com.example.naengbiseo.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.room.FoodData
import kotlinx.android.synthetic.main.food_item.view.*

class FoodViewHolder(v: View):RecyclerView.ViewHolder(v) {
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

    fun bind(foodData: FoodData, position: Int) {
        view.food_name.setText(foodData.foodName)
        // 체크박스 관련
        if(checkbox_state == 1) {
            view.check_box.visibility = View.VISIBLE
            view.check_box.setChecked(false)
        }
        else {
            view.check_box.visibility = View.GONE
        }
    }


}