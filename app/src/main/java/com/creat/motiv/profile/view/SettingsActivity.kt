package com.creat.motiv.profile.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentSettingsBinding
import com.creat.motiv.profile.view.binders.SettingsBinder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(R.layout.activity_settings) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            SettingsBinder(it.uid, FragmentSettingsBinding.bind(settings_layout))
        }
    }
}