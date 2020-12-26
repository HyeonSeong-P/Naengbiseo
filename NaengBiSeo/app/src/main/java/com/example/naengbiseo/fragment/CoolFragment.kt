package com.example.naengbiseo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import com.example.naengbiseo.viewmodel.FoodAddViewModelFactory
import com.example.naengbiseo.viewmodel.MainViewModel
import com.example.naengbiseo.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.food_item.view.*
import kotlinx.android.synthetic.main.food_item.view.food_name
import kotlinx.android.synthetic.main.fragment_cold.*
import kotlinx.android.synthetic.main.fragment_food_add.view.*
import kotlinx.android.synthetic.main.fragment_shelf.*
import kotlinx.android.synthetic.main.frament_cool.*

class CoolFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var sel:Int=0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frament_cool, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity // 프래그먼트에서 액티비티 접근하는 법 꼭 기억하자!!!!
        val dao = AppDatabase.getInstance(mainActivity).foodDao()
        val repository = FoodDataRepository.getInstance(dao)
        val factory = MainViewModelFactory(repository)
        var viewModel = ViewModelProviders.of(activity as MainActivity, factory).get(
            MainViewModel::class.java)

        //viewModel.initSortData()
        viewModel.sort_data.observe(viewLifecycleOwner, Observer {
            Log.d("a","반응 왔다,냉장고에")
            viewModel.sort()
            (search_recyclerview_cool.adapter as FoodViewAdapter).notifyDataSetChanged()
        })
        viewModel.allFoodData.observe(viewLifecycleOwner, Observer{
            viewModel.sort()
            (search_recyclerview_cool.adapter as FoodViewAdapter).notifyDataSetChanged() // 화면 갱신에 사용
        }) // 버튼안에 옵저브를 안넣더라도 항상 옵저브하고 있어야 room 의 userdata 를 쓸수 있다,

        //adapter 추가
        search_recyclerview_cool.adapter =
            FoodViewAdapter(viewModel,1)
        //레이아웃 매니저 추가
        search_recyclerview_cool.layoutManager = LinearLayoutManager(activity)

        (search_recyclerview_cool.adapter as FoodViewAdapter).setItemClickListener(object : FoodViewAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                if(v.id == R.id.food_item_layout){
                    if (sel == 1) {
                        v.check_box.toggle()
                        if (v.check_box.isChecked) {
                            viewModel.addDelData(v.food_name.text.toString(),"cool",v.buy_date.text.toString())
                        } else {
                            viewModel.removeDelData(v.food_name.text.toString(),"cool",v.buy_date.text.toString())
                        }
                    }
                    else{
                        Log.d("s","클릭")
                        viewModel.setCompareData(
                            v.food_name.text.toString(),
                            "cool",
                            v.buy_date.text.toString()
                        )
                        findNavController().navigate(R.id.itemStatusFragment)
                    }
                }

            }
        })

        viewModel.trash_button_cool_event.observe(viewLifecycleOwner, Observer{
            if(it==1){
                sel=1
                FoodViewHolder.activateCheckbox()
                (search_recyclerview_cool.adapter as FoodViewAdapter).notifyDataSetChanged()
            }
            else{
                sel=0
                FoodViewHolder.inActivateCheckbox()
                (search_recyclerview_cool.adapter as FoodViewAdapter).notifyDataSetChanged()
            }


        })




    }
}