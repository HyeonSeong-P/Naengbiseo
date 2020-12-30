package com.example.naengbiseo

import android.content.Intent
import com.example.naengbiseo.viewmodel.MainViewModel

class FoodIcon(val iconName: String, val iconResource: Int, val category: String = "") {
    val getIconName: String
        get() {
            return iconName
        }
    val getIconResource: Int
        get() {
            return iconResource
        }

    val getCategory:String
        get(){
            return category
        }
}