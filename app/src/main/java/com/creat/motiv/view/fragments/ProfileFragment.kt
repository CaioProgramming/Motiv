package com.creat.motiv.view.fragments


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.model.Beans.Quotes
import com.creat.motiv.model.Beans.User
import com.creat.motiv.model.UserDB
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.Pref
import com.creat.motiv.presenter.ProfilePresenter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private var preferences: Pref? = null
    var profilePresenter: ProfilePresenter? = null
    var fragmentbind: FragmentProfileBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentbind = DataBindingUtil.inflate(LayoutInflater.from(activity),R.layout.fragment_profile,null,false)
        val user = FirebaseAuth.getInstance().currentUser
        fragmentbind?.let{
            val act = activity as AppCompatActivity
            act.setSupportActionBar(it.toolbar)
            act.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            user?.let { create(it) }
            it.toolbar.setNavigationOnClickListener {
                val navview= act.findViewById<BottomNavigationView>(R.id.navigation)
                navview.selectedItemId = R.id.navigation_home
            }
            setHasOptionsMenu(true)

        }
        return fragmentbind!!.root

    }

    fun create(fireuser: FirebaseUser)
    {
        //preferences = Pref(Objects.requireNonNull<Context>(context))
        val userdb = UserDB()
        userdb.getUser(fireuser.uid, object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var user = User()
                    val  u  = dataSnapshot.getValue(User::class.java)
                    if (u != null) {
                        user.name = u.name
                        user.email = u.email
                        user.picurl = u.picurl
                        user.token = u.token
                        user.uid = u.uid
                        user.phonenumber = u.phonenumber
                        profilePresenter = ProfilePresenter(activity!!,fragmentbind!!,user)
                    }else{
                        activity?.let { Alert.builder(it).snackmessage(R.drawable.ic_error_outline_black_24dp,"Usuário não encontrado") }
                    }
                } else {
                    activity?.let { Alert.builder(it).snackmessage(null,"Usuário não encontrado") }
                }
            }

        })



    }

    fun create(user: User){
        profilePresenter = ProfilePresenter(activity!!,fragmentbind!!,user)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        activity?.let { profilePresenter?.let { it1 ->
            Alert.builder(it).settings(it1)
            return true
        }
        }
        return false
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profilemenu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun Tutorial() {
        val novo = Objects.requireNonNull<Bundle>(Objects.requireNonNull<FragmentActivity>(activity).intent.extras).getBoolean("novo")

        if (novo) {

            val preferences = Pref(Objects.requireNonNull<Context>(context))
            if (!preferences.profiletutorialstate()) {
                preferences.setProfileTutorial(true)
                val a = Alert(activity!!)
                a.message(activity!!.getDrawable(R.drawable.ic_mobile_post_monochrome), getString(R.string.profile_intro))

            }


        }
    }









}// Required empty public constructor
