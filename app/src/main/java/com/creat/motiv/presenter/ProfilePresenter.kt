package com.creat.motiv.presenter

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.model.Beans.User
import com.creat.motiv.utils.Alert
import com.creat.motiv.view.adapters.QuotePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth


class ProfilePresenter(val activity: Activity, val profileFragment: FragmentProfileBinding, val user: User){


    init{
        val fireuser = FirebaseAuth.getInstance().currentUser

        user.picurl?.let { loaduserpic(it) }
        user.uid?.let {
            profileFragment.quotespager.adapter = QuotePagerAdapter(it,this)
            profileFragment.usertabs.setupWithViewPager(profileFragment.quotespager)
            profileFragment.usertabs.getTabAt(0)?.text = "Posts"
            //profileFragment.usertabs.getTabAt(0)?.icon = activity.getDrawable(R.drawable.posts)
            //profileFragment.usertabs.getTabAt(1)?.text =  "Favoritos"
            profileFragment.usertabs.getTabAt(1)?.icon = activity.getDrawable(R.drawable.favorites)
            if (fireuser != null &&  it == fireuser.uid) {
                profileFragment.profilepic.setOnClickListener {
                    val alert = Alert(activity)
                    alert.Picalert(this)
                }
            }
            profileFragment.quotespager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    when (position) {
                        0 -> print("Posts")
                        else -> print("Favoritos")
                    }
                }


                override fun onPageSelected(position: Int) {
                    val navigation: TabLayout = profileFragment.usertabs
                    val inanimation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_bottom)
                    val inanimation2 = AnimationUtils.loadAnimation(activity, R.anim.slide_in_top)
                    when (position) {
                        0 -> {
                            navigation.getTabAt(position)?.text = "Posts"
                            navigation.getTabAt(0)?.icon = null
                            navigation.getTabAt(position)?.view?.startAnimation(inanimation)
                            navigation.getTabAt(1)?.icon = activity.getDrawable(R.drawable.favorites)
                            navigation.getTabAt(1)?.text = ""
                            navigation.getTabAt(1)?.view?.startAnimation(inanimation2)


                        }
                        1 -> {
                            navigation.getTabAt(position)?.text = "Favoritos"
                            navigation.getTabAt(position)?.icon = null
                            navigation.getTabAt(position)?.view?.startAnimation(inanimation)
                            navigation.getTabAt(0)?.icon = activity.getDrawable(R.drawable.posts)
                            navigation.getTabAt(0)?.text = ""
                            navigation.getTabAt(0)?.view?.startAnimation(inanimation2)

                        }

                    }
                }

            })

        }


    }




    private fun loaduserpic(url:String){
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