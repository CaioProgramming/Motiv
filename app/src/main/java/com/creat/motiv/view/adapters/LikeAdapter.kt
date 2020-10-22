package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.contract.AdaptersContract
import com.creat.motiv.databinding.UserViewBinding
import com.creat.motiv.model.Beans.Likes
import com.creat.motiv.view.binders.UserTopBinder


class LikeAdapter(private val likesList: List<Likes>, private val context: Context) : RecyclerView.Adapter<LikeAdapter.MyViewHolder>(), AdaptersContract {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val userbind = UserViewBinding.inflate(LayoutInflater.from(context), null, false)

        return MyViewHolder(userbind)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val likes = likesList[holder.adapterPosition]
        UserTopBinder(likes.id, holder.userbind, context).apply {
            this.animateView()
        }
    }


    override fun getItemCount(): Int {
        return likesList.size
    }


    class MyViewHolder(val userbind: UserViewBinding) : RecyclerView.ViewHolder(userbind.root)
}

