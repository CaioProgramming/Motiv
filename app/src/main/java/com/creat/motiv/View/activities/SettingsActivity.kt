package com.creat.motiv.View.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.creat.motiv.Model.Beans.Quotes
import com.creat.motiv.Model.QuotesDB
import com.creat.motiv.R
import com.creat.motiv.Utils.Alert
import com.creat.motiv.Utils.Tools
import com.creat.motiv.Utils.Tools.searcharg
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import de.mateware.snacky.Snacky
import java.util.*


class SettingsActivity : AppCompatActivity() {


    internal var activity: Activity = this
    internal var user = FirebaseAuth.getInstance().currentUser
    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var changename: TextView? = null
    private var deleteposts: TextView? = null
    private var deleteaccount: TextView? = null
    private var exit: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        this.exit = findViewById(R.id.exit)
        this.deleteaccount = findViewById(R.id.deleteaccount)
        this.deleteposts = findViewById(R.id.deleteposts)
        this.changename = findViewById(R.id.changename)
        this.toolbar = findViewById(R.id.toolbar)


    }

    private fun Removeposts() {


        val quotesdb = Tools.quotesreference
        quotesdb.orderByChild("userID")
                .startAt(user!!.uid)
                .endAt(user!!.uid + searcharg)
        quotesdb.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val myquotes = ArrayList<Quotes>()
                myquotes.clear()
                for (d in dataSnapshot.children) {
                    val q = d.getValue(Quotes::class.java)
                    myquotes.add(q!!)
                }
                val quotesDB = QuotesDB(this@SettingsActivity)
                val alertDialog = AlertDialog.Builder(activity)
                        .setTitle("Tem certeza?")
                        .setMessage("Suas " + myquotes.size + " frases serão removidas para sempre! S E M P R E")
                        .setNeutralButton("Tenho certeza sim, cliquei porque quis!") { dialogInterface, i ->
                            for (quotes in myquotes) {
                                quotesDB.Removerposts(quotes.id!!)
                            }
                        }
                        .setNegativeButton("Cliquei errado calma", null)
                alertDialog.show()


            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }

    private fun RemoverAccount() {
        val quotesdb = Tools.quotesreference
        quotesdb.orderByChild("userID").startAt(user!!.uid)
                .endAt(user!!.uid + searcharg)
        quotesdb.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val myquotes = ArrayList<Quotes>()
                myquotes.clear()
                for (d in dataSnapshot.children) {
                    val q = d.getValue(Quotes::class.java)
                    myquotes.add(q!!)
                }
                val quotesDB = QuotesDB(activity)
                val alertDialog = AlertDialog.Builder(activity)
                        .setTitle("Tem certeza?")
                        .setMessage("Você e suas " + myquotes.size + " frases serão removidos para sempre! S E M P R E")
                        .setNeutralButton("Sim me tira daqui agora") { dialogInterface, i ->
                            val progressDialog = ProgressDialog(activity, R.style.Dialog_No_Border)
                            progressDialog.show()
                            for (quotes in myquotes) {
                                quotesDB.Apagarconta(quotes.id!!)
                            }
                            progressDialog.setMessage("Apagando tudo...")
                            val timer = object : CountDownTimer(2000, 100) {
                                override fun onTick(l: Long) {

                                }

                                override fun onFinish() {
                                    progressDialog.dismiss()
                                }
                            }.start()
                        }.setNegativeButton("Cliquei errado calma", null)
                alertDialog.show()


            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }


    private fun changename() {
        val a = Alert(activity)
        a.changename()
    }

    override fun onResume() {
        changename!!.setOnClickListener { changename() }



        exit!!.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val snackbar = Snacky.builder().setActivity(activity).setBackgroundColor(Color.WHITE).setTextColor(activity.resources.getColor(R.color.colorPrimaryDark)).build()
            snackbar.setText("Você saiu do aplicativo")
            snackbar.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE).show()
            val timer = object : CountDownTimer(3500, 100) {
                override fun onTick(l: Long) {

                }

                override fun onFinish() {
                    val intent = Intent(applicationContext, Splash::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }.start()
        }


        deleteposts!!.setOnClickListener { Removeposts() }

        deleteaccount!!.setOnClickListener { RemoverAccount() }
        super.onResume()
    }
}
