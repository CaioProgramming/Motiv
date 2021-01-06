package com.creat.motiv.view.activities

import android.animation.Animator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.creat.motiv.R
import com.creat.motiv.utilities.RC_SIGN_IN
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.ilustriscore.core.view.dialog.VerticalDialog
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class Splash : AppCompatActivity(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val datenow = Calendar.getInstance().time
        val calendar = GregorianCalendar()
        calendar.time = datenow
        inlustrisbrand.text = String.format(getString(R.string.company), calendar.get(Calendar.YEAR))
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        splash_animation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {
                signIn()

            }


        })


    }


    private fun signIn() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val providers = Arrays.asList(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build())
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(), RC_SIGN_IN)
        } else {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("novo", false)
            i.putExtra("notification", true)
            startActivity(i)
            this.finish()
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
                    VerticalDialog.build(
                            R.id.rootblur,
                            fragmentManager = supportFragmentManager,
                            message = "Atenção, ocorreu um erro ao realizar o login! Tente novamente \n (${response.error?.message})",
                            okMessage = "Tentar novamente",
                            okClick = { signIn() }
                    )
                }

            }

        }
    }

}