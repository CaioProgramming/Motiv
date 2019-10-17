package com.creat.motiv.View.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.Model.Beans.User
import com.creat.motiv.Model.QuotesDB
import com.creat.motiv.Model.UserDB
import com.creat.motiv.R
import com.creat.motiv.Utils.Alert
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import de.hdodenhof.circleimageview.CircleImageView

class UserActivity : AppCompatActivity() {

    private var myquotesrecycler: RecyclerView? = null
    private var appbarlayout: AppBarLayout? = null
    private var collapsetoolbar: CollapsingToolbarLayout? = null
    private var toolbar: Toolbar? = null
    private var profilepic: CircleImageView? = null
    private var username: TextView? = null
    private var posts: TextView? = null
    private var edit: Button? = null
    private var uid: String? = null
    private var name: String? = null
    private var upic: String? = null
    private val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)
        uid = intent.getStringExtra("uid")
        name = intent.getStringExtra("uname")
        upic = intent.getStringExtra("upic")
        initView()

    }

    private fun initView() {
        myquotesrecycler = findViewById(R.id.myquotesrecycler)
        appbarlayout = findViewById(R.id.appbarlayout)
        collapsetoolbar = findViewById(R.id.collapsetoolbar)
        toolbar = findViewById(R.id.toolbar)
        profilepic = findViewById(R.id.profilepic)
        posts = findViewById(R.id.posts)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_left_arrow)

        toolbar!!.setNavigationOnClickListener { activity.finish() }
        Carregar()
    }

    private fun Carregar() {
        val u = User()
        u.name = name!!
        u.picurl = upic!!
        u.uid = uid!!
        val userDB = UserDB(this)
        userDB.LoadUser(profilepic!!, toolbar!!, u)

        val quotesDB = QuotesDB(this)
        quotesDB.recyclerView = myquotesrecycler
        quotesDB.usercount = posts
        if (true) {
            quotesDB.CarregarUserQuotes(uid!!)
        } else {
            val a = Alert(this)
            a.Message(a.erroricon, "Erro ao recuperar informações de usuário")
        }
    }
}
