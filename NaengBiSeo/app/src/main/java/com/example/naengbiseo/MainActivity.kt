package com.example.naengbiseo

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.naengbiseo.room.AppDatabase
import com.example.naengbiseo.room.FoodDao
import com.example.naengbiseo.room.FoodDataRepository
import com.example.naengbiseo.viewmodel.MainViewModel
import com.example.naengbiseo.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.host_activity.*
import kotlinx.android.synthetic.main.layout_main_drawer.*


class MainActivity : AppCompatActivity(),
    PopupMenu.OnMenuItemClickListener {
    lateinit var dao: FoodDao
    lateinit var repository: FoodDataRepository
    lateinit var factory: MainViewModelFactory
    lateinit var viewModel: MainViewModel
    lateinit var imm:InputMethodManager
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.host_activity)
        dao = AppDatabase.getInstance(this).foodDao()
        repository = FoodDataRepository.getInstance(dao)
        factory = MainViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, factory).get(
        MainViewModel::class.java)
        //searchIconEditText.focus



        viewModel.initSortData()
        sort_button.text = "구매순"
        buttonEvent()
        initViewFinal()

    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /*override fun onTouchEvent(event: MotionEvent?): Boolean {
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager // as로 타입 캐스팅
        imm.hideSoftInputFromWindow(searchIconEditText.windowToken,0)
        return true
    }*/
    //모듈화를 위해 분리 , 클래스안에 함수 매소드.
    fun initViewFinal() {
        setSupportActionBar(main_toolbar) // 전체화면에 메인 툴바를 넣겠다.

        val host = nav_host_fragment as NavHostFragment //우리가 만든것(nav_host_fragment)과 이미 있는것을 결합.nav_host_fragment 는 view,xml
        //NavHostFragment 는 클래스
        val navController = host.navController // 바로 윗줄 포함 두줄 필수. 네비게이션.xml에 접근위해.

        navController.addOnDestinationChangedListener{_, destination, _ ->
            // 화면이 바뀔때 키보드 무조건 숨김
            val dest: String = try{
                resources.getResourceName(destination.id)
            } catch (e: Exception){
                return@addOnDestinationChangedListener
            }
            handleToolbar(destination)
        } // 원랜 ({})

    }


    /*override fun onSupportNavigateUp(): Boolean {

        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }*/

    private fun handleToolbar(destination: NavDestination) { //툴바 표시할지 안할지
        supportActionBar?.setDisplayShowTitleEnabled(false) //타이틀 제목 없애기
        when (destination.id) { // 스위치 문과 동일.

            R.id.mainFragment-> {
                //이게 드로우어를 락 언락 정하는거
                main_drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                // 드로우어 툴바 쉽게 꺼내오게 서포트툴바
                supportActionBar?.show()
            }
            else -> {
                //이게 드로우어를 락 언락 정하는거
                main_drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                // 드로우어 툴바 쉽게 꺼내오게 서포트툴바
                supportActionBar?.hide() // 툴바 숨기기

            }

        }

    }

    private fun buttonEvent() {

        search_button.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.searchFragment)
        }
        sort_button.setOnClickListener {

            /*val popup = PopupMenu(this, sort_button)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.sort_menu, popup.menu)
            popup.show()*/

            PopupMenu(this, sort_button).apply {

                setOnMenuItemClickListener(this@MainActivity)
                inflate(R.menu.sort_menu)
                show()
            }
        }
        x_button.setOnClickListener{
            main_drawer_layout.closeDrawers()
        }

        go_to_basket_button.setOnClickListener{
            findNavController(R.id.nav_host_fragment).navigate(R.id.shoppingCartFragment)
        }
    }


    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.category_sort -> {
                Log.d("s","클릭")
                viewModel.setSortData(0)
                sort_button.text = "항목별"
            }
            R.id.buy_sort -> {
                Log.d("s","클릭")
                viewModel.setSortData(1)
                sort_button.text = "구매순"
            }
            R.id.expiration_sort -> {
                Log.d("s","클릭")
                viewModel.setSortData(2)
                sort_button.text = "유통기한 임박순"
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
