package com.creat.motiv.View.activities

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
import com.creat.motiv.Model.Beans.User
import com.creat.motiv.Model.Beans.Version
import com.creat.motiv.R
import com.creat.motiv.Utils.Alert
import com.creat.motiv.Utils.Pref
import com.creat.motiv.Utils.Tools
import com.creat.motiv.adapters.MainAdapter
import com.creat.motiv.databinding.ActivityMain2Binding
import com.firebase.ui.auth.AuthUI
import com.fxn.OnBubbleClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.*

class MainActivity : AppCompatActivity() {
    internal lateinit var preferences: Pref
    protected lateinit var app: App
    internal lateinit var version: Version
    internal var a: Alert? = null
    internal var user: FirebaseUser? = null


    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actbind: ActivityMain2Binding = DataBindingUtil.setContentView(this, R.layout.activity_main2)
        app = application as App
        user = FirebaseAuth.getInstance().currentUser
        checkUser()
        preferences = Pref(this)

        val mainAdapter = MainAdapter(supportFragmentManager)
        pager.adapter = mainAdapter
        bubbleTabBar.setupBubbleTabBar(pager)
        bubbleTabBar.addBubbleListener(object : OnBubbleClickListener {
            override fun onBubbleClick(id: Int) {
                when (id) {
                    R.id.home -> {
                        pager.setCurrentItem(0, true)
                        Tools.fadeIn(profilepic, 500)

                    }
                    R.id.favorites -> {
                        pager.setCurrentItem(1, true)
                        Tools.fadeIn(profilepic, 500)

                    }

                    else -> {
                        pager.setCurrentItem(2, true)
                        Tools.fadeOut(profilepic, 500)

                    }
                }
            }
        })


     /*   val adView = findViewById<AdView>(R.id.adView)
        MobileAds.initialize(this,
                "ca-app-pub-4979584089010597/9177000416")

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)*/



        Glide.with(this).load(user!!.photoUrl).error(getDrawable(R.drawable.notfound)).into(profilepic!!)
        profilepic!!.setOnClickListener { pager!!.currentItem = 2 }
        if (!user!!.isEmailVerified) {
            val builder = AlertDialog.Builder(this).setMessage("Email não verificado")
            builder.setMessage("Eai Beleza? Verifica o email que você vai poder fazer mais que apenas ver frases")

            builder.setPositiveButton("Manda esse email aí po") { dialogInterface, i -> user!!.sendEmailVerification() }
            builder.setNegativeButton("Não to afim meu camarada") { dialogInterface, i -> dialogInterface.dismiss() }

            builder.show()
        }
        //theme();
        internetconnection()


        val time: Long = 500
        setSupportActionBar(toolbar)


        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                when (position) {
                    2 -> {
                        toolbar.title = user!!.displayName
                    }
                    1 -> {
                        toolbar.title = "Favoritos"

                    }
                    else -> {
                        toolbar.title = getString(R.string.app_name)

                    }
                }
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    2 -> {
                        toolbar.title = user!!.displayName
                    }
                    1 -> {
                        toolbar.title = getString(R.string.app_name)
                    }
                    else -> {
                        toolbar.title = getString(R.string.app_name)
                    }
                } //To change body of created functions use File | Settings | File Templates.
            }
        })


        fadeIn(toolbar!!, time * 2).andThen(fadeIn(bubbleTabBar, time))
                ?.andThen(fadeIn(pager, time))
                ?.subscribe()




        version()

    }

    fun fadeIn(view: View, duration: Long): Completable {
        val animationSubject = CompletableSubject.create()
        return animationSubject.doOnSubscribe {
            ViewCompat.animate(view)
                    .setDuration(duration)
                    .alpha(1f)
                    .withEndAction {
                        animationSubject.onComplete()
                    }
        }


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
        var versionName = info.versionName
        version = Version()

        val versioncheck: Query
        versioncheck = FirebaseDatabase.getInstance().reference.child("version")

        versioncheck.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {

                    if (d != null) {
                        version.version = Objects.requireNonNull<Any>(d.value).toString()
                    }


                }
                if (version.version != versionName) {


                    val alert = Alert(this@MainActivity)
                    alert.version("Sua versão está desatualizada o motiv atualmente está na versão ${version.version}" +
                            "  enquanto você está na versão  ${versionName}")




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

        if (pager!!.currentItem == 0) {
            super.onBackPressed()
        } else {
            pager!!.setCurrentItem(0, true)
        }

    }

    private fun internetconnection() {
        if (a == null) {
            if (!isNetworkAvailable) {
                a = Alert(this)
                a!!.Message(getDrawable(R.drawable.ic_broken_link), Tools.offlinemessage())
            }
        }


    }


}
