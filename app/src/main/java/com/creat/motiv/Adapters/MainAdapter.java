package com.creat.motiv.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.creat.motiv.Fragments.FavoritesFragment;
import com.creat.motiv.Fragments.HomeFragment;
import com.creat.motiv.Fragments.ProfileFragment;

public class MainAdapter extends FragmentPagerAdapter {
    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment(); //ChildFragment1 at position 0
            case 1:
                return new FavoritesFragment(); //ChildFragment2 at position 1
            case 2:
                return new ProfileFragment(); //ChildFragment3 at position 2
        }
        return null; //does not happen
    }

    @Override
    public int getCount() {
        return 3; //three fragments
    }
}
