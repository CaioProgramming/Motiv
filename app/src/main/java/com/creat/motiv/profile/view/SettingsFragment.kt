package com.creat.motiv.profile.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentSettingsBinding
import com.creat.motiv.profile.view.binders.SettingsBinder
import com.google.firebase.auth.FirebaseAuth
import com.ilustris.motiv.base.utils.activity
import com.ilustris.motiv.base.utils.getToolbar
import com.ilustris.motiv.base.utils.setMotivTitle
import kotlinx.android.synthetic.main.activity_settings.*

const val SETTINGS_FRAG_TAG = "SETTINGS_FRAGMENT"

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SettingsBinder(FragmentSettingsBinding.bind(view))
        if (context is AppCompatActivity) {
            val activity: AppCompatActivity = context as AppCompatActivity
            activity.run {
                supportActionBar?.show()
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                getToolbar()?.let {
                    it.setNavigationOnClickListener {
                        supportFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out)
                                .remove(this@SettingsFragment)
                                .replace(R.id.nav_host_fragment, ProfileFragment())
                                .addToBackStack(null).commit()
                    }
                    setMotivTitle("Configurações")
                }
            }
        }
    }
}