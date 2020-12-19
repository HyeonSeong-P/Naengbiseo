package com.example.naengbiseo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        /*var statusViewModel = ViewModelProvider(
            requireParentFragment(),
            statusFactory
        ).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            ItemStatusViewModel::class.java
        )*/
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
            go_to_select_button.setImageResource(foodData!!.icon)
            food_edit_text.setText(foodData!!.foodName)
            food_number_edit_text.setText(foodData!!.foodNumber.toString())
            purchase_date_text.setText(foodData!!.buyDate)
            expiration_date_text.setText(foodData!!.expirationDate)

            foodNum = foodData!!.foodNumber

        })

        back_button.setOnClickListener {
            foodData?.foodName = food_edit_text.text.toString()
            viewModel.updateData(foodData!!)
            findNavController().navigateUp()
        }
        /*youtube_button.setOnClickListener {
            //var itemText=item_text.text.toString()
            var siteString="https://www.youtube.com/results?search_query="+itemText+"+레시피"
            webview.loadUrl(siteString)
            val bundle= bundleOf(
                "arg_site" to siteString
            )

        }
        recipe10000_button.setOnClickListener {
            //var itemText=item_text.text.toString()
            var siteString="https://www.10000recipe.com/recipe/list.html?q="+itemText+"+레시피"
            webview.loadUrl(siteString)
            val bundle= bundleOf(
                "arg_site" to siteString
            )

        }
        do_eat_button.setOnClickListener {
            //var itemText=item_text.text.toString()
            var siteString="https://www.haemukja.com/recipes?utf8=✓&sort=rlv&name="+itemText+"+레시피"
            webview.loadUrl(siteString)
            val bundle= bundleOf(
                "arg_site" to siteString
            )

        }*/

    }
}