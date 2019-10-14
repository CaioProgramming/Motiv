package com.creat.motiv.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.creat.motiv.Fragments.FavoritesFragment
import com.creat.motiv.Fragments.HomeFragment
import com.creat.motiv.Fragments.ProfileFragment

class MainAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return HomeFragment() //ChildFragment1 at position 0
            1 -> return FavoritesFragment() //ChildFragment2 at position 1
            2 -> return ProfileFragment() //ChildFragment3 at position 2
        }
        return HomeFragment()//does not happen
    }

    override fun getCount(): Int {
        return 3 //three fragments
    }
}
