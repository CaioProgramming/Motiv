package com.creat.motiv.features.profile.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.databinding.UserItemBinding
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.base.utils.loadImage

class UserListRecyclerAdapter(
    private val users: List<User>,
    private val onSelectUser: (User) -> Unit
) : RecyclerView.Adapter<UserListRecyclerAdapter.UserViewHolder>() {


    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
            UserItemBinding.bind(itemView).run {
                val user = users[bindingAdapterPosition]
                profilepic.loadImage(user.picurl)
                username.text = user.name
                itemView.setOnClickListener {
                    onSelectUser(user)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = users.size

}