package com.creat.motiv

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.creat.motiv.Adapters.MainAdapter
import com.creat.motiv.Beans.User
import com.creat.motiv.Beans.Version
import com.creat.motiv.Utils.Alert
import com.creat.motiv.Utils.Pref
import com.creat.motiv.Utils.Tools
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import de.hdodenhof.circleimageview.CircleImageView
import de.mateware.snacky.Snacky
import java.util.*

class MainActivity : AppCompatActivity() {
    internal lateinit var preferences: Pref
    protected lateinit var app: App
    internal lateinit var version: Version
    internal var a: Alert? = null
    internal var context: Context = this
    private val m_dialog: Dialog? = null
    internal var activity: Activity = this
    internal var user: FirebaseUser? = null
    private var tabs: TabLayout? = null
    private var pager: ViewPager? = null
    private var toolbar: Toolbar? = null
    private var profilepic: CircleImageView? = null
    internal var container: CoordinatorLayout? = null

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as App
        user = FirebaseAuth.getInstance().currentUser
        checkUser()
        preferences = Pref(context)
        setContentView(R.layout.activity_main2)
        container = findViewById(R.id.container)


        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        this.tabs = findViewById(R.id.tabs)
        this.pager = findViewById(R.id.pager)
        this.profilepic = findViewById(R.id.profilepic)

        val mainAdapter = MainAdapter(supportFragmentManager)
        pager!!.adapter = mainAdapter
        tabs!!.setupWithViewPager(pager)
        tabs!!.getTabAt(0)!!.text = "Home"
        tabs!!.getTabAt(1)!!.text = "Favoritos"
        tabs!!.getTabAt(2)!!.text = "Perfil"
        val adView = findViewById<AdView>(R.id.adView)
        MobileAds.initialize(this,
                "ca-app-pub-4979584089010597/9177000416")

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


        pager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                var appBarLayout: AppBarLayout = findViewById(R.id.appbarlayout)

                if (position == 2) {
                    appBarLayout.setExpanded(false, true)
                } else {
                    appBarLayout.setExpanded(true, true)
                }

            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        Glide.with(this).load(user!!.photoUrl).error(getDrawable(R.drawable.notfound)).addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                pager!!.currentItem = 2
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                return false
            }
        }).into(profilepic!!)
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
        version()
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
            val i = Intent(this, Splash::class.java)
            startActivity(i)
            this.finish()
        }
    }

    private fun version() {
        val versionName = BuildConfig.VERSION_NAME
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


                    val snackbar = Snacky.builder().setActivity(activity).setText("Sua versão está desatualizada " +
                            " o motiv atualmente está na versão " + version.version + " enquanto você está na versão  " + versionName)
                            .setIcon(R.drawable.ic_autorenew_black_24dp)
                            .setTextColor(Color.BLACK)
                            .setActionTextColor(resources.getColor(R.color.colorPrimaryDark))
                            .setBackgroundColor(Color.WHITE)
                            .setDuration(10000)
                            .build()

                    snackbar.setAction("Atualizar") {
                        val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.creat.motiv")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                    snackbar.show()

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
