package com.example.naengbiseo

import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.example.naengbiseo.room.*
import com.example.naengbiseo.viewmodel.AlarmViewModel
import com.example.naengbiseo.viewmodel.AlarmViewModelFactory
import com.example.naengbiseo.viewmodel.MainViewModel
import com.example.naengbiseo.viewmodel.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BroadcastD: BroadcastReceiver() {
    val INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED
    val CHANNEL_ID = "test"
    val notificationId = 1000
    lateinit var dao1: FoodDao
    lateinit var repository1: FoodDataRepository

    override fun onReceive(context: Context, intent: Intent) { //알람 시간이 되었을때 onReceive를 호출함
        dao1 = AppDatabase.getInstance(context).foodDao()
        repository1 = FoodDataRepository.getInstance(dao1)
        CoroutineScope(Dispatchers.IO).launch {
            val allFoodData = repository1.getAllData2()
            var dDayFoodList = mutableListOf<Pair<FoodData, Long>>()
            val simpleFormat= SimpleDateFormat("yyyy년 MM월 dd일")
            val today = Calendar.getInstance() // 현재 날짜
            val my_d_day = MainActivity.pref_dDay.myEditText.toInt()

            for (foodData in allFoodData) { // filtering
                val realExpDate =simpleFormat.parse(foodData.expirationDate) // 문자열로 부터 날짜 들고오기!
                val dDay = (today.time.time - realExpDate.time) / (60 * 60 * 24 * 1000)
                if (dDay + my_d_day > 0 && foodData.purchaseStatus == 1) {
                    dDayFoodList.add(Pair(foodData, dDay))
                }
            }
            var alarmFoodList = dDayFoodList.toList()
            alarmFoodList = alarmFoodList.sortedBy { pair -> pair.second } // d-day를 기준으로 sorting
            val foodNameList = alarmFoodList.map { pair -> pair.first.foodName }

            val alarmIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            var title = foodNameList.size.toString() + "개의 유통기한 임박 재료가 있어요!"
            var content = foodNameList.joinToString()
            var builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        }
    }
    private fun getNotificationIcon(): Int {
        val useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        return if (useWhiteIcon) {
            R.mipmap.ic_stat_ic_launcher
        } else {
            R.mipmap.ic_launcher
        }
    }
}