package com.ilustris.motiv.manager.ui.styles

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.quote.beans.NEW_STYLE_ID
import com.creat.motiv.quote.beans.QuoteStyle
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.defineTextAlignment
import com.ilustris.motiv.base.utils.defineTextSize
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.StyleCardBinding
import com.ilustris.motiv.manager.databinding.StylePreviewCardBinding

class StylePreviewAdapter(private val styles: List<QuoteStyle>, private val isPreview: Boolean = false, val onRequestDelete: (QuoteStyle) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class StylePreviewHolder(private val stylePreviewCardBinding: StylePreviewCardBinding): RecyclerView.ViewHolder(stylePreviewCardBinding.root) {

        fun bind(quoteStyle: QuoteStyle) {

            stylePreviewCardBinding.run {
                val context: Context = root.context
                Glide.with(context).load(quoteStyle.backgroundURL).into(styleImage)
                styleText.typeface = FontUtils.getTypeFace(context,quoteStyle.font)
                styleText.defineTextSize(quoteStyle.textSize)
                styleText.defineTextAlignment(quoteStyle.textAlignment)
                root.setOnLongClickListener {
                    onRequestDelete.invoke(quoteStyle)
                    false
                }
            }
        }

    }

    inner class StyleHolder(private val stylePreviewCardBinding: StyleCardBinding): RecyclerView.ViewHolder(stylePreviewCardBinding.root) {

        fun bind(quoteStyle: QuoteStyle) {

            stylePreviewCardBinding.run {
                val context: Context = root.context
                Glide.with(context).load(quoteStyle.backgroundURL).into(styleImage)
                styleText.typeface = FontUtils.getTypeFace(context,quoteStyle.font)
                styleText.defineTextSize(quoteStyle.textSize)
                styleText.defineTextAlignment(quoteStyle.textAlignment)
                root.setOnLongClickListener {
                    onRequestDelete.invoke(quoteStyle)
                    false
                }
                if (quoteStyle.id == NEW_STYLE_ID) {
                    styleText.text = "Criar novo estilo"
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (!isPreview) StyleHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.style_preview_card,parent,false))
        else StylePreviewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.style_preview_card,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isPreview) {
            (holder as StylePreviewHolder).bind(styles[position])
        } else {
            (holder as StyleHolder).bind(styles[position])
        }
    }

    override fun getItemCount(): Int {
        return styles.size
    }

}