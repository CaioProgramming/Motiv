package com.creat.motiv.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.ilustris.motiv.base.binder.UserViewBinder
import com.ilustris.motiv.base.databinding.UserQuoteCardViewBinding

class LikersRecyclerAdapter(val userList: List<String>) : RecyclerView.Adapter<LikersRecyclerAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val userItemBinding: UserQuoteCardViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.user_quote_card_view, parent, false)
        return UserViewHolder(userItemBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userQuoteCardViewBinding.run {
            UserViewBinder(userList[position], holder.userQuoteCardViewBinding)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(val userQuoteCardViewBinding: UserQuoteCardViewBinding) : RecyclerView.ViewHolder(userQuoteCardViewBinding.root)

}