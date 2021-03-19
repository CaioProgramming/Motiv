package com.ilustris.motiv.manager.ui.styles

import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.NewStyleFormBinding

class NewStyleActivity : AppCompatActivity(R.layout.new_style_form) {


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        DataBindingUtil.setContentView<NewStyleFormBinding>(this, R.layout.new_style_form).run {
            NewStyleBinder(this, supportFragmentManager).initView()
        }
    }
}