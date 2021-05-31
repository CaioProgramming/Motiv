package com.creat.motiv.profile.view.adapters

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.UserCardBinding
import com.creat.motiv.profile.view.UserActivity
import com.google.firebase.auth.FirebaseAuth
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.Routes
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.databinding.UserItemBinding
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.base.utils.loadImage

class UserRecyclerAdapter(val userList: List<User>) : RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val userItemBinding: UserCardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.user_card, parent, false
        )
        return UserViewHolder(userItemBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(val userCardBinding: UserCardBinding) : RecyclerView.ViewHolder(userCardBinding.root) {

        fun bind() {
            val context = userCardBinding.root.context
            val currentUser = FirebaseAuth.getInstance().currentUser
            val user = userList[adapterPosition]
            userCardBinding.run {
                userpic.loadImage(user.picurl)
                usercover.loadGif(user.cover)
                username.text = user.name
                userCard.setOnClickListener {
                    if (user.uid != currentUser?.uid) {
                        Routes(context).openUserProfile(user, userpic)
                    }
                }
                if (user.admin) {
                    userpic.borderColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
                    userpic.borderWidth = 5
                } else {
                    userpic.borderColor = Color.TRANSPARENT
                    userpic.borderWidth = 0
                }
                userCard.slideInBottom()
            }
        }

    }

}