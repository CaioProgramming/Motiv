package com.creat.motiv.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.creat.motiv.R
import com.creat.motiv.databinding.ActivitySettingsBinding
import com.creat.motiv.utilities.Alert
import com.creat.motiv.utilities.DialogStyles
import com.creat.motiv.view.binders.ChangeNameBinder
import com.creat.motiv.view.binders.ProfileTopBinder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.title_layout.*


class SettingsActivity : AppCompatActivity(R.layout.activity_settings) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actbind: ActivitySettingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        actbind.run {
            setContentView(root)
            deleteposts.setOnClickListener {
                Alert(this@SettingsActivity, DialogStyles.FULL_SCREEN).deleteAllDialog()
            }
            ChangeNameBinder(this@SettingsActivity, actbind.changenameView)
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let {
                ProfileTopBinder(uid = it.uid, viewBind = this.profileView, context = this@SettingsActivity, isSettings = true, fragmentManager = supportFragmentManager)
            }
            exit.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(applicationContext, Splash::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            it.title = ""
        }

        collapseTitle.text = getString(R.string.settings_activity_title)
    }

}
