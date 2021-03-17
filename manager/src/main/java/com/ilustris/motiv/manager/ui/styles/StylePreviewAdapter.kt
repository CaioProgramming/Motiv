package com.ilustris.motiv.manager.ui.styles

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilustris.motiv.base.beans.NEW_STYLE_ID
import com.ilustris.motiv.base.beans.QuoteStyle
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.defineTextAlignment
import com.ilustris.motiv.base.utils.defineTextSize
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.StyleCardBinding
import com.ilustris.motiv.manager.databinding.StylePreviewCardBinding

class StylePreviewAdapter(private var styles: List<QuoteStyle>,
                          private val isPreview: Boolean = false,
                          private var selectedStyle: String? = null,
                          val onRequestDelete: (QuoteStyle) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateStyles(list: List<QuoteStyle>) {
        styles = list
        notifyDataSetChanged()
    }

    fun setSelectedStyle(style: String) {
        selectedStyle = style
        notifyDataSetChanged()
    }

    inner class StylePreviewHolder(private val stylePreviewCardBinding: StylePreviewCardBinding) : RecyclerView.ViewHolder(stylePreviewCardBinding.root) {

        fun bind(quoteStyle: QuoteStyle) {

            stylePreviewCardBinding.run {
                val context: Context = root.context
                styleImage.loadGif(quoteStyle.backgroundURL)
                styleText.run {
                    typeface = FontUtils.getTypeFace(context, quoteStyle.font)
                    defineTextSize(quoteStyle.textSize)
                    defineTextAlignment(quoteStyle.textAlignment)
                    setTextColor(Color.parseColor(quoteStyle.textColor))
                }
                selectedStyle?.let {
                    previewCard.isSelected = quoteStyle.id == it
                }
                previewCard.setOnClickListener {
                    onRequestDelete.invoke(quoteStyle)
                }
            }
        }

    }

    inner class StyleHolder(private val stylePreviewCardBinding: StyleCardBinding): RecyclerView.ViewHolder(stylePreviewCardBinding.root) {

        fun bind(quoteStyle: QuoteStyle) {

            stylePreviewCardBinding.run {
                val context: Context = root.context
                styleImage.loadGif(quoteStyle.backgroundURL)
                styleText.run {
                    typeface = FontUtils.getTypeFace(context, quoteStyle.font)
                    defineTextSize(quoteStyle.textSize)
                    defineTextAlignment(quoteStyle.textAlignment)
                    setTextColor(Color.parseColor(quoteStyle.textColor))
                }



                styleCard.setOnClickListener {
                    onRequestDelete.invoke(quoteStyle)
                }
                if (quoteStyle.id == NEW_STYLE_ID) {
                    styleText.text = "Criar novo estilo"
                } else {
                    styleText.text = "Motiv"
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (!isPreview) StyleHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.style_card, parent, false))
        else StylePreviewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.style_preview_card, parent, false))
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