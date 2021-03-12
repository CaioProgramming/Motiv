package com.creat.motiv.profile.view.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.profile.view.UserActivity
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.databinding.UserItemBinding

class UserRecyclerAdapter(val userList: List<User>) : RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val userItemBinding: UserItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.user_item, parent, false)
        return UserViewHolder(userItemBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userItemBinding.run {
            val user = userList[position]
            username.text = user.name
            Glide.with(holder.itemView.context).load(user.picurl).into(profilepic)
            userContainer.setOnClickListener {
                val context = holder.itemView.context
                val i = Intent(context, UserActivity::class.java)
                i.putExtra("USER", user)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,
                        profilepic as View, "profilepic")
                context.startActivity(i, options.toBundle())
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(val userItemBinding: UserItemBinding) : RecyclerView.ViewHolder(userItemBinding.root)

}