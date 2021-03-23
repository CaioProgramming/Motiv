package com.creat.motiv.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.creat.motiv.R
import com.creat.motiv.databinding.PlayerLayoutBinding
import com.creat.motiv.quote.EditQuoteActivity
import com.creat.motiv.radio.PlayerBinder
import com.ilustris.motiv.base.utils.RC_SIGN_IN
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilustris.animations.fadeOut
import com.ilustris.motiv.base.dialog.DefaultAlert
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


open class MainActivity : AppCompatActivity(R.layout.activity_main) {

    var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    var playerBinder: PlayerBinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.motiv_toolbar))
        if (savedInstanceState == null) {
            setupNavigation()
            if (user != null) {
                playerBinder = PlayerBinder(PlayerLayoutBinding.bind(playerView)).apply {
                    initView()
                }
            }
        }
    }


    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_search, R.id.navigation_add, R.id.navigation_profile, R.id.navigation_settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.navigation_add -> {
                startActivity(Intent(this, EditQuoteActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                if (playerBinder == null) {
                    playerBinder = PlayerBinder(PlayerLayoutBinding.bind(playerView)).apply {
                        initView()
                    }
                }
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
                AuthUI.IdpConfig.EmailBuilder().build())
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setLogo(R.mipmap.ic_launcher)
                .setAvailableProviders(providers)
                .setTheme(R.style.Motiv_Theme)
                .build(), RC_SIGN_IN)
    }

}
