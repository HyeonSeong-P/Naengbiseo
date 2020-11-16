package com.example.naengbiseo.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FoodDao {
    @Query("SELECT * FROM foodData")
    fun getAll(): LiveData<List<FoodData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(inputUserData: FoodData)

    @Delete // db 데이터 삭제기능
    fun deleteData(food: FoodData)
}