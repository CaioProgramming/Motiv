package com.ilustris.motiv.manager.features.icon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.databinding.IconLayoutBinding
import com.ilustris.motiv.base.utils.NEW_PIC
import com.ilustris.motiv.manager.R

private const val ICONVIEW = 0
private const val ADDICONVIEW = 1

class RecyclerGalleryAdapter(
    pictureList: ArrayList<String>? = ArrayList(),
    private val openPicker: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var pictures = ArrayList<String>()

    init {
        if (pictureList.isNullOrEmpty()) pictures.add(NEW_PIC) else pictures.addAll(pictureList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ICONVIEW) PictureViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.icon_layout, parent, false)
        ) else NewIconHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.add_icon_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PictureViewHolder -> {
                holder.bind()
            }
            is NewIconHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (pictures[position] == NEW_PIC) ADDICONVIEW else ICONVIEW
    }

    override fun getItemCount(): Int = pictures.size
    fun updateList(images: ArrayList<String>) {
        if (itemCount == 6) pictures.remove(NEW_PIC)
        pictures.addAll(images)
        notifyDataSetChanged()
    }


    inner class PictureViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val picsLayoutBinding = IconLayoutBinding.bind(itemView)

        fun bind() {
            picsLayoutBinding.run {
                val picture = pictures[bindingAdapterPosition]
                val context = picsLayoutBinding.root.context
                Glide.with(context).load(picture).into(iconImageview)
                root.setOnLongClickListener {
                    pictures.remove(picture)
                    notifyItemRemoved(bindingAdapterPosition)
                    false
                }
                root.slideInBottom()
            }
        }
    }

    inner class NewIconHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.setOnClickListener {
                openPicker()
            }
        }
    }

}
