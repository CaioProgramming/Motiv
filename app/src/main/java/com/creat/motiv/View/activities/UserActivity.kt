package com.creat.motiv.View.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.creat.motiv.Model.Beans.User
import com.creat.motiv.Model.QuotesDB
import com.creat.motiv.Model.UserDB
import com.creat.motiv.Utils.Alert
import kotlinx.android.synthetic.main.user_profile.*


class UserActivity : AppCompatActivity() {

    private var uid: String? = null
    private var name: String? = null
    private var upic: String? = null
    private val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.creat.motiv.R.layout.user_profile)
        uid = intent.getStringExtra("uid")
        name = intent.getStringExtra("uname")
        upic = intent.getStringExtra("upic")
        initView()

    }

    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar!!.setNavigationOnClickListener { activity.finish() }
        Carregar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
