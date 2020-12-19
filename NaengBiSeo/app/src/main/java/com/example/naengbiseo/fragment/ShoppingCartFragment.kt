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
import com.example.naengbiseo.room.FoodData
import com.example.naengbiseo.room.FoodDataRepository
import com.example.naengbiseo.viewmodel.BasketViewModel
import com.example.naengbiseo.viewmodel.BasketViewModelFactory
import kotlinx.android.synthetic.main.fragment_shopping_cart.*

class ShoppingCartFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var verticalViewAdapter = ShoppingCartViewAdapter(mutableListOf())
    private var horizontalViewAdapter = ShoppingCartHorizontalAdapter(mutableListOf())
    private val selectedIconList = mutableListOf<FoodIcon>()
    var sel:Int=0
    companion object { // static 변수처럼 사용됨. 앱이 실행될 때 딱 한 번 초기화
        private val TYPE_CATEGORY_HEADER = 0
        private val TYPE_CATEGORY_FOOTER = 1
        private val allIconList = mutableListOf<FoodIcon>(
            FoodIcon("직접입력", TYPE_CATEGORY_HEADER),
            FoodIcon("육류", R.drawable.meat),
            FoodIcon("과일류", R.drawable.fruit),
            FoodIcon("채소류", R.drawable.vegetable),
            FoodIcon("해산물", R.drawable.seafood),
            FoodIcon("곡류/견과류", R.drawable.nuts),
            FoodIcon("유제품/난류", R.drawable.dairy),
            FoodIcon("면류", R.drawable.noodle),
            FoodIcon("통조림", R.drawable.can),
            FoodIcon("냉동식품", R.drawable.frozen_food),
            FoodIcon("간편조리식품", R.drawable.convenience_food),
            FoodIcon("가열조리식품", R.drawable.boiling_food),
            FoodIcon("잼/양념/오일류", R.drawable.jam_seasoning_oil),
            FoodIcon("음료/제과류", R.drawable.beverage_snack),
            FoodIcon("건강식품", R.drawable.health_food),
            FoodIcon("과일류", TYPE_CATEGORY_HEADER),
            FoodIcon("포도", R.drawable.grape),
            FoodIcon("딸기", R.drawable.strawberry),
            FoodIcon("배", R.drawable.pear),
            FoodIcon("귤", R.drawable.mandarin),
            FoodIcon("사과", R.drawable.apple),
            FoodIcon("footer", TYPE_CATEGORY_FOOTER),
            FoodIcon("채소류", TYPE_CATEGORY_HEADER),
            FoodIcon("브로콜리", R.drawable.broccoli),
            FoodIcon("호박", R.drawable.pumpkin),
            FoodIcon("토마토", R.drawable.tomato),
            FoodIcon("양파", R.drawable.onion),
            FoodIcon("footer", TYPE_CATEGORY_FOOTER)
        )
        private val iconList = mutableListOf<FoodIcon>()
    }
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

        iconList.clear() // iconList가 검색으로 인해서 바껴있을 수도 있기때문에 원소를 추가하기 전 clear부터
        iconList.addAll(allIconList)

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

        addIconsToBasketButton.setOnClickListener { // 등록하기 버튼 - 누를시 장바구니 페이지로 이동
            viewModel.insertData(selectedIconList)
            findNavController().navigate(R.id.action_shoppingCartFragment_to_basketFragment)
//            viewModel.setIcon(selectedIconList)
        }

        (verticalRecyclerViewInShoppingCart.adapter as ShoppingCartViewAdapter).setItemClickListener(object : ShoppingCartViewAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                /*Log.d("MSG", "click position: " + position)
                Log.d("MSG", "iconName: " + iconList[position].iconName)
                Log.d("MSG", "iconResource: " + iconList[position].iconResource)*/
                if (iconList[position].iconResource > 1) { // header, footer는 추가하면 안됨
                    selectedIconList.add(iconList[position])
                    horizontalViewAdapter.iconList = selectedIconList
                    horizontalViewAdapter.notifyDataSetChanged()
                }
            }
        })

        (horizontalRecyclerViewInShoppingCart.adapter as ShoppingCartHorizontalAdapter).setItemClickListener(object : ShoppingCartHorizontalAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                selectedIconList.removeAt(position)
                horizontalViewAdapter.iconList = selectedIconList
                horizontalViewAdapter.notifyDataSetChanged()
            }
        })

        searchIconEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                searching(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    fun searching(text: String) {
        var data = allIconList
        var searchedData = data.filter { it.iconName.contains(text) && it.iconResource > 1 }
        iconList.clear()
        if (text.isEmpty()) {
            iconList.addAll(allIconList)
            verticalViewAdapter.iconList = iconList
            verticalViewAdapter.notifyDataSetChanged()
        } else {
            iconList.addAll(searchedData.toMutableList())
            verticalViewAdapter.iconList = searchedData.toMutableList()
            verticalViewAdapter.notifyDataSetChanged()
        }
    }
}