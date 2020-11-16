package com.example.naengbiseo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.host_activity.*
import kotlinx.android.synthetic.main.layout_main_drawer.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host_activity)

        buttonEvent()
        initViewFinal()

    }
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                findNavController(R.id.nav_host_fragment).navigateUp() // 뒤로가기(이전화면으로)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
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
        menu_button.setOnClickListener{
            main_drawer_layout.openDrawer(Gravity.RIGHT)
        }
        x_button.setOnClickListener{
            main_drawer_layout.closeDrawers()
        }
        button_basket.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.basketFragment) //nav_host_fragment 에서 subFragment1로
            main_drawer_layout.closeDrawers()
        }
        button_trash.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.trashFragment) //nav_host_fragment 에서 subFragment1로
            main_drawer_layout.closeDrawers()
        }
        button_setting.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.settingFragment) //nav_host_fragment 에서 subFragment1로
            main_drawer_layout.closeDrawers()
        }
    }


}
