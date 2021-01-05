package com.creat.motiv.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.UserQuoteCardViewBinding
import com.creat.motiv.view.binders.UserViewBinder

class LikersRecyclerAdapter(val userList: List<String>) : RecyclerView.Adapter<LikersRecyclerAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val userItemBinding: UserQuoteCardViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.user_quote_card_view, parent, false)
        return UserViewHolder(userItemBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userQuoteCardViewBinding.run {
            UserViewBinder(userList[position], holder.itemView.context, holder.userQuoteCardViewBinding).run {
                viewBind.username.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1)
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(val userQuoteCardViewBinding: UserQuoteCardViewBinding) : RecyclerView.ViewHolder(userQuoteCardViewBinding.root)

}