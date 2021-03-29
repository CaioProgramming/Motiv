package com.ilustris.motiv.manager.ui.icons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilustris.motiv.base.utils.activity
import com.ilustris.motiv.base.utils.setMotivTitle
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.FragmentIconsBinding

class IconsFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_icons, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        IconsBinder(FragmentIconsBinding.bind(view), childFragmentManager).initView()
        context?.activity()?.setMotivTitle("Ícones")
    }
}