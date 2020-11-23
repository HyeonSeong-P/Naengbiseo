package com.example.naengbiseo

import android.content.Intent
import com.example.naengbiseo.viewmodel.MainViewModel

class FoodIcon(val iconName: String, val iconResource: Int) {
    val getIconResource: Int
        get() {
            return iconResource
        }
    val getIconName: String
        get() {
            return iconName
        }
}