package com.example.naengbiseo.adapter

import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.naengbiseo.fragment.ColdFragment
import com.example.naengbiseo.fragment.CoolFragment
import com.example.naengbiseo.fragment.ErrorFragment
import com.example.naengbiseo.fragment.ShelfFragment

class ViewPagerAdapter2(fm: FragmentManager,lifecycle: Lifecycle):
    FragmentStateAdapter(fm,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            0 -> ShelfFragment()
            1 -> CoolFragment()
            2 -> ColdFragment()
            else -> ErrorFragment()
        }
    }

}