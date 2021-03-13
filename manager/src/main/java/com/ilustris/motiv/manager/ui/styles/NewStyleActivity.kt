package com.ilustris.motiv.manager.ui.styles

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.FragmentStylesBinding

class NewStyleActivity : AppCompatActivity(R.layout.fragment_styles) {


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
         DataBindingUtil.setContentView<FragmentStylesBinding>(this, R.layout.fragment_styles).run {
            StyleBinder(this, supportFragmentManager).initView()
        }
    }
}