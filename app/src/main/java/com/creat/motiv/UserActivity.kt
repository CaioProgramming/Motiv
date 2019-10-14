package com.creat.motiv

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.Beans.User
import com.creat.motiv.Database.QuotesDB
import com.creat.motiv.Database.UserDB
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
        username = findViewById(R.id.username)
        posts = findViewById(R.id.posts)
        edit = findViewById(R.id.edit)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar!!.setNavigationOnClickListener { activity.finish() }
        edit!!.visibility = View.GONE
        Carregar(myquotesrecycler)
    }

    private fun Carregar(recyclerView: RecyclerView?) {
        val u = User()
        u.name = name!!
        u.picurl = upic!!
        u.uid = uid!!
        val userDB = UserDB(this)
        userDB.LoadUser(profilepic!!, username!!, u)

        val quotesDB = QuotesDB(this)
        if (true) {
            quotesDB.CarregarUserQuotes(posts!!, uid!!)
        } else {
            val a = Alert(this)
            a.Message(a.erroricon, "Erro ao recuperar informações de usuário")
        }
    }
}
