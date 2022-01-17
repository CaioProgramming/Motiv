package com.creat.motiv.profile.view.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.UserCardBinding
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.base.utils.loadImage

class UserRecyclerAdapter(val userList: List<User>) : RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
            UserCardBinding.bind(itemView).run {
                val context = root.context
                val user = userList[adapterPosition]
                userpic.loadImage(user.picurl)
                usercover.loadGif(user.cover)
                username.text = user.name
                userCard.setOnClickListener {

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