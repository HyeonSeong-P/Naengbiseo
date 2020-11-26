package com.example.naengbiseo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.naengbiseo.FoodIcon
import com.example.naengbiseo.R
import com.example.naengbiseo.adapter.ShoppingCartViewAdapter
import kotlinx.android.synthetic.main.fragment_shopping_cart.*

class FoodIconAddFragment: Fragment() {
    private val iconList = mutableListOf<FoodIcon>()
    private val TYPE_CATEGORY_HEADER = 0
    private val TYPE_CATEGORY_FOOTER = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food_icon_add, container, false)
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