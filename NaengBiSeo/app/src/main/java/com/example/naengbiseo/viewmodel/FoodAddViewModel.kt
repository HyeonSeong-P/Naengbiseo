package com.example.naengbiseo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.naengbiseo.FoodIcon
import com.example.naengbiseo.room.FoodData
import com.example.naengbiseo.room.FoodDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class FoodAddViewModel (private val foodDataRepository: FoodDataRepository): ViewModel() {
    private val _icon_data = SingleLiveEvent<Pair<Int,String>>() // 내부에서 작동
    val icon_data: LiveData<Pair<Int,String>> get() = _icon_data // 외부로 노출

    fun setIcon(foodIcon:Int,iconName:String){
        var iconPair:Pair<Int,String> = Pair(foodIcon,iconName)
        _icon_data.setValue(iconPair)
    }

    var TAG = javaClass.simpleName

    /** 뷰모델에서 모델로 데이터를 넣기위한거?*/
    var allFoodData: LiveData<List<FoodData>> = foodDataRepository.getAllData()


    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun insertData(foodData: FoodData) {
        viewModelScope.launch { foodDataRepository.insert(foodData) }
    }
}