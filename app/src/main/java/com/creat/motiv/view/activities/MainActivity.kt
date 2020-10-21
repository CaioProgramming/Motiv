package com.creat.motiv.view.activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.ActivityMainBinding
import com.creat.motiv.model.Beans.User
import com.creat.motiv.model.Beans.Version
import com.creat.motiv.model.UserDB
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.Pref
import com.creat.motiv.utils.Tools
import com.creat.motiv.utils.Tools.RC_SIGN_IN
import com.creat.motiv.utils.Tools.setLightStatusBar
import com.creat.motiv.utils.Tools.uimode
import com.creat.motiv.view.adapters.MainAdapter
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.mikhaellopez.rxanimation.fadeIn
import de.hdodenhof.circleimageview.CircleImageView
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


        pager.adapter = MainAdapter(supportFragmentManager)
        navigation.setupWithViewPager(pager, true)

        if (!user!!.isEmailVerified) {
            Alert.builder(this).mailmessage()
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Home"
        if(!uimode(this)){
            setLightStatusBar(this)
        }

        toolbar.fadeIn().andThen(pager.fadeIn()).ambWith(navigation.fadeIn()).subscribe()
    }

    var previoustab = 0
    private fun swapTabs(position: Int) {
        val inanimation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_in_bottom)
        val inanimation2 = AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_in_top)
        navigation.getTabAt(position)?.view?.startAnimation(inanimation)
        navigation.getTabAt(previoustab)?.view?.startAnimation(inanimation2)
    }


    fun reloaduser() {
        user = FirebaseAuth.getInstance().currentUser
        checkUser()
        if (navigation.selectedTabPosition == 2) navigation.getTabAt(2)?.text = user?.displayName
    }

    private fun checkUser() {


        if (user != null) {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
                val u = User()
                u.name = user?.displayName
                u.email = user?.email
                u.picurl = user?.photoUrl.toString()
                u.phonenumber = user?.phoneNumber
                u.uid = user?.uid
                u.token = instanceIdResult.token
                UserDB().insertUser(u)
            }

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

        val profilePic: MenuItem? = menu.findItem(R.id.profilepic)
        val circleImageView: CircleImageView? = profilePic?.actionView as CircleImageView

        circleImageView?.let {
            it.layoutParams.height = Tools.dpToPx(100, this)
            it.layoutParams.width = Tools.dpToPx(100, this)
            Glide.with(this).load(user!!.photoUrl).error(getDrawable(R.drawable.notfound)).into(it)

        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId



        when (id) {
            R.id.navigation_about -> {
                val alert = Alert(this)
                alert.about()
            }
            R.id.profilepic -> {
                pager.currentItem = 2
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
                a = Alert(this)
                a!!.message(getDrawable(R.drawable.ic_broken_link), Tools.offlinemessage())
            }
        }


    }

}
