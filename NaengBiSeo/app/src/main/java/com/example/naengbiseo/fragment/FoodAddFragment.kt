package com.example.naengbiseo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class FoodAddFragment: Fragment() {
    private val TYPE_NO_ICON = -1
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

        add_food_btn.setOnClickListener {
            val food_name = food_edit_text.text.toString()
            val radio_btn_id = radio_group.checkedRadioButtonId
            if(food_name == null || food_name.isEmpty()) return@setOnClickListener
            when(radio_btn_id) {
                radio_btn1.id -> {
                    viewModel.insertData(FoodData(foodName=food_name, storeLocation = "shelf", foodIcon = TYPE_NO_ICON, purchaseStatus = 1)) // 음식 정보 저장
                    food_edit_text.setText("")
                }
                radio_btn2.id -> {
                    viewModel.insertData(FoodData(foodName=food_name, storeLocation ="cool", foodIcon = TYPE_NO_ICON, purchaseStatus = 1)) //Contacts 생성
                    food_edit_text.setText("")
                }
                radio_btn3.id -> {
                    viewModel.insertData(FoodData(foodName=food_name, storeLocation ="cold", foodIcon = TYPE_NO_ICON, purchaseStatus = 1)) //Contacts 생성
                    food_edit_text.setText("")
                }
            }
            findNavController().navigate(R.id.action_foodAddFragment_to_mainFragment)
        }
    }
}