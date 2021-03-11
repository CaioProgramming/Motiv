package com.creat.motiv.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentSettingsBinding
import com.creat.motiv.utilities.hideBackButton
import com.creat.motiv.utilities.showSupportActionBar
import com.creat.motiv.utilities.snackmessage
import com.creat.motiv.profile.view.binders.SettingsBinder
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment() {


    lateinit var settingsBinding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        settingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return settingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        context?.hideBackButton()
        context?.showSupportActionBar()
        if (user == null) {
            snackmessage(requireContext(), "Você está desconectado")
        } else {
            SettingsBinder(user.uid, settingsBinding)
        }
    }


}