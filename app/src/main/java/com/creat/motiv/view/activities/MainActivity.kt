package com.creat.motiv.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.creat.motiv.R
import com.creat.motiv.databinding.ActivityMainBinding
import com.creat.motiv.view.fragments.*
import com.ilustris.motiv.base.utils.RC_SIGN_IN
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilustris.motiv.base.dialog.DefaultAlert


open class MainActivity : AppCompatActivity(R.layout.activity_main) {

    var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.motiv_toolbar))
        ActivityMainBinding.inflate(layoutInflater).setupView()
    }

    private fun ActivityMainBinding.setupView() {
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
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
