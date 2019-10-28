package com.creat.motiv.presenter

import android.app.Activity
import com.bumptech.glide.Glide
import com.creat.motiv.Model.QuotesDB
import com.creat.motiv.R
import com.creat.motiv.Utils.Alert
import com.creat.motiv.View.fragments.ProfileFragment
import com.creat.motiv.contract.ViewContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main2.profilepic
import kotlinx.android.synthetic.main.bottom_options.*
import kotlinx.android.synthetic.main.user_profile.*


class ProfilePresenter(val activity: Activity, val profileFragment: ProfileFragment) : ViewContract {
    val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    override fun initview() {
        val profilepic = profileFragment.profilepic
        val edit = profileFragment.edit

        Glide.with(activity).load(user.photoUrl).error(activity.getDrawable(R.drawable.notfound)).into(profilepic)
        profilepic.setOnClickListener {
            val alert = Alert(activity)
            alert.Picalert(this)
        }
        edit.setOnClickListener {
            val alert = Alert(activity)
            alert.settings()
        }

        carregar()

    }


    override fun carregar() {
        val pDB = QuotesDB(activity)
        pDB.recyclerView = profileFragment.myquotesrecycler!!
        pDB.usercount = profileFragment.posts
        pDB.CarregarUserQuotes(user.uid)
        //To change body of created functions use File | Settings | File Templates.
    }

    fun reload() {
        carregar()
    }
}