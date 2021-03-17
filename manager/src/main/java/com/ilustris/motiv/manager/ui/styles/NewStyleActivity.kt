package com.ilustris.motiv.manager.ui.styles

import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.FragmentStylesBinding

class NewStyleActivity : AppCompatActivity(R.layout.fragment_styles) {


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        DataBindingUtil.setContentView<FragmentStylesBinding>(this, R.layout.fragment_styles).run {
            NewStyleBinder(this, supportFragmentManager).initView()
        }
    }
}