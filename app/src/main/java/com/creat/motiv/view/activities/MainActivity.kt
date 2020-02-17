package com.creat.motiv.view.activities

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.creat.motiv.model.Beans.User
import com.creat.motiv.model.Beans.Version
import com.creat.motiv.R
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.Pref
import com.creat.motiv.utils.Tools
import com.creat.motiv.adapters.MainAdapter
import com.creat.motiv.databinding.ActivityMainBinding
import com.creat.motiv.utils.NewQuotepopup
import com.creat.motiv.utils.Tools.fadeIn
import com.creat.motiv.utils.Tools.fadeOut
import com.creat.motiv.utils.Tools.setLightStatusBar
import com.creat.motiv.utils.Tools.uimode
import com.creat.motiv.view.fragments.HomeFragment
import com.creat.motiv.view.fragments.ProfileFragment
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {
    internal lateinit var preferences: Pref
    protected lateinit var app: App
    internal lateinit var version: Version
    internal var a: Alert? = null
    internal var user: FirebaseUser? = null
    var home = true

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
        profilepic!!.setOnClickListener { showprofilefrag() }
        if (!user!!.isEmailVerified) {
         Alert.builder(this).mailmessage()
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        version()
       if(!uimode(this)){
        setLightStatusBar(this)
       }

        navigation.setOnNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame,HomeFragment())
                .commit()
    }



    private fun showprofilefrag(){
        home = false
        val profileFragment = ProfileFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame,profileFragment)
                .commit()
        profileFragment.create(user!!)
        supportActionBar?.hide()
    }


    private fun checkUser() {


        if (user != null) {
            val userreference = FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)
            userreference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        val u = User()
                        u.name = user!!.displayName!!
                        u.email = user!!.email!!
                        u.picurl = user!!.photoUrl.toString()
                        u.phonenumber = user!!.phoneNumber!!
                        u.uid = user!!.uid
                        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
                            u.token = instanceIdResult.token
                            userreference.setValue(u)
                        }


                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
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
                    .build(), Splash.RC_SIGN_IN)
        }
    }

    private fun version() {
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
        val versionName = info.versionName
        version = Version()


       val versioncheck = FirebaseDatabase.getInstance().reference.child("version")

        versioncheck.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {
                    if (d != null) {
                        version.version = Objects.requireNonNull<Any>(d.value).toString()
                    }
                }
                if (version.version != versionName) {
                    Alert.builder(this@MainActivity).version()
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
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
            navigation.selectedItemId = R.id.navigation_home
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.navigation_home -> {
                home = true
                fadeIn(frame,1000).andThen(fadeOut(newpost,1000))
                        .andThen {supportFragmentManager.beginTransaction()
                                .replace(R.id.frame,HomeFragment())
                                .commit()}
                        .subscribe()
                supportActionBar?.show()

                return false
            }
            R.id.navigation_new -> {
                fadeOut(frame,1000)
                        .andThen(fadeIn(newpost,1000)).andThen {
                    val newquote = NewQuotepopup(this)
                    newquote.showup()
                }.subscribe()

                return false
            }
        }
        return false
    }


}
