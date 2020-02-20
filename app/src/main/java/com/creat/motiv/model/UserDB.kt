package com.creat.motiv.model

import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.model.Beans.User
import com.creat.motiv.R
import com.creat.motiv.adapters.RecyclerAdapter
import com.creat.motiv.model.Beans.Quotes
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.Tools
import com.creat.motiv.presenter.ProfilePresenter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlin.collections.ArrayList

class UserDB {

    private var profilePresenter: ProfilePresenter? = null

    constructor(profilePresenter: ProfilePresenter?) {
        this.profilePresenter = profilePresenter
    }

    constructor()


    private val userref = Tools.userreference


    fun getUser( uid: String,valueEventListener: ValueEventListener){
        userref.child(uid).addValueEventListener(valueEventListener)
    }


    fun changeuserpic( pic: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val a = Alert(profilePresenter!!.activity)
        val profileChangeRequest = UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(pic)).build()
        firebaseUser!!.updateProfile(profileChangeRequest).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
                    val u = User()
                    u.uid = firebaseUser.uid
                    if (firebaseUser.phoneNumber != null) {
                        u.phonenumber = firebaseUser.phoneNumber!!
                    }

                    u.picurl = firebaseUser.photoUrl.toString()
                    u.email = firebaseUser.email!!
                    u.name = firebaseUser.displayName!!
                    u.token = instanceIdResult.token
                    userref.child(u.uid!!).setValue(u).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            a.message(profilePresenter!!.activity.getDrawable(R.drawable.ic_success), "Perfil atualizado com sucesso")
                            val userpic = profilePresenter!!.activity.findViewById<CircleImageView>(R.id.profilepic)
                            Glide.with(profilePresenter!!.activity).load(firebaseUser.photoUrl).into(userpic)

                        }
                    }
                }
            } else {
                a.message(a.error, "Erro ao atualizar foto " + task.exception!!.message)
            }
        }


    }

    fun changeusername(name: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val profileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        val a = Alert(profilePresenter!!.activity)


        firebaseUser!!.updateProfile(profileChangeRequest).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
                    val u = User()
                    u.uid = firebaseUser.uid
                    u.picurl = firebaseUser.photoUrl.toString()
                    u.email = firebaseUser.email!!
                    u.name = firebaseUser.displayName!!
                    u.token = instanceIdResult.token
                    userref.child(u.uid!!).setValue(u).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            a.message(profilePresenter!!.activity.getDrawable(R.drawable.ic_success), "Perfil atualizado com sucesso")
                        }
                    }
                }
            } else {
                a.message(a.error, "Erro ao atualizar dados " + task.exception!!.message)
            }
        }


    }

    public var quotesdb = Tools.quotesreference


    fun getuserquotes(uid: String,valueEventListener: ValueEventListener){
        quotesdb.orderByChild("userID").equalTo(uid).addListenerForSingleValueEvent(valueEventListener)

    }
    fun getuserfavorites(uid: String,valueEventListener: ValueEventListener){
        quotesdb.addListenerForSingleValueEvent(valueEventListener)

    }

    fun findfavorites(uid: String,recyclerView: RecyclerView) {
        quotesdb.addListenerForSingleValueEvent(object : ValueEventListener {
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
                            q.textcolor = profilePresenter!!.activity.titleColor
                            q.backgroundcolor = Color.TRANSPARENT
                        }
                        quotesdb.child(q.id!!).child("likes").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    quotesArrayList.add(q)
                                    print("Loaded ${quotesArrayList.size} favorite quotes")
                                    /*val alert = Alert(activity)
                                    alert.loading()*/

                                }
                            }
                        })
                        println("Quotes " + quotesArrayList.size)
                        println("Quote  " + q.id)
                    }
                }

                recyclerView.adapter = RecyclerAdapter(quotesArrayList,profilePresenter!!.activity)
                recyclerView.layoutManager = LinearLayoutManager(profilePresenter!!.activity,RecyclerView.VERTICAL,false)
                /*profilePresenter!!.counttab(quotesArrayList.size,
                        profilePresenter!!.profileFragment.usertabs.getTabAt(0)!!," favoritos")*/
            }
        })
    }

    fun finduserquotes(uid: String,recyclerView: RecyclerView){
        Log.println(Log.INFO, "Quotes", "Loading quotes from ${uid}")
        var quoteslist = ArrayList<Quotes>()
        quotesdb.orderByChild("userID").equalTo(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                return//To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val quotesArrayList = ArrayList<Quotes>()
                quotesArrayList.clear()
                for (d in snapshot.children) {
                    val q = d.getValue(Quotes::class.java)
                    if (q != null) {
                        q.id = d.key
                        if (q.textcolor == 0 || q.backgroundcolor == 0) {
                            q.textcolor = profilePresenter!!.activity.titleColor
                            q.backgroundcolor = Color.TRANSPARENT
                        }
                        quoteslist.add(q)
                        println("Quotes " + quotesArrayList.size)
                        println("Quote  " + q.id)
                    }
                }


                recyclerView.adapter = RecyclerAdapter(quotesArrayList,profilePresenter!!.activity)
                recyclerView.layoutManager = LinearLayoutManager(profilePresenter!!.activity,RecyclerView.VERTICAL,false)
                /*profilePresenter!!.counttab(quotesArrayList.size,
                        profilePresenter!!.profileFragment.usertabs.getTabAt(0)!!," publicações")*/
            }
        })

    }

}
