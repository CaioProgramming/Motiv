package com.creat.motiv.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.creat.motiv.R
import com.creat.motiv.databinding.QuotesCardBinding
import com.creat.motiv.utilities.Alert
import com.creat.motiv.utilities.RC_SIGN_IN
import com.creat.motiv.quote.view.binder.QuoteCardBinder
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.ilustris.motiv.base.beans.Quote
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class Splash : AppCompatActivity(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val datenow = Calendar.getInstance().time
        val calendar = GregorianCalendar()
        calendar.time = datenow
        QuoteCardBinder(Quote.splashQuote().apply {
            val dateNow = Calendar.getInstance().time
            val calendar = GregorianCalendar()
            calendar.time = dateNow
            author = String.format(getString(R.string.company), calendar.get(Calendar.YEAR))
        }, QuotesCardBinding.bind(quoteCard))
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
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
                val i = Intent(this, MainActivity::class.java)
                i.putExtra("novo", false)
                i.putExtra("notification", true)
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_up)
                startActivity(i)
                this.finish()
            }, 5000)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                this.finish()
            } else {
                if (response != null) {
                    Alert(this).showAlert(
                            message = "Ocorreu um erro ao fazer seu login tente novamente", okClick = {
                        signIn()
                    },
                            icon = R.drawable.ic_sad
                    )
                }

            }

        }
    }

    private fun begin(exists: Boolean) {
        if (!exists) {
            val i = Intent(this, NewUser::class.java)
            startActivity(i)
            this.finish()
        } else {

        }
    }


}