package com.creat.motiv.presenter

import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.adapters.QuotePagerAdapter
import com.creat.motiv.adapters.RecyclerAdapter
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.model.Beans.QuoteHead
import com.creat.motiv.model.Beans.Quotes
import com.creat.motiv.model.Beans.User
import com.creat.motiv.model.UserDB
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.Tools

import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.CompletableObserver


class ProfilePresenter(val activity: Activity, val profileFragment: FragmentProfileBinding, val user: User){


    init{
        var userquotes = ArrayList<Quotes>()
        var favoritequotes = ArrayList<Quotes>()
        profileFragment.profilepic.setOnClickListener {
            val alert = Alert(activity)
            alert.Picalert(this)
        }
        val userDB = UserDB(this)
        userDB.getuserquotes(user.uid!!, object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userquotes.clear()
                for (d in snapshot.children) {
                    val q = d.getValue(Quotes::class.java)
                    if (q != null) {
                        q.id = d.key
                        if (q.textcolor == 0 || q.backgroundcolor == 0) {
                            q.textcolor =  activity.titleColor
                            q.backgroundcolor = Color.TRANSPARENT
                        }
                        userquotes.add(q)
                        println("Quotes " + userquotes.size)
                        println("Quote  " + q.id)
                    }
                }



            }

        })
        userDB.getuserfavorites(user.uid!!, object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                favoritequotes.clear()
                for (d in dataSnapshot.children) {
                    val q = d.getValue(Quotes::class.java)
                    if (q != null) {
                        q.id = d.key
                        if (q.textcolor == 0 || q.backgroundcolor == 0) {
                            q.textcolor = activity.titleColor
                            q.backgroundcolor = Color.TRANSPARENT
                        }
                        userDB.quotesdb.child(q.id!!).child("likes").child(user.uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    favoritequotes.add(q)
                                    print("Loaded ${favoritequotes.size} favorite quotes")
                                }
                            }
                        })
                        println("Favorite Quotes " + favoritequotes.size)
                        println("Quote  " + q.id)
                    }
                }
            }

        })

        profileFragment.profiletoolbar.title = user.name
        loaduserpic(user.uid!!)
        hideshimmer()
        profileFragment.usertabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                print("deselected") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val wichlist = if (tab!!.position == 0) {
                    userquotes
                } else {
                    favoritequotes
                }
                Tools.fadeIn(profileFragment.loading,1000).andThen(Tools.fadeOut(profileFragment.quotes,1000))
                        .andThen(Tools.fadeOut(profileFragment.loading,500).ambWith(Tools.fadeIn(profileFragment.quotes,900)))
                        .doOnComplete {
                            Handler().postDelayed({ profileFragment.quotes.adapter = RecyclerAdapter(wichlist,activity) },1000)
                        }.subscribe()


            }

        })
        counttab(userquotes.size,profileFragment.usertabs.getTabAt(0)!!," posts")
        counttab(favoritequotes.size,profileFragment.usertabs.getTabAt(1)!!," favoritos")


        profileFragment.quotes.adapter = RecyclerAdapter(userquotes,activity)
        profileFragment.quotes.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)



    }
    private fun loaduserpic(url:String){
        Glide.with(activity).load(url).error(activity.getDrawable(R.drawable.notfound)).into(profileFragment.profilepic)

    }
    fun hideshimmer(){
        val handler = Handler()
        handler.postDelayed({
            profileFragment.topshimmer.hideShimmer()
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