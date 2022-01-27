package com.creat.motiv.features.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.ilustris.animations.popIn
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.databinding.UserPicviewBinding
import com.ilustris.motiv.base.utils.loadImage

class LikersAdapter(private val likes: ArrayList<User>, private val selectUser: (User) -> Unit) :
    RecyclerView.Adapter<LikersAdapter.UserLikePicHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserLikePicHolder {
        return UserLikePicHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_picview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserLikePicHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = if (likes.size <= 5) likes.size else 5

    fun refreshLikers(user: User) {
        likes.add(user)
        notifyItemInserted(itemCount)
    }

    inner class UserLikePicHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            UserPicviewBinding.bind(itemView).run {
                val user = likes[bindingAdapterPosition]
                userpic.loadImage(user.picurl)
                itemView.setOnClickListener { selectUser(user) }
                itemView.slideInBottom()
            }
        }
    }
}