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

import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class ProfilePresenter(val activity: Activity, val profileFragment: FragmentProfileBinding, val user: User){


    init{
        var quoteslist = ArrayList<QuoteHead>()
        var quotePagerAdapter:QuotePagerAdapter? = null
        profileFragment.profiledetails.profilepic.setOnClickListener {
            val alert = Alert(activity)
            alert.Picalert(this)
        }
        val userDB = UserDB(this)
        userDB.getuserquotes(user.uid!!, object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val quotesArrayList = ArrayList<Quotes>()
                quotesArrayList.clear()
                for (d in snapshot.children) {
                    val q = d.getValue(Quotes::class.java)
                    if (q != null) {
                        q.id = d.key
                        if (q.textcolor == 0 || q.backgroundcolor == 0) {
                            q.textcolor =  activity.titleColor
                            q.backgroundcolor = Color.TRANSPARENT
                        }
                        quotesArrayList.add(q)
                        println("Quotes " + quotesArrayList.size)
                        println("Quote  " + q.id)
                    }
                }
                val quoteHead = QuoteHead("Posts",quotesArrayList)
                quoteslist.add(quoteHead)
                val quotePagerAdapter = QuotePagerAdapter(quoteslist,activity)
                quotePagerAdapter.notifyDataSetChanged()
                counttab(quoteHead.quoteslist.size,profileFragment.usertabs.getTabAt(0)!!,quoteHead.titulo)

            }

        })
        userDB.getuserfavorites(user.uid!!, object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val quotesArrayList = ArrayList<Quotes>()
                quotesArrayList.clear()
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
                                    quotesArrayList.add(q)
                                    print("Loaded ${quotesArrayList.size} favorite quotes")
                                }
                            }
                        })
                        println("Quotes " + quotesArrayList.size)
                        println("Quote  " + q.id)
                    }
                }
                val quoteHead = QuoteHead("Favoritos ",quotesArrayList)
                quoteslist.add(quoteHead)
                quotePagerAdapter?.notifyDataSetChanged()
                counttab(quoteHead.quoteslist.size,profileFragment.usertabs.getTabAt(0)!!,quoteHead.titulo)
            }

        })
        Glide.with(activity)
                .load("https://assets-ouch.icons8.com/thumb/609/8f7caf84-037b-4aaa-94da-f965adb44fe0.png")
                .into(profileFragment.profiledetails.backpic)
        profileFragment.profiledetails.usernametext.text = user.name
        loaduserpic(user.uid!!)


        profileFragment.lists.adapter = quotePagerAdapter
        profileFragment.usertabs.setupWithViewPager(profileFragment.lists)
        profileFragment.settings.setOnClickListener { Alert.builder(activity).settings(this) }
        quotePagerAdapter?.notifyDataSetChanged()
        hideshimmer()
     }
    private fun loaduserpic(url:String){
        Glide.with(activity).load(url).error(activity.getDrawable(R.drawable.notfound)).into(profileFragment.profiledetails.profilepic)

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
            tab.text = "${amount} " + text
        }
    }

}