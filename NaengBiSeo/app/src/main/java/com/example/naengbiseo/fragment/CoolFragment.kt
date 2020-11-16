package com.example.naengbiseo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import kotlinx.android.synthetic.main.fragment_cold.*
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
        viewModel.del_data.observe(viewLifecycleOwner, Observer{
            for(s in it){
                Log.d("sd",s)
            }
        })
        // 화면 갱신이 문제다
        viewModel.allFoodData.observe(viewLifecycleOwner, Observer{
            // 여기 바로 밑에 구문들을 여기 옵저버 안에 넣어놔야 실시간으로 데이터 수정된다.
            //adapter 추가
            search_recyclerview_cool.adapter =
                FoodViewAdapter(viewModel,1)
            //레이아웃 매니저 추가
            search_recyclerview_cool.layoutManager = LinearLayoutManager(activity)

            (search_recyclerview_cool.adapter as FoodViewAdapter).setItemClickListener(object : FoodViewAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    Log.d("s","떳다2")
                    if (sel == 1) {
                        Log.d("s","떳다1")
                        v.check_box.toggle()
                        if (v.check_box.isChecked) {
                            var st="apfhd"
                            Log.d("s","추가"+v.food_name.text.toString())
                            viewModel.addDelData(v.food_name.text.toString())
                        } else {
                            Log.d("s","지움"+v.food_name.text.toString())
                            viewModel.removeDelData(v.food_name.text.toString())
                        }
                    }
                    else{
                        Log.d("s","떳다3")
                    }
                }
            })
        }) // 버튼안에 옵저브를 안넣더라도 항상 옵저브하고 있어야 room 의 userdata 를 쓸수 있다,

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