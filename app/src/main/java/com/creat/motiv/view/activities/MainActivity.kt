package com.creat.motiv.view.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.ActivityMainBinding
import com.creat.motiv.model.beans.Version
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.Pref
import com.creat.motiv.utils.RC_SIGN_IN
import com.creat.motiv.utils.Tools
import com.creat.motiv.utils.Tools.setLightStatusBar
import com.creat.motiv.utils.Tools.uimode
import com.creat.motiv.view.adapters.MainAdapter
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

open class MainActivity : AppCompatActivity(){
    private lateinit var preferences: Pref
    protected lateinit var app: App
    internal lateinit var version: Version
    internal var a: Alert? = null
    internal var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val home: Boolean
        get() {
            return pager.currentItem == 0
        }
    var previoustab = 0

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actbind: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(actbind.root)
        app = application as App
        internetconnection()

        user = FirebaseAuth.getInstance().currentUser
        checkUser()
        preferences = Pref(this)
        pager.run {
            adapter = MainAdapter(supportFragmentManager)
            navigation.setupWithViewPager(pager, true)
            navigation.getTabAt(0)?.text = "Home"
            navigation.getTabAt(1)?.text = "Escrever"
            navigation.getTabAt(2)?.text = FirebaseAuth.getInstance().currentUser?.displayName
                    ?: "Deslogado"

        }
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            Glide.with(this).load(it.photoUrl).error(R.drawable.notfound).into(userpic)
            userpic.setOnClickListener {
                pager.currentItem = 2
            }
        }

        if (!user!!.isEmailVerified) {
            Alert(this).showAlert(message = Tools.offlinemessage(), buttonMessage = "Conectar",
                    icon = R.drawable.fui_ic_mail_white_24dp,
                    okClick = {
                        user.sendEmailVerification()
                    })

        }

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        if (!uimode(this)) {
            setLightStatusBar(this)
        }

    }

    private fun checkUser() {
        if (user == null) {
            val providers = Arrays.asList<AuthUI.IdpConfig>(
                    AuthUI.IdpConfig.FacebookBuilder().build(),
                    //new AuthUI.IdpConfig.TwitterBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build())
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(), RC_SIGN_IN)
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

    override fun onBackPressed() {

        if (home) {
            super.onBackPressed()
        } else {
            pager?.setCurrentItem(1,true)
        }

    }

    private fun internetconnection() {
        if (a == null) {
            if (!isNetworkAvailable) {
                Alert(this).showAlert(message = Tools.offlinemessage(), buttonMessage = "Conectar", icon = R.drawable.ic_broken_link)
            }
        }


    }

}
