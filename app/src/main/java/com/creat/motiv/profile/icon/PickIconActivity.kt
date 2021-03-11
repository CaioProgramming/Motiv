package com.creat.motiv.profile.icon

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.creat.motiv.R
import com.creat.motiv.databinding.ActivityPickIconBinding
import com.ilustris.motiv.base.beans.Pics
import com.creat.motiv.utilities.ADMIN_USER
import com.creat.motiv.utilities.SELECTED_ICON
import com.creat.motiv.profile.icon.view.ProfilePicSelectBinder

class PickIconActivity : AppCompatActivity(R.layout.activity_pick_icon) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val admin = intent.getBooleanExtra(ADMIN_USER, false)
        val pickIconBinding: ActivityPickIconBinding = DataBindingUtil.setContentView(this, R.layout.activity_pick_icon)
        ProfilePicSelectBinder(dialog = null,
                admin = admin,
                viewBind = pickIconBinding.iconBind,
                picSelected = ::onSelectPick)
        pickIconBinding.run {
            setSupportActionBar(titleView.toolbar)
            titleView.toolbar.setNavigationOnClickListener { onBackPressed() }
            titleView.collapseTitle.text = "Selecionar ícone de perfil"

        }
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = ""
        }

    }

    private fun onSelectPick(pic: Pics) {
        val resultIntent = Intent()
        resultIntent.putExtra(SELECTED_ICON, pic)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

}

