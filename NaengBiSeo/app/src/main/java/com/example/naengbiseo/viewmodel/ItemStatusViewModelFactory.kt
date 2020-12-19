package com.example.naengbiseo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.naengbiseo.room.FoodDataRepository


class ItemStatusViewModelFactory(
    private val foodDataRepository: FoodDataRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ItemStatusViewModel(foodDataRepository) as T
    }
}