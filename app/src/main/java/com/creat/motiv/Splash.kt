package com.creat.motiv

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.creat.motiv.Utils.Tools
import com.creat.motiv.Utils.Typewritter
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseUiException
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import de.mateware.snacky.Snacky
import java.util.*

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_splash)
        val applogo = findViewById<Typewritter>(R.id.apptext)
        val brand = findViewById<TextView>(R.id.inlustrisbrand)
        val datenow = Calendar.getInstance().time
        val calendar = GregorianCalendar()
        calendar.time = datenow
        brand.text = String.format(getString(R.string.company), calendar.get(Calendar.YEAR))
        applogo.delay = 300
        applogo.animateText(getString(R.string.app_name) + "")
        //setalarm();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val countDownTimer = object : CountDownTimer(1500, 100) {
            override fun onTick(l: Long) {

            }

            override fun onFinish() {
                SignIn()

            }
        }.start()


    }


    private fun SignIn() {
        val user = FirebaseAuth.getInstance().currentUser
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


                newuser()


            } else {
                if (response != null) {
                    Snacky.builder().setActivity(this).error().setText("Erro " + Objects.requireNonNull<FirebaseUiException>(response.error).message + " causa " + response.error!!.cause).show()
                }

            }

        }
    }

    private fun newuser() {
        val user = FirebaseAuth.getInstance().currentUser
        val reference = Tools.userreference
        reference.child(user!!.uid)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                begin(dataSnapshot.exists())
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }

    private fun begin(exists: Boolean) {
        if (!exists) {
            val i = Intent(this, NewUser::class.java)
            startActivity(i)
            this.finish()
        } else {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("novo", false)
            i.putExtra("notification", true)
            startActivity(i)
            this.finish()
        }
    }

    companion object {
        private val RC_SIGN_IN = 123
    }
}