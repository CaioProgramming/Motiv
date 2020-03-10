package com.creat.motiv.View.activities
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.adapters.MainAdapter
import com.creat.motiv.databinding.ActivityMainBinding
import com.creat.motiv.model.Beans.User
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.Tools
import com.creat.motiv.View.activities.Splash.Companion.RC_SIGN_IN
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
   var user = FirebaseAuth.getInstance().currentUser
    private lateinit var app: App

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
        user = FirebaseAuth.getInstance().currentUser
        checkUser()
        val mainAdapter = MainAdapter(supportFragmentManager)
        pager.adapter = mainAdapter
        navigation.setupWithViewPager(pager)
        if (!user!!.isEmailVerified) {
             Alert.builder(this).mailmessage()
        }
        internetconnection()
        var profilepic = CircleImageView(this)
        profilepic.maxWidth = 45
        profilepic.maxHeight = 48
        Glide.with(this).load(user!!.photoUrl).error(getDrawable(R.drawable.notfound)).into(profilepic)
        navigation.getTabAt(2)?.customView = profilepic
        navigation.getTabAt(0)?.setIcon(R.drawable.home)
        navigation.getTabAt(1)?.setIcon(R.drawable.add)


    }



    private fun checkUser() {
        if (user != null) {
            val userreference = FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)
            userreference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
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

        if (pager!!.currentItem == 0) {
            super.onBackPressed()
        } else {
            pager!!.setCurrentItem(0, true)
        }

    }

    private fun internetconnection() {
            if (!isNetworkAvailable) {
                Alert.builder(this).message(getDrawable(R.drawable.ic_broken_link),Tools.offlinemessage())
            }


    }


}
