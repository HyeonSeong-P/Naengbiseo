package com.example.naengbiseo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.MainActivity
import com.example.naengbiseo.R
import com.example.naengbiseo.adapter.BasketViewAdapter
import com.example.naengbiseo.adapter.ShoppingCartViewAdapter
import com.example.naengbiseo.room.AppDatabase
import com.example.naengbiseo.room.FoodDataRepository
import com.example.naengbiseo.viewmodel.BasketViewModel
import com.example.naengbiseo.viewmodel.BasketViewModelFactory
import com.example.naengbiseo.viewmodel.FoodAddViewModel
import com.example.naengbiseo.viewmodel.FoodAddViewModelFactory
import kotlinx.android.synthetic.main.fragment_basket.*

class BasketFragment :Fragment(){
    private var viewAdapter = BasketViewAdapter(mutableListOf())
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
        val dao = AppDatabase.getInstance(ac).foodDao()
        val repository = FoodDataRepository.getInstance(dao)
        val factory = BasketViewModelFactory(repository)
        var viewModel = ViewModelProvider(requireParentFragment(), factory).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            BasketViewModel::class.java)

        RecyclerViewInBasketFragment.adapter = viewAdapter
        RecyclerViewInBasketFragment.layoutManager = LinearLayoutManager(activity)

        viewModel.icons_data.observe(viewLifecycleOwner, Observer{
            Log.d("MSG","icons_data changed")
            viewAdapter.iconList = it
        })
    }
}