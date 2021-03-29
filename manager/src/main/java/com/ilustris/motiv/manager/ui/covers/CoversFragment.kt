package com.ilustris.motiv.manager.ui.covers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilustris.motiv.base.utils.activity
import com.ilustris.motiv.base.utils.hideSupportActionBar
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.FragmentCoversBinding

class CoversFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_covers, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoversBinder(FragmentCoversBinding.bind(view))
        context?.hideSupportActionBar()

    }
}