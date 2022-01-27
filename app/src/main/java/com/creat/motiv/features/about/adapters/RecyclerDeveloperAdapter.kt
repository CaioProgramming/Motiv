package com.creat.motiv.features.about.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.DeveloperLayoutBinding
import com.ilustris.animations.popIn
import com.ilustris.motiv.base.beans.Developer

class RecyclerDeveloperAdapter(
    private var developerList: List<Developer> = emptyList(),
) : RecyclerView.Adapter<RecyclerDeveloperAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.developer_layout, parent, false)
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (developerList.isNotEmpty()) {
            holder.bind()
        }
    }


    override fun getItemCount(): Int = if (developerList.isNotEmpty()) developerList.size else 4


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind() {
            DeveloperLayoutBinding.bind(itemView).run {
                val developer = developerList[bindingAdapterPosition]
                developerName.text = developer.nome
                Glide.with(root.context).load(developer.photoURI).into(pic)
                developerCard.setOnClickListener {
                    val uri = Uri.parse(developer.socialLink)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    root.context.startActivity(intent)
                }
                developerCard.popIn()

            }
        }
    }
}
