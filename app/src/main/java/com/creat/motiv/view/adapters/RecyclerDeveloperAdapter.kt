package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.PicsLayoutBinding
import com.creat.motiv.model.beans.Developer
import com.creat.motiv.utilities.popIn

class RecyclerDeveloperAdapter(private var developerList: List<Developer> = emptyList(),
                               private val context: Context) : RecyclerView.Adapter<RecyclerDeveloperAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val picsBind: PicsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pics_layout, parent, false)
        return MyViewHolder(picsBind)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        developerList[position].run {
            Glide.with(context).load(this.photoURI).into(holder.picsLayoutBinding.pic)
            holder.picsLayoutBinding.pic.popIn()
            holder.picsLayoutBinding.card.setCardBackgroundColor(context.resources.getColor(R.color.colorPrimaryDark))
        }


    }


    override fun getItemCount(): Int = developerList.size


    inner class MyViewHolder(val picsLayoutBinding: PicsLayoutBinding) : RecyclerView.ViewHolder(picsLayoutBinding.root)
}
