package com.creat.motiv.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.creat.motiv.R
import com.creat.motiv.databinding.ActivitySettingsBinding
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.DialogStyles
import com.creat.motiv.view.binders.ChangeNameBinder
import com.google.firebase.auth.FirebaseAuth


class SettingsActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actbind: ActivitySettingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        actbind.run {
            setContentView(root)
            deleteposts.setOnClickListener {
                Alert(this@SettingsActivity, DialogStyles.FULL_SCREEN).deleteAllDialog()
            }
            ChangeNameBinder(this@SettingsActivity, actbind.changenameView)
            exit.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(applicationContext, Splash::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }

}
