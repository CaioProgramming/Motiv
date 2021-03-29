package com.creat.motiv.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.creat.motiv.R
import com.creat.motiv.databinding.PlayerLayoutBinding
import com.creat.motiv.profile.view.PROFILE_FRAG_TAG
import com.creat.motiv.profile.view.ProfileFragment
import com.creat.motiv.profile.view.SETTINGS_FRAG_TAG
import com.creat.motiv.profile.view.SettingsFragment
import com.creat.motiv.quote.EditQuoteActivity
import com.creat.motiv.radio.PlayerBinder
import com.creat.motiv.view.fragments.*
import com.ilustris.motiv.base.utils.RC_SIGN_IN
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilustris.animations.fadeOut
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.utils.setMotivTitle
import kotlinx.android.synthetic.main.activity_main.*
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
            nav_view.selectedItemId = R.id.navigation_home
        }
    }

    override fun onResume() {
        setupNavigation()
        super.onResume()
    }

    override fun onBackPressed() {
        if (isHome()) super.onBackPressed() else handleCurrentFragment()


    }

    private fun isHome(): Boolean {
        val homeFrag = (supportFragmentManager.findFragmentByTag(HOME_FRAG_TAG) as HomeFragment?)
        return homeFrag != null && homeFrag.isVisible
    }

    private fun handleCurrentFragment() {
        val favoritesFragment = (supportFragmentManager.findFragmentByTag(FAVORITES_FRAG_TAG) as FavoritesFragment?)
        val settingsFragment = (supportFragmentManager.findFragmentByTag(SETTINGS_FRAG_TAG) as SettingsFragment?)
        val searchFragment = (supportFragmentManager.findFragmentByTag(SEARCH_FRAG_TAG) as SearchFragment?)
        val profileFragment = (supportFragmentManager.findFragmentByTag(PROFILE_FRAG_TAG) as ProfileFragment?)


        if (favoritesFragment != null || settingsFragment != null) {
            nav_view.selectedItemId = R.id.navigation_profile
            return
        }
        if (searchFragment != null || profileFragment != null) {
            nav_view.selectedItemId = R.id.navigation_home
            return
        }
    }


    private fun setupNavigation() {

        nav_view.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navigation_home -> {
                        setMotivTitle(getString(R.string.app_name))
                        supportFragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                .replace(R.id.nav_host_fragment, HomeFragment(), HOME_FRAG_TAG)
                                .commit()
                        true
                    }
                    R.id.navigation_search -> {
                        supportFragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                .replace(R.id.nav_host_fragment, SearchFragment(), SEARCH_FRAG_TAG)
                                .commit()
                        true
                    }
                    R.id.navigation_profile -> {

                        supportFragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                .replace(R.id.nav_host_fragment, ProfileFragment(), PROFILE_FRAG_TAG)
                                .commit()
                        true
                    }
                    else -> false
                }

            }
        }

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
                AuthUI.IdpConfig.EmailBuilder().build())
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setLogo(R.mipmap.ic_launcher)
                .setAvailableProviders(providers)
                .setTheme(R.style.Motiv_Theme)
                .build(), RC_SIGN_IN)
    }

}
