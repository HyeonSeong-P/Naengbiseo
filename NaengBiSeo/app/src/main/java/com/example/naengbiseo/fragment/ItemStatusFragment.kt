package com.example.naengbiseo.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.naengbiseo.MainActivity
import com.example.naengbiseo.R
import com.example.naengbiseo.adapter.FoodViewAdapter
import com.example.naengbiseo.room.AppDatabase
import com.example.naengbiseo.room.FoodData
import com.example.naengbiseo.room.FoodDataRepository
import com.example.naengbiseo.viewmodel.*
import kotlinx.android.synthetic.main.fragment_cold.*
import kotlinx.android.synthetic.main.fragment_food_add.*
import kotlinx.android.synthetic.main.fragmetn_item_status.*
import kotlinx.android.synthetic.main.fragmetn_item_status.back_button
import kotlinx.android.synthetic.main.fragmetn_item_status.expiration_date_text
import kotlinx.android.synthetic.main.fragmetn_item_status.food_edit_text
import kotlinx.android.synthetic.main.fragmetn_item_status.food_number_edit_text
import kotlinx.android.synthetic.main.fragmetn_item_status.go_to_select_button
import kotlinx.android.synthetic.main.fragmetn_item_status.purchase_date_text
import kotlinx.android.synthetic.main.fragmetn_item_status.memo_edit_text
import kotlinx.android.synthetic.main.fragmetn_item_status.purchase_button
import kotlinx.android.synthetic.main.fragmetn_item_status.expiration_button
import kotlinx.android.synthetic.main.fragmetn_item_status.radio_group
import kotlinx.android.synthetic.main.fragmetn_item_status.radio_btn1
import kotlinx.android.synthetic.main.fragmetn_item_status.radio_btn2
import kotlinx.android.synthetic.main.fragmetn_item_status.radio_btn3
import java.lang.NumberFormatException
import java.util.*


class ItemStatusFragment : Fragment() {
    var foodIcon: Int = 0
    var foodNum: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmetn_item_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity // 프래그먼트에서 액티비티 접근하는 법 꼭 기억하자!!!!
        val dao = AppDatabase.getInstance(mainActivity).foodDao()
        val repository = FoodDataRepository.getInstance(dao)
        val factory = ItemStatusViewModelFactory(repository)
        val factory2 = FoodAddViewModelFactory(repository)
        var viewModel2 = ViewModelProvider(
            requireParentFragment(),
            factory2
        ).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            FoodAddViewModel::class.java
        )

        var viewModel = ViewModelProviders.of(activity as MainActivity, factory).get(
            MainViewModel::class.java
        )



        numMinus_button.setOnClickListener {
            foodNum--
            food_number_edit_text.setText(foodNum.toString())
        }

        numPlus_button.setOnClickListener {
            foodNum++
            food_number_edit_text.setText(foodNum.toString())
        }


        viewModel2.icon_data.observe(viewLifecycleOwner, Observer {
            Log.d("sd", "변경됐냐?!!")
            foodIcon = it.first

            go_to_select_button.setImageResource(it.first)
            food_edit_text.setText(it.second)
        })

        var foodData: FoodData? = null

        viewModel.compare_data.observe(viewLifecycleOwner, Observer {
            foodData = viewModel.getFoodData()!!
            go_to_select_button.setImageResource(foodData!!.foodIcon)
            food_edit_text.setText(foodData!!.foodName)
            food_number_edit_text.setText(foodData!!.foodNumber.toString())
            purchase_date_text.setText(foodData!!.buyDate)
            expiration_date_text.setText(foodData!!.expirationDate)
            memo_edit_text.setText(foodData!!.foodMemo)
            foodNum = foodData!!.foodNumber
            when(foodData!!.storeLocation){
                "shelf" -> {
                    radio_group.check(radio_btn1.id)
                }
                "cool" -> {
                    radio_group.check(radio_btn2.id)
                }
                "cold" -> {
                    radio_group.check(radio_btn3.id)
                }
            }

        })

        back_button.setOnClickListener {
            val food_name = food_edit_text.text.toString()
            val expiration_date = expiration_date_text.text.toString()
            val purchase_date=purchase_date_text.text.toString()
            var memo= memo_edit_text.text.toString()

            val radio_btn_id = radio_group.checkedRadioButtonId

            foodData?.foodName = food_name
            foodData?.foodNumber = foodNum
            foodData?.buyDate = purchase_date
            foodData?.expirationDate = expiration_date
            foodData?.foodMemo = memo
            viewModel.updateData(foodData!!)
            when(radio_btn_id) {
                radio_btn1.id -> {
                    foodData?.storeLocation = "shelf"
                }
                radio_btn2.id -> {
                    foodData?.storeLocation = "cool"
                }
                radio_btn3.id -> {
                    foodData?.storeLocation = "cold"
                }
                else -> {
                    Toast.makeText(activity as MainActivity,"음식을 보관할 장소를 선택해주세요!!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            findNavController().navigateUp()
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
        youtube_button.setOnClickListener {
            val food_name = food_edit_text.text.toString()
            //var itemText=item_text.text.toString()
            var siteString="https://www.youtube.com/results?search_query="+food_name+"+레시피"
            webview.loadUrl(siteString)

        }
        recipe10000_button.setOnClickListener {
            val food_name = food_edit_text.text.toString()
            //var itemText=item_text.text.toString()
            var siteString="https://www.10000recipe.com/recipe/list.html?q="+food_name+"+레시피"
            webview.loadUrl(siteString)

        }
        google_button.setOnClickListener {
            val food_name = food_edit_text.text.toString()
            //var itemText=item_text.text.toString()
            var siteString="https://www.google.com/search?q="+food_name+"+레시피"
            webview.loadUrl(siteString)
        }

    }
}