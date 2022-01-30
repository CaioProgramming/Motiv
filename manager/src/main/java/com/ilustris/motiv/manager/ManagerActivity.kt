package com.ilustris.motiv.manager

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.giphy.sdk.ui.Giphy
import com.ilustris.motiv.base.utils.GIPHY_KEY
import com.ilustris.motiv.manager.databinding.ActivityManagerBinding


class ManagerActivity : AppCompatActivity() {

    private var managerBinding: ActivityManagerBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Giphy.configure(this, GIPHY_KEY)
        managerBinding = ActivityManagerBinding.inflate(layoutInflater)
        setContentView(managerBinding!!.root)
        managerBinding?.setupView()
    }

    private fun ActivityManagerBinding.setupView() {
        setSupportActionBar(mainContent.motivToolbar)
        val toggle = ActionBarDrawerToggle(
            this@ManagerActivity,
            drawer,
            mainContent.motivToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navController = findNavController(R.id.manager_nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawer)
        navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


}