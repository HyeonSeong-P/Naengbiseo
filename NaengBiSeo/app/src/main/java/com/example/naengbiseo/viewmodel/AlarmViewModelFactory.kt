package com.example.naengbiseo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.naengbiseo.room.ExcelDataRepository
import com.example.naengbiseo.room.FoodDataRepository

class AlarmViewModelFactory(
    private val foodDataRepository: FoodDataRepository,
    private val excelDataRepository: ExcelDataRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlarmViewModel(foodDataRepository, excelDataRepository) as T
    }
}