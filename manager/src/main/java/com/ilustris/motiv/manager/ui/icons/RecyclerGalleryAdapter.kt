package com.ilustris.motiv.manager.ui.icons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.utilities.NEW_PIC
import com.ilustris.animations.popIn
import com.ilustris.animations.slideInRight
import com.ilustris.motiv.base.databinding.PicsLayoutBinding
import com.ilustris.motiv.manager.R

class RecyclerGalleryAdapter(pictureList: ArrayList<String>? = ArrayList(), private val openPicker: () -> Unit) : RecyclerView.Adapter<RecyclerGalleryAdapter.MyViewHolder>() {

    var pictures = ArrayList<String>()

    init {
        if (pictureList.isNullOrEmpty()) pictures.add(NEW_PIC) else pictures.addAll(pictureList)
        notifyDataSetChanged()
    }

    fun updateSaved(count: Int) {
        savedPics.add(count)
        notifyDataSetChanged()
    }

    var savedPics = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val context = parent.context
        val picsBind: PicsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pics_layout, parent, false)
        return MyViewHolder(picsBind)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(pictures[position])
    }


    override fun getItemCount(): Int = pictures.size
    fun updateList(images: ArrayList<String>) {
        if (itemCount == 6) pictures.remove(NEW_PIC)
        pictures.addAll(images)
        notifyDataSetChanged()
    }


    inner class MyViewHolder(private val picsLayoutBinding: PicsLayoutBinding) : RecyclerView.ViewHolder(picsLayoutBinding.root) {

        fun bind(picture: String) {
            picsLayoutBinding.run {
                val context = picsLayoutBinding.root.context
                if (picture != NEW_PIC) {
                    Glide.with(context).load(picture).into(pic)
                    card.setOnLongClickListener {
                        pictures.remove(picture)
                        notifyDataSetChanged()
                        false
                    }
                } else {
                    pic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_arrow_upward_24))
                    pic.setOnClickListener {
                        openPicker.invoke()
                    }
                }
                card.slideInRight()
            }
        }
    }
}
