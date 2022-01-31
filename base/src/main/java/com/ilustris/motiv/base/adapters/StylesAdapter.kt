package com.ilustris.motiv.base.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.databinding.StylePagerLayoutBinding

class StylesAdapter(val styles: ArrayList<Style>) :
    RecyclerView.Adapter<StylesAdapter.StyleHolder>() {


    inner class StyleHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val context: Context by lazy { itemView.context }

        fun bind(quoteStyle: Style) {
            StylePagerLayoutBinding.bind(itemView).run {
                Glide.with(context).asGif().load(quoteStyle.backgroundURL).centerCrop()
                    .into(styleGif)
            }
        }

    }

    fun refreshStyle(style: Style) {
        styles.add(style)
        notifyItemInserted(itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StyleHolder {
        return StyleHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.style_pager_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StyleHolder, position: Int) {
        holder.bind(styles[position])
    }

    override fun getItemCount(): Int {
        return styles.size
    }

}