package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.UserPicviewBinding
import com.creat.motiv.view.binders.UserPicBind

class CardLikeAdapter(val likes: List<String>, val context: Context) : RecyclerView.Adapter<CardLikeAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val userPicviewBinding = DataBindingUtil.inflate<UserPicviewBinding>(LayoutInflater.from(context), R.layout.user_picview, parent, false)
        return MyViewHolder(userPicviewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.run {
            UserPicBind(likes[position], context, userPicviewBinding)
        }
    }

    override fun getItemCount(): Int = if (likes.size <= 5) likes.size else 3

    class MyViewHolder(val userPicviewBinding: UserPicviewBinding) : RecyclerView.ViewHolder(userPicviewBinding.root)
}