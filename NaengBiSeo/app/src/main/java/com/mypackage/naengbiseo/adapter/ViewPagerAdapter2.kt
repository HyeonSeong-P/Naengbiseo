package com.trinity.naengbiseo.adapter

import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.trinity.naengbiseo.fragment.ColdFragment
import com.trinity.naengbiseo.fragment.CoolFragment
import com.trinity.naengbiseo.fragment.ErrorFragment
import com.trinity.naengbiseo.fragment.ShelfFragment

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