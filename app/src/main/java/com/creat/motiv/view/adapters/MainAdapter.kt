package com.creat.motiv.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.creat.motiv.view.fragments.HomeFragment
import com.creat.motiv.view.fragments.NewQuoteFragment
import com.creat.motiv.view.fragments.ProfileFragment

class MainAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> NewQuoteFragment()
            else -> ProfileFragment()
        }


    }

    override fun getCount(): Int {
        return 3//three fragments
    }
}
