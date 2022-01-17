package com.ilustris.motiv.manager.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilustris.motiv.base.utils.activity
import com.ilustris.motiv.base.utils.hideSupportActionBar
import com.ilustris.motiv.base.utils.showSupportActionBar
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.FragmentManagerHomeBinding

class HomeFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manager_home, container, false).rootView
    }

}