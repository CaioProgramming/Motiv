package com.ilustris.motiv.manager

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.giphy.sdk.ui.Giphy
import com.ilustris.motiv.base.utils.setMotivTitle
import com.ilustris.motiv.manager.ui.covers.CoversFragment
import com.ilustris.motiv.manager.ui.home.HomeFragment
import com.ilustris.motiv.manager.ui.icons.IconsFragment
import com.ilustris.motiv.manager.ui.styles.StylesFragment
import kotlinx.android.synthetic.main.activity_manager.*

const val GIPHY_KEY = "spg4bqTWAgxiLjsh4VEnQ1Embqpg3dmk"

class ManagerActivity : AppCompatActivity(R.layout.activity_manager) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Giphy.configure(this, GIPHY_KEY)
        //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        setSupportActionBar(findViewById(R.id.motiv_toolbar))
        if (savedInstanceState == null) {
            setupNavigation()
            nav_view.selectedItemId = R.id.navigation_home
        }
    }

    private fun setupNavigation() {
        nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    changeFragment(HomeFragment())
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
                else -> false
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.nav_host_fragment, fragment)
                .commit()
    }

    override fun onBackPressed() {
        if (nav_view.selectedItemId == R.id.navigation_home) super.onBackPressed() else nav_view.selectedItemId = R.id.navigation_home
    }

}