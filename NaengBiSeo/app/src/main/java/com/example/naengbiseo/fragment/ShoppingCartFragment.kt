package com.example.naengbiseo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.FoodIcon
import com.example.naengbiseo.MainActivity
import com.example.naengbiseo.R
import com.example.naengbiseo.adapter.FoodViewAdapter
import com.example.naengbiseo.adapter.FoodViewHolder
import com.example.naengbiseo.adapter.ShoppingCartViewAdapter
import com.example.naengbiseo.room.AppDatabase
import com.example.naengbiseo.room.FoodDataRepository
import com.example.naengbiseo.viewmodel.MainViewModel
import com.example.naengbiseo.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.food_item.view.*
import kotlinx.android.synthetic.main.fragment_cold.*
import kotlinx.android.synthetic.main.fragment_shopping_cart.*

class ShoppingCartFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val iconList = mutableListOf<FoodIcon>()
    private val TYPE_CATEGORY_HEADER = 0
    private val TYPE_CATEGORY_FOOTER = 1
    var sel:Int=0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iconList.add(FoodIcon("과일류", TYPE_CATEGORY_HEADER))
        iconList.add(FoodIcon("포도", R.drawable.grape))
        iconList.add(FoodIcon("딸기", R.drawable.strawberry))
        iconList.add(FoodIcon("배", R.drawable.pear))
        iconList.add(FoodIcon("귤", R.drawable.mandarin))
        iconList.add(FoodIcon("footer", TYPE_CATEGORY_FOOTER))
        iconList.add(FoodIcon("채소류", TYPE_CATEGORY_HEADER))
        iconList.add(FoodIcon("브로콜리", R.drawable.broccoli))
        iconList.add(FoodIcon("호박", R.drawable.pumpkin))
        iconList.add(FoodIcon("토마토", R.drawable.tomato))
        iconList.add(FoodIcon("양파", R.drawable.onion))
        iconList.add(FoodIcon("footer", TYPE_CATEGORY_FOOTER))


        search_recyclerview_shopping_cart.adapter = ShoppingCartViewAdapter(iconList)

        // 열을 3으로 설정한 GridLayoutManager 의 인스턴스를 생성하고 설정
        val gridLayoutManager = GridLayoutManager(activity, 4)

        // SpanSizeLookup 으로 위치별로 차지할 폭을 결정한다
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // 카페고리 header 나 footer 영역은 grid 4칸을 전부 차지하여 표시
                if (iconList[position].getIconResource == TYPE_CATEGORY_HEADER ||
                    iconList[position].getIconResource == TYPE_CATEGORY_FOOTER) {
                    return gridLayoutManager.spanCount
                }
                return 1 // 나머지는 1칸만 사용
            }
        }
        //레이아웃 매니저 추가
        search_recyclerview_shopping_cart.layoutManager = gridLayoutManager
    }
}