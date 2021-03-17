package com.creat.motiv.view.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.creat.motiv.R
import com.creat.motiv.databinding.PlayerLayoutBinding
import com.creat.motiv.quote.view.EditQuoteActivity
import com.creat.motiv.radio.PlayerBinder
import com.creat.motiv.utilities.Alert
import com.creat.motiv.utilities.RC_SIGN_IN
import com.ilustris.motiv.base.Tools
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilustris.animations.fadeOut
import com.ilustris.motiv.base.dialog.DefaultAlert
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


open class MainActivity : AppCompatActivity(R.layout.activity_main) {

    internal var a: Alert? = null
    var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        if (savedInstanceState == null) {
            setupNavigation()
            PlayerBinder(PlayerLayoutBinding.bind(playerView)).initView()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        GlobalScope.launch(Dispatchers.IO) {
            delay(10000)
            GlobalScope.launch(Dispatchers.Main) {
                message.fadeOut()
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
            R.id.navigation_about -> {
                val i = Intent(this, AboutActivity::class.java)
                startActivity(i)
            }
            R.id.navigation_add -> {
                startActivity(Intent(this, EditQuoteActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()
        setupNavigation()

    }


}
