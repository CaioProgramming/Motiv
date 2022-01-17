package com.ilustris.motiv.base.dialog.listdialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.ListDialogItemLayoutBinding

class ListDialogAdapter(val dialogItems: DialogItems, val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<ListDialogAdapter.ListItemViewHolder>() {

    inner class ListItemViewHolder(val listDialogItemLayoutBinding: ListDialogItemLayoutBinding) :
        RecyclerView.ViewHolder(listDialogItemLayoutBinding.root) {

        fun bind() {
            listDialogItemLayoutBinding.dialogItemView.text = dialogItems[adapterPosition].text
            listDialogItemLayoutBinding.dialogItemView.setOnClickListener {
                onItemClick.invoke(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val dialogItemBind: ListDialogItemLayoutBinding = ListDialogItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListItemViewHolder(dialogItemBind)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = dialogItems.size


}