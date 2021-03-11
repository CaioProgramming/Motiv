package com.creat.motiv.view.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.creat.motiv.MotivApplication
import com.creat.motiv.R
import com.creat.motiv.model.beans.Version
import com.creat.motiv.utilities.Alert
import com.creat.motiv.utilities.RC_SIGN_IN
import com.creat.motiv.utilities.Tools
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


open class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var motivApplication: MotivApplication
    internal lateinit var version: Version
    internal var a: Alert? = null
    internal var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser


    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val w: Window = window
        //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            setupNavigation()
        }
        internetconnection()
        motivApplication = application as MotivApplication
        user = FirebaseAuth.getInstance().currentUser
        checkUser()

    }



    private fun setupNavigation() {

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
    }

    private fun checkUser() {
        if (user == null) {
            val providers = Arrays.asList<AuthUI.IdpConfig>(
                    //AuthUI.IdpConfig.FacebookBuilder().build(),
                    //new AuthUI.IdpConfig.TwitterBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build())
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.Motiv_Theme)
                    .build(), RC_SIGN_IN)
        } else {
            val user = FirebaseAuth.getInstance().currentUser
            if (!user!!.isEmailVerified) {
                Alert(this).showAlert(message = Tools.offlinemessage(), buttonMessage = "Conectar",
                        icon = R.drawable.fui_ic_mail_white_24dp,
                        okClick = {
                            user.sendEmailVerification()
                        })

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.navigation_about -> {
                val i = Intent(this, AboutActivity::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()
        checkUser()
        internetconnection()
        setupNavigation()

    }


    override fun onStart() {
        super.onStart()
        checkUser()
        internetconnection()
    }


    override fun onRestart() {
        super.onRestart()
        checkUser()
        internetconnection()
    }


    private fun internetconnection() {
        if (a == null) {
            if (!isNetworkAvailable) {
                Alert(this).showAlert(message = Tools.offlinemessage(), buttonMessage = "Conectar", icon = R.drawable.ic_broken_link)
            }
        }
    }

}
