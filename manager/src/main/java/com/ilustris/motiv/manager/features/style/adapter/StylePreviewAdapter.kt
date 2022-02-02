package com.ilustris.motiv.manager.features.style.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.beans.NEW_STYLE_ID
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.utils.defineTextAlignment
import com.ilustris.motiv.base.utils.getTypefaceStyle
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.StyleCardBinding
import com.ilustris.motiv.manager.databinding.StylePreviewCardBinding

class StylePreviewAdapter(
    var styles: List<Style>,
    private var isPreview: Boolean = false,
    private var selectedStyle: String? = null,
    val onSelectStyle: (Style, Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateStyles(list: List<Style>) {
        styles = list
        notifyDataSetChanged()
    }

    fun setSelectedStyle(style: String) {
        selectedStyle = style
        notifyDataSetChanged()

    }

    fun updatePreview(preview: Boolean) {
        isPreview = preview
        notifyDataSetChanged()
    }

    inner class StylePreviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(quoteStyle: Style) {

            StylePreviewCardBinding.bind(itemView).run {
                styleImage.loadGif(quoteStyle.backgroundURL)
                styleText.run {
                    setTypeface(quoteStyle.typeface, quoteStyle.fontStyle.getTypefaceStyle())
                    defineTextAlignment(quoteStyle.textAlignment)
                    setTextColor(Color.parseColor(quoteStyle.textColor))
                    quoteStyle.shadowStyle.run {
                        setShadowLayer(radius, dx, dy, Color.parseColor(shadowColor))
                    }
                }
                selectedStyle?.let {
                    styleCard.isSelected = quoteStyle.id == it
                }
                styleCard.setOnClickListener {
                    onSelectStyle.invoke(quoteStyle, bindingAdapterPosition)
                }

            }
        }

    }

    inner class StyleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(quoteStyle: Style) {

            StyleCardBinding.bind(itemView).run {
                styleImage.loadGif(quoteStyle.backgroundURL)
                styleText.run {
                    setTypeface(quoteStyle.typeface, quoteStyle.fontStyle.getTypefaceStyle())
                    defineTextAlignment(quoteStyle.textAlignment)
                    setTextColor(Color.parseColor(quoteStyle.textColor))
                }
                selectedStyle?.let {
                    styleCard.isSelected = quoteStyle.id == it
                }

                styleCard.setOnClickListener {
                    onSelectStyle.invoke(quoteStyle, bindingAdapterPosition)
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
        return if (!isPreview) StyleHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.style_card,
                parent,
                false
            )
        )
        else StylePreviewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.style_preview_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (!holder.itemView.isVisible) {
            holder.itemView.fadeIn()
        }
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