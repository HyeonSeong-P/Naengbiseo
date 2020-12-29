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
import com.example.naengbiseo.room.ExcelDataRepository
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
import java.text.SimpleDateFormat
import java.util.*

class ShelfFragment : Fragment(){
    var simpleFormat: SimpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
    var simpleFormat2:SimpleDateFormat = SimpleDateFormat("yyyy. MM. dd")
    var realBuyDate: Date? = null
    lateinit var dateString:String
    var sel:Int=0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shelf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity // 프래그먼트에서 액티비티 접근하는 법 꼭 기억하자!!!!
        val dao1 = AppDatabase.getInstance(mainActivity).foodDao()
        val dao2 = AppDatabase.getInstance(mainActivity).excelDao()
        val repository1 = FoodDataRepository.getInstance(dao1)
        val repository2 = ExcelDataRepository.getInstance(dao2)
        val factory = MainViewModelFactory(repository1, repository2)
        var viewModel = ViewModelProviders.of(activity as MainActivity, factory).get(
            MainViewModel::class.java)
        //viewModel.del_data.observe(viewLifecycleOwner, Observer{}) 없어도 되네?

        //viewModel.initSortData()
        viewModel.sort_shelf_data.observe(viewLifecycleOwner, Observer {
            Log.d("a","반응 왔다")
            viewModel.sort()

            (search_recyclerview_shelf.adapter as FoodViewAdapter).notifyDataSetChanged()
        })
        viewModel.allFoodData.observe(viewLifecycleOwner, Observer{
            viewModel.sort()
            (search_recyclerview_shelf.adapter as FoodViewAdapter).notifyDataSetChanged()
        }) // 버튼안에 옵저브를 안넣더라도 항상 옵저브하고 있어야 room 의 userdata 를 쓸수 있다,

        //adapter 추가
        search_recyclerview_shelf.adapter =
            FoodViewAdapter(viewModel,0)
        //레이아웃 매니저 추가
        search_recyclerview_shelf.layoutManager = LinearLayoutManager(activity)

        (search_recyclerview_shelf.adapter as FoodViewAdapter).setItemClickListener(object : FoodViewAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {

//                    Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                if(v.id == R.id.food_item_layout){
                    if(v.buy_date.text.toString() == "재료 정보를 기입해주세요"){
                        realBuyDate = simpleFormat2.parse("1111. 11. 11")
                        dateString = simpleFormat.format(realBuyDate)
                    }
                    else{
                        realBuyDate = simpleFormat2.parse(v.buy_date.text.toString())
                        dateString = simpleFormat.format(realBuyDate)
                    }
                    if (sel == 1) {
                        v.check_box.toggle()
                        if (v.check_box.isChecked) {
                            viewModel.addDelData(v.food_name.text.toString(),"shelf",dateString)
                        } else {
                            viewModel.removeDelData(v.food_name.text.toString(),"shelf",dateString)
                        }
                    }
                    else{
                        if(v.buy_date.text.toString() == "재료 정보를 기입해주세요"){
                            viewModel.setCompareData(
                                v.food_name.text.toString(),
                                "shelf",
                                "1111. 11. 11"
                            )
                        }
                        else{
                            viewModel.setCompareData(
                                v.food_name.text.toString(),
                                "shelf",
                                v.buy_date.text.toString()
                            )
                        }
                        findNavController().navigate(R.id.itemStatusFragment)
                    }
                }

            }
        })
        viewModel.trash_button_shelf_event.observe(viewLifecycleOwner, Observer{

            if(it==1){
                sel=1
                FoodViewHolder.activateCheckbox()
                (search_recyclerview_shelf.adapter as FoodViewAdapter).notifyDataSetChanged()
            }
            else{
                sel=0
                FoodViewHolder.inActivateCheckbox()
                (search_recyclerview_shelf.adapter as FoodViewAdapter).notifyDataSetChanged()
            }
        })
    }

}