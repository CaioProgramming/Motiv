package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.contract.AdaptersContract
import com.creat.motiv.databinding.UserQuoteCardViewBinding
import com.creat.motiv.model.beans.Likes
import com.creat.motiv.view.binders.UserViewBinder


class LikeAdapter(private val likesList: List<Likes>, private val context: Context) : RecyclerView.Adapter<LikeAdapter.MyViewHolder>(), AdaptersContract {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val userbind: UserQuoteCardViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.user_quote_card_view, parent, false)

        return MyViewHolder(userbind)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val likes = likesList[holder.adapterPosition]
        UserViewBinder(likes.id, context, holder.userbind)
    }


    override fun getItemCount(): Int {
        return likesList.size
    }


    class MyViewHolder(val userbind: UserQuoteCardViewBinding) : RecyclerView.ViewHolder(userbind.root)
}

