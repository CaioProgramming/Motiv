package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.PicsLayoutBinding
import com.creat.motiv.utilities.NEW_PIC
import com.creat.motiv.utilities.repeatFade
import com.creat.motiv.utilities.slideInBottom

class RecyclerGalleryAdapter(var pictureList: ArrayList<String> = ArrayList(),
                             private val context: Context, private val openPicker: () -> Unit) : RecyclerView.Adapter<RecyclerGalleryAdapter.MyViewHolder>() {

    init {
        if (pictureList.isEmpty()) pictureList.add(NEW_PIC)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val picsBind: PicsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pics_layout, parent, false)
        return MyViewHolder(picsBind)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (pictureList.isNotEmpty() && position < pictureList.size) {
            val picture = pictureList[position]
            holder.picsLayoutBinding.run {
                if (picture != NEW_PIC) {
                    Glide.with(context).load(picture).into(pic)
                    card.setOnLongClickListener {
                        pictureList.add(position, NEW_PIC)
                        notifyItemChanged(position)
                        false
                    }
                } else {
                    pic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_arrow_upward_24))
                    pic.setOnClickListener {
                        openPicker.invoke()
                    }
                }
                card.slideInBottom()
            }
        } else {
            holder.picsLayoutBinding.card.repeatFade()
        }
    }


    override fun getItemCount(): Int = if (pictureList.isNotEmpty()) 5 else 6
    fun updateList(images: ArrayList<String>) {
        pictureList = images
        notifyDataSetChanged()
    }


    inner class MyViewHolder(val picsLayoutBinding: PicsLayoutBinding) : RecyclerView.ViewHolder(picsLayoutBinding.root)
}
