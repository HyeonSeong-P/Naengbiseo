package com.example.naengbiseo.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naengbiseo.FoodIcon
import com.example.naengbiseo.MainActivity
import com.example.naengbiseo.R
import com.example.naengbiseo.adapter.*
import com.example.naengbiseo.room.AppDatabase
import com.example.naengbiseo.room.FoodDataRepository
import com.example.naengbiseo.viewmodel.BasketViewModel
import com.example.naengbiseo.viewmodel.BasketViewModelFactory
import kotlinx.android.synthetic.main.fragment_shopping_cart.*

class ShoppingCartFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var verticalViewAdapter = ShoppingCartViewAdapter(mutableListOf())
    private var horizontalViewAdapter = ShoppingCartHorizontalAdapter(mutableListOf())
    private val iconList = mutableListOf<FoodIcon>()
    private val selectedIcon = mutableListOf<FoodIcon>()
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
        val ac=activity as MainActivity
        val dao = AppDatabase.getInstance(ac).foodDao()
        val repository = FoodDataRepository.getInstance(dao)
        val factory = BasketViewModelFactory(repository)
        var viewModel = ViewModelProvider(requireParentFragment(), factory).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            BasketViewModel::class.java)

        iconList.add(FoodIcon("과일류", TYPE_CATEGORY_HEADER))
        iconList.add(FoodIcon("포도", R.drawable.grape))
        iconList.add(FoodIcon("딸기", R.drawable.strawberry))
        iconList.add(FoodIcon("배", R.drawable.pear))
        iconList.add(FoodIcon("귤", R.drawable.mandarin))
        iconList.add(FoodIcon("사과", R.drawable.apple))
        iconList.add(FoodIcon("footer", TYPE_CATEGORY_FOOTER))
        iconList.add(FoodIcon("채소류", TYPE_CATEGORY_HEADER))
        iconList.add(FoodIcon("브로콜리", R.drawable.broccoli))
        iconList.add(FoodIcon("호박", R.drawable.pumpkin))
        iconList.add(FoodIcon("토마토", R.drawable.tomato))
        iconList.add(FoodIcon("양파", R.drawable.onion))
        iconList.add(FoodIcon("footer", TYPE_CATEGORY_FOOTER))

        verticalViewAdapter.iconList = iconList
        verticalRecyclerViewInShoppingCart.adapter = verticalViewAdapter
        horizontalRecyclerViewInShoppingCart.adapter = horizontalViewAdapter

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
        verticalRecyclerViewInShoppingCart.layoutManager = gridLayoutManager
        horizontalRecyclerViewInShoppingCart.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        shoppingCartSearchButton.setOnClickListener {
            Log.d("MSG","Button clicked")
        }

        addIconsToBasketButton.setOnClickListener {
            findNavController().navigate(R.id.action_shoppingCartFragment_to_basketFragment)
            viewModel.setIcon(selectedIcon)
        }

        searchIconEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                searching(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        (verticalRecyclerViewInShoppingCart.adapter as ShoppingCartViewAdapter).setItemClickListener(object : ShoppingCartViewAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                if (iconList[position].iconResource > 1) { // header, footer는 추가하면 안됨
                    selectedIcon.add(iconList[position])
                    horizontalViewAdapter.iconList = selectedIcon
                    horizontalViewAdapter.notifyDataSetChanged()
                }
            }
        })

        (horizontalRecyclerViewInShoppingCart.adapter as ShoppingCartHorizontalAdapter).setItemClickListener(object : ShoppingCartHorizontalAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                selectedIcon.removeAt(position)
                horizontalViewAdapter.iconList = selectedIcon
                horizontalViewAdapter.notifyDataSetChanged()
            }
        })
    }

    fun searching(text: String) {
        var data = iconList
        var searchedData = data.filter { it.iconName == text }
        if (text.isEmpty()) {
            verticalViewAdapter.iconList = iconList
            verticalViewAdapter.notifyDataSetChanged()
        } else {
            verticalViewAdapter.iconList = searchedData.toMutableList()
            verticalViewAdapter.notifyDataSetChanged()
        }
    }
}