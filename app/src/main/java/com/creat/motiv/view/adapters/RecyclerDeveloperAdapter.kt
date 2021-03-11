package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.DeveloperLayoutBinding
import com.creat.motiv.model.beans.Developer
import com.ilustris.animations.popIn
import com.ilustris.animations.slideUp

class RecyclerDeveloperAdapter(private var developerList: List<Developer> = emptyList(),
                               private val context: Context) : RecyclerView.Adapter<RecyclerDeveloperAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val picsBind: DeveloperLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.developer_layout, parent, false)
        return MyViewHolder(picsBind)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (developerList.isNotEmpty()) {
            developerList[position].run {
                Glide.with(context).load(this.photoURI).into(holder.picsLayoutBinding.pic)
                holder.picsLayoutBinding.developerName.text = this.nome
                holder.picsLayoutBinding.pic.popIn()
            }
        }


    }


    override fun getItemCount(): Int = if (developerList.isNotEmpty()) developerList.size else 4


    inner class MyViewHolder(val picsLayoutBinding: DeveloperLayoutBinding) : RecyclerView.ViewHolder(picsLayoutBinding.root)
}
