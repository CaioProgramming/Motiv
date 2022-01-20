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

class RecyclerIconAdapter(
    private var pictureList: ArrayList<Icon> = ArrayList(),
    private val onSelectIcon: (Icon) -> Unit
) : RecyclerView.Adapter<RecyclerIconAdapter.IconHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconHolder {

        return IconHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.icon_layout, parent, false)
        )
    }


    override fun onBindViewHolder(holder: IconHolder, position: Int) {
        holder.bind()
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
}
