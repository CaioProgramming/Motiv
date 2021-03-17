package com.ilustris.motiv.manager

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.giphy.sdk.ui.Giphy
import kotlinx.android.synthetic.main.activity_manager.*

const val GIPHY_KEY = "spg4bqTWAgxiLjsh4VEnQ1Embqpg3dmk"

class ManagerActivity : AppCompatActivity(R.layout.activity_manager) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Giphy.configure(this, GIPHY_KEY)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        setSupportActionBar(managerToolbar)
        if (savedInstanceState == null) {
            setupNavigation()
        }
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_styles))
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
    }

}