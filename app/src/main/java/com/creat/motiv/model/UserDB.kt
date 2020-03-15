package com.creat.motiv.model

import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.adapters.RecyclerAdapter
import com.creat.motiv.model.Beans.Quotes
import com.creat.motiv.model.Beans.User
import com.creat.motiv.presenter.ProfilePresenter
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.ColorUtils.ERROR
import com.creat.motiv.utils.ColorUtils.SUCCESS
import com.creat.motiv.utils.Tools
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.mikhaellopez.rxanimation.fadeIn
import de.hdodenhof.circleimageview.CircleImageView

class UserDB: ValueEventListener {

    private var profilePresenter: ProfilePresenter? = null
    private var recyclerView: RecyclerView ? = null
    private var notfound: TextView ? = null
    constructor(profilePresenter: ProfilePresenter?) {
        this.profilePresenter = profilePresenter
    }

    constructor()


    private val userref = Tools.userreference

    fun insertUser(user: FirebaseUser) {
        val userreference = FirebaseDatabase.getInstance().getReference("Users").child(user.uid)
        userreference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val u = User()
                u.name = user.displayName!!
                u.email = user.email!!
                u.picurl = user.photoUrl.toString()
                u.phonenumber = user.phoneNumber!!
                u.uid = user.uid
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
                    u.token = instanceIdResult.token
                    userreference.setValue(u)
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    fun insertUser(user: User) {
        val userreference = FirebaseDatabase.getInstance().getReference("Users").child(user.uid!!)
        userreference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val u = User()
                u.name = user.name
                u.email = user.email
                u.picurl = user.picurl.toString()
                u.phonenumber = user.phonenumber
                u.uid = user.uid
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
                    u.token = instanceIdResult.token
                    userreference.setValue(u)
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    fun getUser( uid: String, valueEventListener: ValueEventListener){
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
                            a.snackmessage(SUCCESS, "Perfil atualizado com sucesso")
                            val userpic = profilePresenter!!.activity.findViewById<CircleImageView>(R.id.profilepic)
                            Glide.with(profilePresenter!!.activity).load(firebaseUser.photoUrl).into(userpic)

                        }
                    }
                }
            } else {
                a.snackmessage(ERROR, "Erro ao atualizar foto " + task.exception!!.message)
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
                            a.snackmessage(SUCCESS, "Perfil atualizado com sucesso")
                        }
                    }
                }
            } else {
                a.snackmessage(ERROR, "Erro ao atualizar dados " + task.exception!!.message)
            }
        }


    }

    private var quotesdb = Tools.quotesreference




    fun findfavorites(uid: String, recyclerView: RecyclerView, notfound: TextView) {
        Log.println(Log.INFO, "Quotes", "Loading favorites quotes from ${uid}")
        this.recyclerView = recyclerView
        this.notfound = notfound
        quotesdb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val quotesArrayList = ArrayList<Quotes>()
                for (d in dataSnapshot.children) {
                    var quotes: Quotes
                    val q = d.getValue(Quotes::class.java)
                    if (q != null) {
                        quotes = q
                        quotes.id = q.id
                        if (q.textcolor == 0 || q.backgroundcolor == 0) {
                            quotes.textcolor = Color.BLACK
                            quotes.backgroundcolor = Color.WHITE
                        }
                        quotesdb.child(quotes.id!!).child("likes").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    quotesArrayList.add(quotes)
                                    print("Loaded ${quotesArrayList.size} favtorite quotes")
                                    quotesArrayList.reverse()
                                    recyclerView.visibility = VISIBLE
                                    val llm = GridLayoutManager(profilePresenter!!.activity, Tools.spancount, GridLayoutManager.VERTICAL, false)
                                    recyclerView.setHasFixedSize(true)
                                    println(quotesArrayList.size)
                                    val myadapter = RecyclerAdapter(quotesArrayList, profilePresenter!!.activity)
                                    recyclerView.adapter = myadapter
                                    recyclerView.layoutManager = llm
                                    profilePresenter?.counttab(quotesArrayList.size, profilePresenter!!.profileFragment.usertabs.getTabAt(1)!!, "\n Favoritos")
                                }
                            }

                        })

                    }

                }
                if (quotesArrayList.size == 0)
                    notfound.text = "Nenhuma frase favorita encontrada..."
                notfound.fadeIn()

            }
        })


    }



    fun finduserquotes(uid: String, recyclerView: RecyclerView, notfound: TextView){
        Log.println(Log.INFO, "Quotes", "Loading quotes from ${uid}")
        this.recyclerView = recyclerView
        this.notfound = notfound
        quotesdb.orderByChild("userID").equalTo(uid).addListenerForSingleValueEvent(this)

    }

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
                    q.textcolor = profilePresenter!!.activity.titleColor
                    q.backgroundcolor = Color.TRANSPARENT
                }
                quotesArrayList.add(q)

            }
        }
        println("founded ${quotesArrayList.size} quotes")

        if (quotesArrayList.size > 0) {
            quotesArrayList.reverse()
            recyclerView?.adapter = RecyclerAdapter(quotesArrayList,profilePresenter!!.activity)
            recyclerView?.layoutManager = LinearLayoutManager(profilePresenter!!.activity,RecyclerView.VERTICAL,false)
            recyclerView?.let { Tools.fadeIn(it,900).subscribe() }
            profilePresenter?.counttab(quotesArrayList.size, profilePresenter!!.profileFragment.usertabs.getTabAt(0)!!, "\n Posts")
        }else{
            notfound?.let { Tools.fadeIn(it,500).ambWith(recyclerView?.let { it1 -> Tools.fadeOut(it1,500) }).subscribe() }
        } //To change body of created functions use File | Settings | File Templates.
    }

}
