package com.ilustris.motiv.manager.ui.icons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilustris.animations.slideInRight
import com.ilustris.motiv.base.databinding.CoverLayoutBinding
import com.ilustris.motiv.base.utils.NEW_PIC
import com.ilustris.motiv.manager.R

class RecyclerGalleryAdapter(
    pictureList: ArrayList<String>? = ArrayList(),
    private val openPicker: () -> Unit
) : RecyclerView.Adapter<RecyclerGalleryAdapter.PictureViewHolder>() {

    var pictures = ArrayList<String>()

    init {
        if (pictureList.isNullOrEmpty()) pictures.add(NEW_PIC) else pictures.addAll(pictureList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cover_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(pictures[position])
    }


    override fun getItemCount(): Int = pictures.size
    fun updateList(images: ArrayList<String>) {
        if (itemCount == 6) pictures.remove(NEW_PIC)
        pictures.addAll(images)
        notifyDataSetChanged()
    }


    inner class PictureViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val picsLayoutBinding = CoverLayoutBinding.bind(itemView)

        fun bind(picture: String) {
            picsLayoutBinding.run {
                val context = picsLayoutBinding.root.context
                if (picture != NEW_PIC) {
                    Glide.with(context).load(picture).into(image)
                    card.setOnLongClickListener {
                        pictures.remove(picture)
                        notifyDataSetChanged()
                        false
                    }
                } else {
                    image.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_baseline_arrow_upward_24
                        )
                    )
                    image.setOnClickListener {
                        openPicker.invoke()
                    }
                }
                card.slideInRight()
            }
        }
    }
}
