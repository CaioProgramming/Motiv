package com.creat.motiv.presenter

import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.Model.QuotesDB
import com.creat.motiv.R
import com.creat.motiv.Utils.Alert
import com.creat.motiv.View.fragments.ProfileFragment
import com.creat.motiv.contract.ViewContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView

class ProfilePresenter(val activity: Activity, val profileFragment: ProfileFragment) : ViewContract {
    val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    var posts: TextView? = null
    var favorites: TextView? = null

    override fun initview(v: View) {
        val username: TextView = v.findViewById(R.id.username)
        favorites = v.findViewById(R.id.likes)
        val myquotesrecycler: RecyclerView = v.findViewById(R.id.myquotesrecycler)
        val profilepic: CircleImageView = v.findViewById(R.id.profilepic)
        val edit: Button = v.findViewById(R.id.edit)
        posts = v.findViewById(R.id.posts)
        Glide.with(activity).load(user.photoUrl).error(activity.getDrawable(R.drawable.notfound)).into(profilepic)
        username.text = user.displayName

        myquotesrecycler.postDelayed({
            carregar()
        }, 100)

        profilepic.setOnClickListener {
            val alert = Alert(activity)
            alert.Picalert(this)
        }

        edit.setOnClickListener {
            val alert = Alert(activity)
            alert.settings()
        }


    }


    override fun carregar() {
        val quotesDB = QuotesDB(activity)
        quotesDB.usercount = posts
        quotesDB.CarregarUserQuotes(user.uid)
        //To change body of created functions use File | Settings | File Templates.
    }

    fun reload() {
        carregar()
    }
}