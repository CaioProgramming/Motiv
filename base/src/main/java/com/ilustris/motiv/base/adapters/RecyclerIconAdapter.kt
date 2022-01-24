package com.ilustris.motiv.base.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.beans.Icon
import com.ilustris.motiv.base.databinding.IconLayoutBinding
import com.ilustris.motiv.base.utils.NEW_PIC
import com.ilustris.motiv.base.utils.loadImage


private const val ICONVIEW = 0
private const val ADDICONVIEW = 1

class RecyclerIconAdapter(
    private var pictureList: ArrayList<Icon> = ArrayList(),
    private val onSelectIcon: (Icon) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType != ADDICONVIEW) IconHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.icon_layout, parent, false)
        ) else AddIconHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.add_icon_layout, parent, false)
        )

    }

    override fun getItemViewType(position: Int): Int {
        return if (pictureList[position].id == NEW_PIC) ADDICONVIEW else ICONVIEW
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IconHolder -> holder.bind()
            is AddIconHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int = pictureList.size

    inner class IconHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            IconLayoutBinding.bind(itemView).run {
                val context = itemView.context
                val icon = pictureList[bindingAdapterPosition]
                if (icon.id != NEW_PIC) {
                    iconImageview.loadImage(icon.uri)
                } else {
                    iconImageview.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_baseline_arrow_upward_24
                        )
                    )
                }
                iconImageview.setOnClickListener {
                    onSelectIcon(icon)
                }
                root.slideInBottom()
            }
        }
    }

    inner class AddIconHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.setOnClickListener {
                onSelectIcon(pictureList[bindingAdapterPosition])
            }
        }
    }
}
