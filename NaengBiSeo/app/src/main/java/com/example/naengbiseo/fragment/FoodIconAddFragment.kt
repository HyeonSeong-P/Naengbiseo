package com.example.naengbiseo.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.naengbiseo.FoodIcon
import com.example.naengbiseo.MainActivity
import com.example.naengbiseo.R
import com.example.naengbiseo.adapter.FoodViewAdapter
import com.example.naengbiseo.adapter.ShoppingCartViewAdapter
import com.example.naengbiseo.adapter.ShoppingCartViewHolder
import com.example.naengbiseo.room.AppDatabase
import com.example.naengbiseo.room.FoodDataRepository
import com.example.naengbiseo.viewmodel.FoodAddViewModel
import com.example.naengbiseo.viewmodel.FoodAddViewModelFactory
import kotlinx.android.synthetic.main.fragment_food_icon_add.*
import kotlinx.android.synthetic.main.fragment_shopping_cart.*
import kotlinx.android.synthetic.main.fragment_shopping_cart.search_recyclerview_shopping_cart

class FoodIconAddFragment : Fragment() {

    var selectList = mutableListOf<Boolean>()
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

        //val viewModel: FoodAddViewModel by viewModels(ownerProducer = { requireParentFragment() })
        val ac=activity as MainActivity
        val dao = AppDatabase.getInstance(ac).foodDao()
        val repository = FoodDataRepository.getInstance(dao)
        val factory = FoodAddViewModelFactory(repository)
        var viewModel = ViewModelProvider(requireParentFragment(), factory).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            FoodAddViewModel::class.java)


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

        for (i in 0..iconList.size) {
            selectList.add(false)
        }

        search_recyclerview_shopping_cart.adapter = ShoppingCartViewAdapter(iconList)

        (search_recyclerview_shopping_cart.adapter as ShoppingCartViewAdapter).setItemClickListener(
            object : ShoppingCartViewAdapter.OnItemClickListener {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onClick(v: View, position: Int) {
                    if (selectList[position] == false) {
                        if (findTrue()) {
                            Toast.makeText(
                                activity as MainActivity,
                                "이미 재료를 선택하셨습니다!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                        viewModel.setIcon(iconList[position].iconResource,iconList[position].iconName)
                        v.elevation = 10F
                        selectList[position] = true
                    } else {
                        v.elevation = 0F
                        selectList[position] = false
                    }
                }
            })

        // 열을 3으로 설정한 GridLayoutManager 의 인스턴스를 생성하고 설정
        val gridLayoutManager = GridLayoutManager(activity, 4)

        // SpanSizeLookup 으로 위치별로 차지할 폭을 결정한다
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // 카페고리 header 나 footer 영역은 grid 4칸을 전부 차지하여 표시
                if (iconList[position].getIconResource == TYPE_CATEGORY_HEADER ||
                    iconList[position].getIconResource == TYPE_CATEGORY_FOOTER
                ) {
                    return gridLayoutManager.spanCount
                }
                return 1 // 나머지는 1칸만 사용
            }
        }
        //레이아웃 매니저 추가
        search_recyclerview_shopping_cart.layoutManager = gridLayoutManager

        add_food_icon_btn.setOnClickListener{
            if(!findTrue()){
                Toast.makeText(
                    activity as MainActivity,
                    "추가할 재료를 선택해주세요!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            else findNavController().navigate(R.id.action_foodIconAddFragment_to_foodAddFragment)
        }
    }



    fun findTrue(): Boolean {
        var flag = false
        for (b in selectList) {
            if (b == true) flag = true
        }
        return flag
    }
}