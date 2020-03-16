package com.creat.motiv.view.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.contract.AdaptersContract
import com.creat.motiv.model.Beans.Likes
import com.creat.motiv.model.Beans.User
import com.creat.motiv.model.UserDB
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.ColorUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView


class LikeAdapter(private val likesList: List<Likes>, private val activity: Activity) : RecyclerView.Adapter<LikeAdapter.MyViewHolder>(), AdaptersContract {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(activity)
        view = mInflater.inflate(R.layout.like_card, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userDB = UserDB()
        var user = User()
        val likes = likesList[holder.adapterPosition]
        userDB.getUser(likes.userid, object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val u = dataSnapshot.getValue(User::class.java)
                    if (u != null) {
                        user.name = u.name
                        user.email = u.email
                        user.picurl = u.picurl
                        user.token = u.token
                        user.uid = u.uid
                        user.phonenumber = u.phonenumber
                        user = u

                    } else {
                        activity.let { Alert.builder(it).snackmessage(ColorUtils.ERROR, "Erro ao encontrar usuário... ${likes.username}") }
                    }
                } else {
                    activity.let {
                        Alert.builder(it).snackmessage(ColorUtils.ERROR, "Usuário não encontrado")
                        user.picurl = likes.userpic
                        user.name = likes.username
                        user.uid = likes.userid
                        userDB.insertUser(user)
                    }
                }
            }

        })

        Log.println(Log.INFO, "loaded like ", likes.username)
        if (likes.userid == user.uid) {
            holder.nome.text = user.name
            Glide.with(activity).load(user.picurl).error(activity.getDrawable(R.drawable.notfound)).into(holder.pic)
        } else {
            holder.nome.text = likes.username
            Glide.with(activity).load(likes.userpic).error(activity.getDrawable(R.drawable.notfound)).into(holder.pic)
        }
        fadeIn(holder.pic, 500).andThen(fadeIn(holder.nome, 1000)).subscribe()


    }


    override fun getItemCount(): Int {
        return likesList.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nome: TextView
        var pic: CircleImageView


        init {
            nome = view.findViewById(R.id.username)
            pic = view.findViewById(R.id.userpic)


        }
    }
}

