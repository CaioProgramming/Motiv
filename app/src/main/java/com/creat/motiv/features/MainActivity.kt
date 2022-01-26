package com.creat.motiv.features

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.creat.motiv.R
import com.creat.motiv.databinding.ActivityMainBinding
import com.ilustris.motiv.base.utils.RC_SIGN_IN
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilustris.motiv.base.dialog.DefaultAlert


open class MainActivity : AppCompatActivity() {

    var mainActBind: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActBind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActBind!!.root)
        mainActBind?.setupView()
    }

    private fun ActivityMainBinding.setupView() {
        val navController = findNavController(R.id.nav_host_fragment)
        //navView.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_profile,
                R.id.navigation_search,
                R.id.navigation_settings,
                R.id.navigation_new_quote
            )
        )
        mainActBind?.run {
            //mainToolbar.setupWithNavController(navController, appBarConfiguration)
            //setSupportActionBar(mainToolbar)
        }

    }

}
