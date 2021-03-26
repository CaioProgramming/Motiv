package com.ilustris.motiv.base.dialog.listdialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.ListDialogItemLayoutBinding

class ListDialogAdapter(val dialogItems: DialogItems, val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<ListDialogAdapter.ListItemViewHolder>() {

    inner class ListItemViewHolder(val listDialogItemLayoutBinding: ListDialogItemLayoutBinding) : RecyclerView.ViewHolder(listDialogItemLayoutBinding.root) {

        fun bind() {
            listDialogItemLayoutBinding.listItem = dialogItems[adapterPosition]
            listDialogItemLayoutBinding.dialogItemView.setOnClickListener {
                onItemClick.invoke(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val dialogItemBind: ListDialogItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_dialog_item_layout, parent, false)
        return ListItemViewHolder(dialogItemBind)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = dialogItems.size


}