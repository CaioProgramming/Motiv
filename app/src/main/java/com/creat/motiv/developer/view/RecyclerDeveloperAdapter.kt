package com.creat.motiv.developer.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.DeveloperLayoutBinding
import com.ilustris.motiv.base.beans.Developer
import com.ilustris.animations.popIn
import com.ilustris.motiv.base.utils.WEB_URL

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
                holder.picsLayoutBinding.developerCard.setOnClickListener {
                    val uri = Uri.parse(socialLink)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                }

            }
        }


    }


    override fun getItemCount(): Int = if (developerList.isNotEmpty()) developerList.size else 4


    inner class MyViewHolder(val picsLayoutBinding: DeveloperLayoutBinding) : RecyclerView.ViewHolder(picsLayoutBinding.root)
}
