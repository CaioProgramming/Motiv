package com.ilustris.motiv.base.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.databinding.PicsLayoutBinding

class RecyclerCoverAdapter(private var covers: List<Cover>,
                           private val onSelectCover: (Cover) -> Unit) : RecyclerView.Adapter<RecyclerCoverAdapter.CoverHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoverHolder {
        return CoverHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.pics_layout, parent, false)
        )
    }


    override fun onBindViewHolder(holder: CoverHolder, position: Int) {
        holder.bind(covers[position])
    }


    override fun getItemCount(): Int = covers.size


    inner class CoverHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(cover: Cover) {
            PicsLayoutBinding.bind(itemView).run {
                Glide.with(itemView.context).load(cover.url).into(image)
                card.setOnClickListener {
                    onSelectCover.invoke(cover)
                }
            }


        }
    }
}
