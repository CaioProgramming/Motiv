package com.ilustris.motiv.base.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.quote.beans.QuoteStyle
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.StylePagerLayoutBinding

class StylesAdapter(val styles: List<QuoteStyle>) : RecyclerView.Adapter<StylesAdapter.StyleHolder>() {


    inner class StyleHolder(val stylePagerLayoutBinding: StylePagerLayoutBinding) : RecyclerView.ViewHolder(stylePagerLayoutBinding.root) {

        val context: Context by lazy { stylePagerLayoutBinding.root.context }

        fun bind(quoteStyle: QuoteStyle) {
            Glide.with(context).asGif().load(quoteStyle.backgroundURL).centerCrop().into(stylePagerLayoutBinding.styleGif)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StyleHolder {
        return StyleHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.style_pager_layout, parent, false))
    }

    override fun onBindViewHolder(holder: StyleHolder, position: Int) {
        holder.bind(styles[position])
    }

    override fun getItemCount(): Int {
        return styles.size
    }

}