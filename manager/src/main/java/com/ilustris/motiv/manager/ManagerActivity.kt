package com.ilustris.motiv.manager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.giphy.sdk.ui.Giphy
import com.google.android.material.navigation.NavigationView
import com.ilustris.motiv.base.utils.GIPHY_KEY
import com.ilustris.motiv.manager.databinding.ActivityManagerBinding
import com.ilustris.motiv.manager.ui.covers.CoversFragment
import com.ilustris.motiv.manager.ui.home.ManagerHomeFragment
import com.ilustris.motiv.manager.ui.icons.IconsFragment
import com.ilustris.motiv.manager.ui.styles.StylesFragment


class ManagerActivity : AppCompatActivity(R.layout.activity_manager) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Giphy.configure(this, GIPHY_KEY)
        //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        ActivityManagerBinding.inflate(layoutInflater).setupView()

    }

    private fun ActivityManagerBinding.setupView() {
        setSupportActionBar(findViewById(R.id.motiv_toolbar))
        val navHost = navHostFragment as NavHostFragment
        val navController = navHost.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawer)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun NavigationView.setupNavigation() {
        setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    changeFragment(ManagerHomeFragment())
                    true
                }
                R.id.navigation_covers -> {
                    changeFragment(CoversFragment())
                    true
                }
                R.id.navigation_icons -> {
                    changeFragment(IconsFragment())
                    true
                }
                R.id.navigation_styles -> {
                    changeFragment(StylesFragment())
                    true
                }
                else -> true
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.fui_slide_out_left)
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

}