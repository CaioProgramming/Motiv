package com.creat.motiv.presenter

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Handler
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.adapters.QuotePagerAdapter
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.model.Beans.User
import com.creat.motiv.utils.Alert

import com.google.android.material.tabs.TabLayout



class ProfilePresenter(val activity: Activity, val profileFragment: FragmentProfileBinding, val user: User){


    init{
        Alert(activity).loading()
        profileFragment.profilepic.setOnClickListener {
            val alert = Alert(activity)
            alert.Picalert(this)
        }
        loaduserpic(user.picurl!!)
        profileFragment.username.text = user.name
        profileFragment.quotespager.adapter = QuotePagerAdapter(  user.uid!!,activity)
        profileFragment.usertabs.setupWithViewPager(profileFragment.quotespager)
        profileFragment.usertabs.getTabAt(0)?.setIcon(R.drawable.posts)
        profileFragment.usertabs.getTabAt(1)?.setIcon(R.drawable.favorites)


    }




    private fun loaduserpic( url:String){
        Glide.with(activity).load(url).error(activity.getDrawable(R.drawable.notfound)).into(profileFragment.profilepic)

    }



    fun hideshimmer(){
        val handler = Handler()
        handler.postDelayed({
            //profileFragment.topshimmer.hideShimmer()
        },2000)


    }

    fun counttab(amount: Int, tab: TabLayout.Tab,text: String){
        val animator = ValueAnimator.ofInt(0,amount)
        animator.duration = 1500
        animator.addUpdateListener {
            tab.text = "$amount $text"
        }
    }

}