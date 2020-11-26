package com.example.naengbiseo.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.naengbiseo.MainActivity
import com.example.naengbiseo.R
import com.example.naengbiseo.room.AppDatabase
import com.example.naengbiseo.room.FoodData
import com.example.naengbiseo.room.FoodDataRepository
import com.example.naengbiseo.viewmodel.FoodAddViewModel
import com.example.naengbiseo.viewmodel.FoodAddViewModelFactory
import com.example.naengbiseo.viewmodel.MainViewModel
import com.example.naengbiseo.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_food_add.*
import java.lang.NumberFormatException
import java.util.*

class FoodAddFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food_add, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac=activity as MainActivity
        val dao = AppDatabase.getInstance(ac).foodDao()
        val repository = FoodDataRepository.getInstance(dao)
        val factory = FoodAddViewModelFactory(repository)
        var viewModel = ViewModelProviders.of(this, factory).get(
            FoodAddViewModel::class.java)


        go_to_select_button.setOnClickListener {
            findNavController().navigate(R.id.action_foodAddFragment_to_foodIconAddFragment)
        }
        add_food_btn.setOnClickListener {
            val food_name = food_edit_text.text.toString()
            val food_number_text = food_number_edit_text.text.toString()
            val expiration_date = expiration_date_text.text.toString()
            val purchase_date=purchase_date_text.text.toString()
            val memo=memo_edit_text.text.toString()

            val radio_btn_id = radio_group.checkedRadioButtonId

            if(food_name == null || food_name.isEmpty() || food_number_text == null || food_number_text.isEmpty()
                || expiration_date == null || expiration_date.isEmpty() || purchase_date == null || purchase_date.isEmpty()
                    ) {
                Toast.makeText(activity as MainActivity,"필수정보를 입력해주세요!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try{
                val food_number = food_number_text.toInt()
                when(radio_btn_id) {
                    radio_btn1.id -> {
                        viewModel.insertData(FoodData(foodName=food_name, storeLocation = "shelf",foodNumber = food_number,buyDate = purchase_date,expirationDate = expiration_date,foodMemo = memo) ) // 음식 정보 저장
                        food_edit_text.setText("")
                    }
                    radio_btn2.id -> {
                        viewModel.insertData(FoodData(foodName=food_name, storeLocation = "cool",foodNumber = food_number,buyDate = purchase_date,expirationDate = expiration_date,foodMemo = memo)) //Contacts 생성
                        food_edit_text.setText("")
                    }
                    radio_btn3.id -> {
                        viewModel.insertData(FoodData(foodName=food_name, storeLocation = "cold",foodNumber = food_number,buyDate = purchase_date,expirationDate = expiration_date,foodMemo = memo)) //Contacts 생성
                        food_edit_text.setText("")
                    }
                    else -> {
                        Toast.makeText(activity as MainActivity,"음식을 보관할 장소를 선택해주세요!!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
            } catch(e:NumberFormatException){
                Toast.makeText(activity as MainActivity,"수량에는 숫자를 입력해주세요!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            findNavController().navigate(R.id.action_foodAddFragment_to_mainFragment)
        }

        purchase_button.setOnClickListener{
            val cal1 = Calendar.getInstance()
            DatePickerDialog(activity as MainActivity, DatePickerDialog.OnDateSetListener { datePicker, y, m, d->
                var M= m+1
                purchase_date_text.text="$y"+"년 "+"$M"+"월 "+"$d"+"일" }, // 이상하게 월은 0월부터네.. +1 해주자
                cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DATE)).show()
        }

        expiration_button.setOnClickListener {
            val cal2 = Calendar.getInstance()
            DatePickerDialog(activity as MainActivity, DatePickerDialog.OnDateSetListener { datePicker, y, m, d->
                var M= m+1
                expiration_date_text.text="$y"+"년 "+"$M"+"월 "+"$d"+"일" },
                cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DATE)).show()
        }

    }
}