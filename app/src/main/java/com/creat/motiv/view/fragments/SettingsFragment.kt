package com.creat.motiv.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentSettingsBinding
import com.ilustris.motiv.base.utils.showSupportActionBar
import com.creat.motiv.profile.view.binders.SettingsBinder
import com.google.firebase.auth.FirebaseAuth
import com.silent.ilustriscore.core.utilities.showSnackBar


class SettingsFragment : Fragment() {


    lateinit var settingsBinding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        settingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return settingsBinding.root
    }


}