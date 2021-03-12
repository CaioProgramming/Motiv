package com.ilustris.motiv.manager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.creat.motiv.utilities.RC_SIGN_IN
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.databinding.ActivitySplashBinding
import com.ilustris.motiv.manager.ui.home.binder.QuoteManagerCardBinder
import java.util.*

class ManagerSplash : AppCompatActivity(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val datenow = Calendar.getInstance().time
        val calendar = GregorianCalendar()
        calendar.time = datenow
        val activitySplashBinding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        activitySplashBinding.run {
            QuoteManagerCardBinder(Quote.adminQuote().apply {
                val dateNow = Calendar.getInstance().time
                val calendar = GregorianCalendar()
                calendar.time = dateNow
                author = String.format(getString(R.string.company), calendar.get(Calendar.YEAR))
            }, quoteCard)
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        signIn()
    }

    private fun signIn() {
        val user = FirebaseAuth.getInstance().currentUser
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
            Handler().postDelayed({
                startMainAct()
            }, 5000)
        }
    }

    private fun startMainAct() {
        val i = Intent(this, ManagerActivity::class.java)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_up)
        startActivity(i)
        this.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                startMainAct()
            } else {
                if (response != null) {
                    signIn()
                }

            }

        }
    }

}