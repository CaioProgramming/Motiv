package com.creat.motiv.profile.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creat.motiv.R

const val SETTINGS_FRAG_TAG = "SETTINGS_FRAGMENT"

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context is AppCompatActivity) {
            val activity: AppCompatActivity = context as AppCompatActivity
            activity.run {
                supportActionBar?.show()
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }
}