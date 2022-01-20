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

    var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                user = FirebaseAuth.getInstance().currentUser
            } else {
                if (response != null) {
                    DefaultAlert(this, "Atenção", "Ocorreu um erro ao realizar o login",
                        okClick = {
                            signIn()
                        }).buildDialog()

                }

            }

        }
    }

    private fun signIn() {
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setLogo(R.mipmap.ic_launcher)
                .setAvailableProviders(providers)
                .setTheme(R.style.Motiv_Theme)
                .build(), RC_SIGN_IN
        )
    }

}
