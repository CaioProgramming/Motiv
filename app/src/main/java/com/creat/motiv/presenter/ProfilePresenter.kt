package com.creat.motiv.presenter

import android.app.Activity
import android.os.CountDownTimer
import android.os.Handler
import android.text.Html
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.adapters.QuotePagerAdapter
import com.creat.motiv.adapters.RecyclerAdapter
import com.creat.motiv.utils.Alert
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.model.Beans.Quotes
import com.creat.motiv.model.Beans.User
import com.creat.motiv.model.UserDB
import com.creat.motiv.utils.Tools
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*
import kotlin.collections.ArrayList


class ProfilePresenter(val activity: Activity, val profileFragment: FragmentProfileBinding,val user:User){


   init{
       profileFragment.profiledetails.profilepic.setOnClickListener {
            val alert = Alert(activity)
            alert.Picalert(this)
        }
       Glide.with(activity)
               .load("https://assets-ouch.icons8.com/thumb/609/8f7caf84-037b-4aaa-94da-f965adb44fe0.png")
               .into(profileFragment.profiledetails.backpic)
       profileFragment.profiledetails.usernametext.text = user.name
       val quotePagerAdapter = QuotePagerAdapter(this)
       profileFragment.lists.adapter = quotePagerAdapter
       profileFragment.usertabs.setupWithViewPager(profileFragment.lists)
       profileFragment.settings.setOnClickListener {Alert.builder(activity).settings(this) }
       loaduserpic(user.uid)
       hideshimmer()
     }
    fun loaduserpic(url:String){
        Glide.with(activity).load(url).error(activity.getDrawable(R.drawable.notfound)).into(profileFragment.profiledetails.profilepic)

    }
    fun hideshimmer(){
        val handler = Handler()
        handler.postDelayed({
            profileFragment.topshimmer.hideShimmer()
        },2000)


    }

}