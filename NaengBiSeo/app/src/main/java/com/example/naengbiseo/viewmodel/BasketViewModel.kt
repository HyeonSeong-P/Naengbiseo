package com.example.naengbiseo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.naengbiseo.FoodIcon
import com.example.naengbiseo.room.ExcelData
import com.example.naengbiseo.room.ExcelDataRepository
import com.example.naengbiseo.room.FoodData
import com.example.naengbiseo.room.FoodDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.random.Random

class BasketViewModel (
    private val foodDataRepository: FoodDataRepository,
    private val excelDataRepository: ExcelDataRepository
): ViewModel() {
    private val _isButtonClickedData = SingleLiveEvent<Boolean>() // 내부에서 작동
    val isButtonClickedData: LiveData<Boolean> get() = _isButtonClickedData // 외부로 노출
    var basketFoodList = listOf<FoodData>()

    fun getBasketFoodAt(position: Int): FoodData {
        return basketFoodList[position]
    }

    fun getFoodToPurchase(): List<FoodData> {
        val allData = allFoodData.value
        val foodListToPurchase = mutableListOf<FoodData>()
        if (allData != null) {
            for (foodData in allData) {
                if (foodData.purchaseStatus == 0) {
                    foodListToPurchase.add(foodData)
                }
            }
        }
        return foodListToPurchase.toList()
    }

    fun delData(position: Int) {
        viewModelScope.launch { foodDataRepository.delete(basketFoodList[position]) }
    }

    fun insertData(foodData: FoodData) {
        viewModelScope.launch { foodDataRepository.insert(foodData) }
    }

    fun updateData(foodData: FoodData) {
        viewModelScope.launch {
            foodDataRepository.update(foodData)
        }
    }

    fun foodAddButtonClicked(isButtonClicked: Boolean) {
        _isButtonClickedData.setValue(isButtonClicked)
    }

    var TAG = javaClass.simpleName

    /** 뷰모델에서 모델로 데이터를 넣기위한거?*/
    var allFoodData: LiveData<List<FoodData>> = foodDataRepository.getAllData()

    var allExcelData: LiveData<List<ExcelData>> = excelDataRepository.getAllData()

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getExcelData(iconName: String): Pair<String, String>? {
        var excelPair: Pair<String, String>? = Pair("", "")
        val excelList = allExcelData.value
        if (excelList != null) {
            for (data in excelList) {
                if (data.iconName == iconName) {
                    excelPair = Pair(data.storeWay, data.treatWay)
                    break
                }
            }
        }
        return excelPair
    }

    fun insertDataList(foodDataList: MutableList<FoodIcon>) {
        viewModelScope.launch {
            for (foodData in foodDataList) {
                val excelPair = getExcelData(foodData.iconName)
                val storeWay = excelPair!!.first
                val treatWay = excelPair!!.second
                foodDataRepository.insert(FoodData(foodName = foodData.iconName, storeLocation = "cool", foodIcon = foodData.iconResource, purchaseStatus = 0, foodCategory = foodData.category, foodNumber = 1, storeWay = storeWay, treatWay = treatWay, expirationDate = "1111년 11월 11일", buyDate = "1111년 11월 11일", uniqueId = (Int.MIN_VALUE..Int.MAX_VALUE).random()))
            }
        }
    }
}