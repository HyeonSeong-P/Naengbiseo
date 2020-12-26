package com.example.naengbiseo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.MainActivity
import com.example.naengbiseo.R
import com.example.naengbiseo.adapter.FoodViewAdapter
import com.example.naengbiseo.adapter.FoodViewHolder
import com.example.naengbiseo.room.AppDatabase
import com.example.naengbiseo.room.FoodDataRepository
import com.example.naengbiseo.viewmodel.*
import kotlinx.android.synthetic.main.food_item.view.*
import kotlinx.android.synthetic.main.food_item.view.food_name
import kotlinx.android.synthetic.main.fragment_cold.*
import kotlinx.android.synthetic.main.fragment_food_add.view.*
import kotlinx.android.synthetic.main.fragment_shelf.*
import kotlinx.android.synthetic.main.frament_cool.*

class ColdFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var sel: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cold, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity // 프래그먼트에서 액티비티 접근하는 법 꼭 기억하자!!!!
        val dao = AppDatabase.getInstance(mainActivity).foodDao()
        val repository = FoodDataRepository.getInstance(dao)
        val factory = MainViewModelFactory(repository)
        var viewModel = ViewModelProviders.of(activity as MainActivity, factory).get(
            MainViewModel::class.java
        )
//        viewModel.del_data.observe(viewLifecycleOwner, Observer{})
        /*val statusFactory = ItemStatusViewModelFactory(repository)
        var statusViewModel = ViewModelProvider(
            requireParentFragment(),
            statusFactory
        ).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            ItemStatusViewModel::class.java
        )*/

        viewModel.allFoodData.observe(viewLifecycleOwner, Observer {
            viewModel.sort()
            (search_recyclerview_cold.adapter as FoodViewAdapter).notifyDataSetChanged() // 이게 중요
        }) // 버튼안에 옵저브를 안넣더라도 항상 옵저브하고 있어야 room 의 userdata 를 쓸수 있다,



        //adapter 추가
        search_recyclerview_cold.adapter =
            FoodViewAdapter(viewModel, 2)
        //레이아웃 매니저 추가
        search_recyclerview_cold.layoutManager = LinearLayoutManager(activity)

        (search_recyclerview_cold.adapter as FoodViewAdapter).setItemClickListener(object :
            FoodViewAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
//                    Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                if (sel == 1) {
                    v.check_box.toggle()
                    if (v.check_box.isChecked) {
                        viewModel.addDelData(v.food_name.text.toString(),"cold",v.buy_date.text.toString())
                    } else {
                        viewModel.removeDelData(v.food_name.text.toString(),"cold",v.buy_date.text.toString())
                    }
                } else {
                    viewModel.setCompareData(
                        v.food_name.text.toString(),
                        "cold",
                        v.buy_date.text.toString()
                    )
                    findNavController().navigate(R.id.itemStatusFragment)
                }
            }
        })

        viewModel.trash_button_cold_event.observe(viewLifecycleOwner, Observer {
            if (it == 1) {
                sel = 1
                FoodViewHolder.activateCheckbox()
                (search_recyclerview_cold.adapter as FoodViewAdapter).notifyDataSetChanged()
            } else {
                sel = 0
                FoodViewHolder.inActivateCheckbox()
                (search_recyclerview_cold.adapter as FoodViewAdapter).notifyDataSetChanged()
            }

        })
    }
}