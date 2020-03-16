package com.creat.motiv.view.activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.adapters.MainAdapter
import com.creat.motiv.databinding.ActivityMainBinding
import com.creat.motiv.model.Beans.Version
import com.creat.motiv.model.UserDB
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.Pref
import com.creat.motiv.utils.Tools
import com.creat.motiv.utils.Tools.RC_SIGN_IN
import com.creat.motiv.utils.Tools.setLightStatusBar
import com.creat.motiv.utils.Tools.uimode
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mikhaellopez.rxanimation.fadeIn
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

open class MainActivity : AppCompatActivity(){
    private lateinit var preferences: Pref
    protected lateinit var app: App
    internal lateinit var version: Version
    internal var a: Alert? = null
    internal var user: FirebaseUser? = null
    private val home: Boolean get() {return pager.currentItem == 1}




    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actbind: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        setContentView(actbind.root)
        app = application as App
        internetconnection()

        user = FirebaseAuth.getInstance().currentUser
        checkUser()
        preferences = Pref(this)
        Glide.with(this).load(user!!.photoUrl).error(getDrawable(R.drawable.notfound)).into(profilepic!!)
        profilepic!!.setOnClickListener { pager.currentItem = 2 }


        pager.adapter = MainAdapter(supportFragmentManager)
        navigation.setupWithViewPager(pager, true)

        if (!user!!.isEmailVerified) {
            Alert.builder(this).mailmessage()
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        if(!uimode(this)){
            setLightStatusBar(this)
        }
        navigation.getTabAt(0)?.icon = getDrawable(R.drawable.home)
        navigation.getTabAt(1)?.icon = getDrawable(R.drawable.add)
        navigation.getTabAt(2)?.customView = profilepic
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                when (position) {
                    0 -> toolbar.title = "Home"
                    1 -> toolbar.title = "Nova publicação"
                    else -> toolbar.title = ""
                }
            }


            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> toolbar.title = "Home"
                    1 -> toolbar.title = "Nova publicação"
                    else -> toolbar.title = ""
                }
            }

        })
        toolbar.fadeIn().andThen(pager.fadeIn()).ambWith(navigation.fadeIn()).subscribe()



    }





    private fun checkUser() {


        if (user != null) {
            val userdb = UserDB()
            userdb.insertUser(user!!)
        } else {
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



        if (id == R.id.navigation_about) {
            val alert = Alert(this)
            alert.about()
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
                a = Alert(this)
                a!!.message(getDrawable(R.drawable.ic_broken_link), Tools.offlinemessage())
            }
        }


    }

}
