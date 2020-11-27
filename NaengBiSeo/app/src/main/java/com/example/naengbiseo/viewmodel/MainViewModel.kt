package com.example.naengbiseo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.naengbiseo.room.FoodData
import com.example.naengbiseo.room.FoodDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class MainViewModel(private val foodDataRepository: FoodDataRepository): ViewModel() {
    private val _del_data=SingleLiveEvent<MutableList<String>>()
    val del_data: LiveData<MutableList<String>> get() = _del_data
    val copyDelList:MutableList<String> = mutableListOf<String>()

    fun addDelData(s:String){
        Log.d("dd","추가됐음")
        copyDelList.add(s)
        _del_data.setValue(copyDelList)
    }

    fun removeDelData(s:String){
        Log.d("dd","제거됐음")
        copyDelList.remove(s)
        _del_data.setValue(copyDelList)
    }

    fun clearDelData(){
        _del_data.value?.clear()
    }
    fun getDelData():MutableList<String>?{
        return _del_data.value
    }
    var TAG = javaClass.simpleName


    private val _trash_button_cool_event = SingleLiveEvent<Int>()
    val trash_button_cool_event get() = _trash_button_cool_event

    private val _trash_button_cold_event = SingleLiveEvent<Int>()
    val trash_button_cold_event get() = _trash_button_cold_event

    private val _trash_button_shelf_event = SingleLiveEvent<Int>()
    val trash_button_shelf_event get() = _trash_button_shelf_event

    private val _searchText = SingleLiveEvent<String>()
    val searchText get() = _searchText

    fun onTrashButton(i:Int) { /** 뷰의 onClickListener 호출될때 얘를 호출해 싱글라이브데이터 의 Setdata를 call
    뷰와 뷰모델의 연결?....*/
        _trash_button_cool_event.setValue(i)
        _trash_button_cold_event.setValue(i)
        _trash_button_shelf_event.setValue(i)
    }


    /** 뷰모델에서 모델로 데이터를 넣기위한거?*/
    var allFoodData: LiveData<List<FoodData>> = foodDataRepository.getAllData()


    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun insertData(foodData: FoodData) {
        viewModelScope.launch { foodDataRepository.insert(foodData) }
    }

    fun deleteData(){
        Log.d("ss","진입함")
        var foodList:List<FoodData>? = allFoodData.value
        var dList=del_data.value
        if(foodList != null){
            Log.d("ss","진입해ㅐㅆ냐?!!")
            if(dList!=null) {
                Log.d("ss","진입해ㅐㅆ냐222?!!")
                for (s in dList) {
                    Log.d("copyDelList", s)
                    for (foodData in foodList) {
                        if (foodData.foodName == s) {
                            Log.d("ss", "삭제 전")
                            viewModelScope.launch {
                                foodDataRepository.delete(foodData)
                            }
                        }
                    }

                }
            }
        }

    }

    fun getColdData(): List<FoodData> {
        var coldFoodData:MutableList<FoodData> = mutableListOf()
        var foodList:List<FoodData>? = allFoodData.value
        if(foodList != null){
            for(data: FoodData in foodList){
                if(data.storeLocation=="cold"){
                    coldFoodData.add(data)
                }
            }
        }
        return coldFoodData.toList()
    }

    fun getCoolData(): List<FoodData> {
        var coolFoodData:MutableList<FoodData> = mutableListOf()
        var foodList:List<FoodData>? = allFoodData.value
        if(foodList != null){
            for(data: FoodData in foodList){
                if(data.storeLocation=="cool"){
                    coolFoodData.add(data)
                }
            }
        }
        return coolFoodData.toList()
    }

    fun getShelfData(): List<FoodData> {
        var shelfFoodData:MutableList<FoodData> = mutableListOf()
        var foodList:List<FoodData>? = allFoodData.value
        if(foodList != null){
            for(data: FoodData in foodList){
                if(data.storeLocation=="shelf"){
                    shelfFoodData.add(data)
                }
            }
        }
        return shelfFoodData.toList()
    }
}