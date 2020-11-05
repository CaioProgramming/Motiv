package com.creat.motiv.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.creat.motiv.R
import com.creat.motiv.databinding.ActivityPickIconBinding
import com.creat.motiv.model.beans.Pics
import com.creat.motiv.utilities.ADMIN_USER
import com.creat.motiv.utilities.SELECTED_ICON
import com.creat.motiv.view.binders.ProfilePicSelectBinder
import kotlinx.android.synthetic.main.title_layout.*

class PickIconActivity : AppCompatActivity(R.layout.activity_pick_icon) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val admin = intent.getBooleanExtra(ADMIN_USER, false)
        val pickIconBinding: ActivityPickIconBinding = DataBindingUtil.setContentView(this, R.layout.activity_pick_icon)
        ProfilePicSelectBinder(this, admin, pickIconBinding.iconBind, ::onSelectPick)
        collapseTitle.text = "Selecionar ícone de perfil"
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = ""
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

    }

    fun onSelectPick(pic: Pics) {
        val resultIntent = Intent()
        resultIntent.putExtra(SELECTED_ICON, pic)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

}

