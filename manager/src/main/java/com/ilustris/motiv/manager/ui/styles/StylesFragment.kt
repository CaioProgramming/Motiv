package com.ilustris.motiv.manager.ui.styles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.FragmentCoversBinding
import com.ilustris.motiv.manager.databinding.FragmentStylesBinding
import com.ilustris.motiv.manager.databinding.StylesRecyclerBinding

class StylesFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.styles_recycler, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            StyleRecyclerBinder(StylesRecyclerBinding.bind(view)).initView()
        }
    }
}