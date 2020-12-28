package com.example.naengbiseo

import android.content.Intent
import com.example.naengbiseo.viewmodel.MainViewModel

class AddData(
    val foodIcon: Int,
    val iconName: String,
    val category: String,
    val storeWay: String,
    val treatWay: String
) {
    val getIconName: String
        get() {
            return iconName
        }
    val getfoodIcon: Int
        get() {
            return foodIcon
        }

    val getCategory: String
        get() {
            return category
        }

    val getStoreWay: String
        get() {
            return storeWay
        }

    val getTreatWay: String
        get() {
            return treatWay
        }
}