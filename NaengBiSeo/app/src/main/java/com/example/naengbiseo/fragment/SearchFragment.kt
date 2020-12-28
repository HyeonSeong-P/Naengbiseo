package com.example.naengbiseo.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.naengbiseo.MainActivity
import com.example.naengbiseo.R
import com.example.naengbiseo.adapter.SearchViewAdapter
import com.example.naengbiseo.room.AppDatabase
import com.example.naengbiseo.room.ExcelDataRepository
import com.example.naengbiseo.room.FoodDataRepository
import com.example.naengbiseo.viewmodel.MainViewModel
import com.example.naengbiseo.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.food_item_search_version.*
import kotlinx.android.synthetic.main.food_item_search_version.view.*
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment: Fragment() {
    private lateinit var callback: OnBackPressedCallback
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
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


        search_recyclerview.adapter = SearchViewAdapter(viewModel)
        search_recyclerview.layoutManager = LinearLayoutManager(activity)

        viewModel.search_data.observe(viewLifecycleOwner, Observer {
            (search_recyclerview.adapter as SearchViewAdapter).notifyDataSetChanged()
        })

        (search_recyclerview.adapter as SearchViewAdapter).setItemClickListener(object : SearchViewAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                var location_text:String = ""
                Log.d("s", "클릭")
                when(v.slt.text.toString()){
                    "선반" ->{
                        location_text = "shelf"
                    }
                    "냉장" ->{
                        location_text = "cool"
                    }
                    "냉동" ->{
                        location_text = "cold"
                    }
                }
                viewModel.setCompareData(
                    v.food_name.text.toString(),
                    location_text,
                    v.buy_date.text.toString()
                )
                findNavController().navigate(R.id.itemStatusFragment)
            }
        })


        search_food_edit_text.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.setSearchData(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                search_food_edit_text.getText().clear()
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}