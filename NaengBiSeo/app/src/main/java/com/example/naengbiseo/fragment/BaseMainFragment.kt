package com.example.naengbiseo.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.naengbiseo.MainActivity
import com.example.naengbiseo.R
import com.example.naengbiseo.adapter.FoodViewAdapter
import com.example.naengbiseo.adapter.FoodViewHolder
import com.example.naengbiseo.adapter.ViewPagerAdapter2
import com.example.naengbiseo.room.AppDatabase
import com.example.naengbiseo.room.FoodDataRepository
import com.example.naengbiseo.viewmodel.FoodAddViewModel
import com.example.naengbiseo.viewmodel.FoodAddViewModelFactory
import com.example.naengbiseo.viewmodel.MainViewModel
import com.example.naengbiseo.viewmodel.MainViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.food_item.view.*
import kotlinx.android.synthetic.main.fragment_base_main.*
import kotlinx.android.synthetic.main.host_activity.*

class BaseMainFragment :Fragment(){
    private val tabTextList = arrayListOf("선반", "냉장실", "냉동실")
    var trashcan_state = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_base_main, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity // 프래그먼트에서 액티비티 접근하는 법 꼭 기억하자!!!!
        val dao = AppDatabase.getInstance(mainActivity).foodDao()
        val repository = FoodDataRepository.getInstance(dao)
        val factory = MainViewModelFactory(repository)
        var viewModel = ViewModelProviders.of(activity as MainActivity, factory).get(
            MainViewModel::class.java)

        trashcan_state = 0

        // 아 이게 각 프래그먼트마다 notify를 통해 화면 갱신을 해주니 여기서 일일이 옵저브 할 필요가 없네!!
        // 왜냐면 여기도 뷰모델 온트래쉬버튼(트래시 버튼 이벤트를 call 함)을 이용해 알려주기 때문!
        trashcan_btn.setOnClickListener {
            if(trashcan_state == 0) {
                trashcan_btn.setBackgroundColor(Color.parseColor("#ff0000"))
                viewModel.onTrashButton(1)
                trashcan_state = 1
            }
            else {
                if(viewModel.getDelData()==null) Log.d("b","비었어 ")
                viewModel.deleteData()
                trashcan_state = 0
                viewModel.onTrashButton(0)
                viewModel.clearDelData()
                trashcan_btn.setBackgroundColor(Color.parseColor("#ffffff"))
            }
        }

        view_pager_main.adapter =
            ViewPagerAdapter2(
                childFragmentManager,
                lifecycle
            )
        view_pager_main.offscreenPageLimit = 2 //프래그먼트 깨지는거 방지
        TabLayoutMediator(mainActivity.tabLayout, view_pager_main) { //탭레이아웃과 뷰페이저 연결
                tab, position ->
            //view_pager_main.currentItem = args.a1.toString().toInt()
            tab.text = tabTextList[position]
        }.attach()
        //view_pager_main.currentItem = args.ar.toInt();
        view_pager_main.setCurrentItem(1, false)


        go_to_add_item_page_btn.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_foodAddFragment)
        }
        go_to_shopping_cart_page_btn.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_shoppingCartFragment)
        }
    }
}