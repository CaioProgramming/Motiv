package com.ilustris.motiv.base.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.databinding.PicsLayoutBinding

class RecyclerCoverAdapter(private var covers: List<Cover>,
                           private val onSelectCover: (Cover) -> Unit) : RecyclerView.Adapter<RecyclerCoverAdapter.CoverHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoverHolder {
        val context = parent.context
        return CoverHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pics_layout, parent, false))
    }


    override fun onBindViewHolder(holder: CoverHolder, position: Int) {
        holder.bind(covers[position])
    }


    override fun getItemCount(): Int = covers.size


    inner class CoverHolder(val picsLayoutBinding: PicsLayoutBinding) : RecyclerView.ViewHolder(picsLayoutBinding.root) {
        val context: Context by lazy { picsLayoutBinding.root.context }

        fun bind(cover: Cover) {
            Glide.with(context).load(cover.url).into(picsLayoutBinding.pic)
            picsLayoutBinding.card.setOnClickListener {
                onSelectCover.invoke(cover)
            }

        }
    }
}
