package com.ilustris.motiv.manager.ui.icons

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ilustris.motiv.manager.R


class AddIconsActivity : AppCompatActivity(R.layout.activity_add_icons) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AddIconBinder(this, DataBindingUtil.setContentView(this, R.layout.activity_add_icons))

    }


}