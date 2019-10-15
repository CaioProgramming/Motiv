package com.creat.motiv.Model

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.TextView
import com.bumptech.glide.Glide
import com.creat.motiv.Model.Beans.User
import com.creat.motiv.R
import com.creat.motiv.Utils.Alert
import com.creat.motiv.Utils.Tools
import com.creat.motiv.View.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import de.hdodenhof.circleimageview.CircleImageView

class UserDB(private val activity: Activity) {
    private val userref = Tools.userreference

    fun LoadUser(userpic: CircleImageView, username: TextView, userr: User) {
        userref.child(userr.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    Glide.with(activity).load(user!!.picurl).error(activity.getDrawable(R.drawable.notfound)).into(userpic)
                    username.text = user.name
                    userr.uid = dataSnapshot.key!!
                    userr.name = user.name
                } else {
                    Log.println(Log.ERROR, "USER", "Can't found on database")
                    Glide.with(activity).load(userr.picurl).error(activity.getDrawable(R.drawable.notfound))
                    username.text = userr.name
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    fun changeuserpic(profileFragment: ProfileFragment?, pic: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val a = Alert(activity)
        val profileChangeRequest = UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(pic)).build()
        firebaseUser!!.updateProfile(profileChangeRequest).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
                    val u = User()
                    u.uid = firebaseUser.uid
                    u.phonenumber = firebaseUser.phoneNumber!!
                    u.picurl = firebaseUser.photoUrl.toString()
                    u.email = firebaseUser.email!!
                    u.name = firebaseUser.displayName!!
                    u.token = instanceIdResult.token
                    userref.child(u.uid).setValue(u).addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            a.Message(activity.getDrawable(R.drawable.ic_success), "Perfil atualizado com sucesso")
                            profileFragment?.reload()
                            val userpic = activity.findViewById<CircleImageView>(R.id.profilepic)
                            Glide.with(activity).load(firebaseUser.photoUrl).into(userpic)

                        }
                    }
                }
            } else {
                a.Message(a.erroricon, "Erro ao atualizar foto " + task.exception!!.message)
            }
        }


    }

    fun changeusername(name: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val profileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        val a = Alert(activity)


        firebaseUser!!.updateProfile(profileChangeRequest).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
                    val u = User()
                    u.uid = firebaseUser.uid
                    u.phonenumber = firebaseUser.phoneNumber!!
                    u.picurl = firebaseUser.photoUrl.toString()
                    u.email = firebaseUser.email!!
                    u.name = firebaseUser.displayName!!
                    u.token = instanceIdResult.token
                    userref.child(u.uid).setValue(u).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            a.Message(activity.getDrawable(R.drawable.ic_success), "Perfil atualizado com sucesso")
                        }
                    }
                }
            } else {
                a.Message(a.erroricon, "Erro ao atualizar dados " + task.exception!!.message)
            }
        }


    }

}
