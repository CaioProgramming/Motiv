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
import com.mikhaellopez.rxanimation.fadeIn


class ProfilePresenter(val activity: Activity, val profileFragment: FragmentProfileBinding, val user: User){


    init{
        profileFragment.profilepic.setOnClickListener {
            val alert = Alert(activity)
            alert.Picalert(this)
        }
        user.picurl?.let { loaduserpic(it) }
        user.name?.let { profileFragment.username.text = it }
        user.uid?.let {
            profileFragment.quotespager.adapter = QuotePagerAdapter(it,this)
            profileFragment.usertabs.setupWithViewPager(profileFragment.quotespager)
            profileFragment.usertabs.getTabAt(0)?.text = "Posts"
            profileFragment.usertabs.getTabAt(1)?.text = "Favoritos"
        }
        profileFragment.photoshimmer.startShimmer()
        var scrollRange = -1
        /*profileFragment.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1){
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0){
                profileFragment.collapsetoolbar.title = user.name
                profileFragment.toolbar.title = user.name
                isShow = true
            } else if (isShow){
                profileFragment.collapsetoolbar.title = " " //careful there should a space between double quote otherwise it wont work
                profileFragment.toolbar.title = " " //careful there should a space between double quote otherwise it wont work
                isShow = false
            }
        })*/

    }




    private fun loaduserpic(url:String){
        Glide.with(activity).load(url).error(activity.getDrawable(R.drawable.notfound)).into(profileFragment.profilepic)
        val handler = Handler()
        handler.postDelayed({
            profileFragment.photoshimmer.hideShimmer()
            profileFragment.profilepic.fadeIn()
        }, 1500)
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