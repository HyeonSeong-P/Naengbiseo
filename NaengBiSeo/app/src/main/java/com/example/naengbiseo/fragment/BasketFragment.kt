package com.example.naengbiseo.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.FoodIcon
import com.example.naengbiseo.MainActivity
import com.example.naengbiseo.R
import com.example.naengbiseo.adapter.BasketViewAdapter
import com.example.naengbiseo.adapter.FoodViewAdapter
import com.example.naengbiseo.adapter.ShoppingCartViewAdapter
import com.example.naengbiseo.room.AppDatabase
import com.example.naengbiseo.room.ExcelDataRepository
import com.example.naengbiseo.room.FoodDataRepository
import com.example.naengbiseo.viewmodel.BasketViewModel
import com.example.naengbiseo.viewmodel.BasketViewModelFactory
import com.example.naengbiseo.viewmodel.FoodAddViewModel
import com.example.naengbiseo.viewmodel.FoodAddViewModelFactory
import kotlinx.android.synthetic.main.basket_food_item.*
import kotlinx.android.synthetic.main.basket_food_item.view.*
import kotlinx.android.synthetic.main.food_item.view.*
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlinx.android.synthetic.main.fragment_cold.*
import kotlinx.android.synthetic.main.frament_cool.*

class BasketFragment :Fragment(){
    private val iconList = mutableListOf<FoodIcon>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac=activity as MainActivity
        val dao1 = AppDatabase.getInstance(ac).foodDao()
        val dao2 = AppDatabase.getInstance(ac).excelDao()
        val repository1 = FoodDataRepository.getInstance(dao1)
        val repository2 = ExcelDataRepository.getInstance(dao2)
        val factory = BasketViewModelFactory(repository1, repository2)
        var viewModel = ViewModelProvider(requireParentFragment(), factory).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            BasketViewModel::class.java)
        var viewAdapter = BasketViewAdapter(viewModel)

        RecyclerViewInBasketFragment.adapter = viewAdapter
        RecyclerViewInBasketFragment.layoutManager = LinearLayoutManager(activity)

        // db가 변동될경우 실행됨
        viewModel.allFoodData.observe(viewLifecycleOwner, Observer{
            var foodListToPurchase = it.filter { it.purchaseStatus == 0 }
            foodListToPurchase = foodListToPurchase.sortedBy { foodData -> foodData.foodName }
            viewModel.basketFoodList = foodListToPurchase
            viewAdapter.notifyDataSetChanged()
            if (viewModel.basketFoodList.isEmpty()) {
                plusFoodBtn.visibility = View.INVISIBLE
            } else {
                plusFoodBtn.visibility = View.VISIBLE
            }
        })

        viewModel.isButtonClickedData.observe(viewLifecycleOwner, Observer{
            findNavController().navigate(R.id.action_basketFragment_to_shoppingCartFragment)
        })

        backButtonInBasketFragment.setOnClickListener {
            findNavController().popBackStack() // 이전 화면으로 이동
        }

        plusFoodBtn.setOnClickListener {
            viewModel.foodAddButtonClicked(true)
        }

        (RecyclerViewInBasketFragment.adapter as BasketViewAdapter).setItemClickListener(object :
            BasketViewAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    Toast.makeText(context, "position: " + position, Toast.LENGTH_SHORT).show()
                }
        })
    }

    override fun onStop() { // back버튼, 홈버튼 누를 시에도 장바구니 최신 데이터를 db에 저장해야하기때문
        val ac=activity as MainActivity
        val dao1 = AppDatabase.getInstance(ac).foodDao()
        val dao2 = AppDatabase.getInstance(ac).excelDao()
        val repository1 = FoodDataRepository.getInstance(dao1)
        val repository2 = ExcelDataRepository.getInstance(dao2)
        val factory = BasketViewModelFactory(repository1, repository2)
        var viewModel = ViewModelProvider(requireParentFragment(), factory).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            BasketViewModel::class.java)
        for (foodData in viewModel.basketFoodList) { // 지금까지 수정했던 basketFoodList를 가지고 db수정
            viewModel.updateData(foodData)
        }
        super.onStop()
    }
}