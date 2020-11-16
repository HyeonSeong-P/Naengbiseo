package com.example.naengbiseo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**데이터를 저장할 Entity, ROOM(MODEL) 관련 클래스.
 */

//@Entity(tableName = "inputMsg")
@Entity(indices = [Index(value = ["재료명", "보관장소"], unique = true)])

data class FoodData(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ColumnInfo(name = "재료명") var foodName: String,
    @ColumnInfo(name = "보관장소") var storeLocation: String/*,
    @ColumnInfo(name = "카테고리") var foodCategory: String,
    @ColumnInfo(name = "수량") var foodNumber: Int,
    @ColumnInfo(name = "메모") var foodMemo: String,
    @ColumnInfo(name = "보관방법") var storeWay: String,
    @ColumnInfo(name = "처리방법") var treatWay: String,
    @ColumnInfo(name = "유통기한") var expirationDate: String,
    @ColumnInfo(name = "구매일자") var buyDate: String*/
)