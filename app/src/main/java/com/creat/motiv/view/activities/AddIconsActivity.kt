package com.creat.motiv.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.creat.motiv.R
import com.creat.motiv.profile.view.binders.AddIconBinder
import kotlinx.android.synthetic.main.title_layout.*


class AddIconsActivity : AppCompatActivity(R.layout.activity_add_icons) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AddIconBinder(this, DataBindingUtil.setContentView(this, R.layout.activity_add_icons))
        collapseTitle.text = "Adicionar ícones"

    }


}