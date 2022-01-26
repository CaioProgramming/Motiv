package com.ilustris.motiv.manager.features.style.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.beans.NEW_STYLE_ID
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.defineTextAlignment
import com.ilustris.motiv.base.utils.getTypefaceStyle
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.manager.databinding.StyleCardBinding
import com.ilustris.motiv.manager.databinding.StylePreviewCardBinding

class StylePreviewAdapter(
    var styles: List<Style>,
    private val isPreview: Boolean = false,
    private var selectedStyle: String? = null,
    val onSelectStyle: (Style) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateStyles(list: List<Style>) {
        styles = list
        notifyDataSetChanged()
    }

    fun setSelectedStyle(style: String) {
        selectedStyle = style
        notifyDataSetChanged()

    }

    inner class StylePreviewHolder(private val stylePreviewCardBinding: StylePreviewCardBinding) : RecyclerView.ViewHolder(stylePreviewCardBinding.root) {

        fun bind(quoteStyle: Style) {

            stylePreviewCardBinding.run {
                val context: Context = root.context
                styleImage.loadGif(quoteStyle.backgroundURL)
                styleText.run {
                    typeface = FontUtils.getTypeFace(context, quoteStyle.font)
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
                    onSelectStyle.invoke(quoteStyle)
                }
                if (!styleCard.isVisible) {
                    styleCard.fadeIn()
                }
            }
        }

    }

    inner class StyleHolder(private val stylePreviewCardBinding: StyleCardBinding): RecyclerView.ViewHolder(stylePreviewCardBinding.root) {

        fun bind(quoteStyle: Style) {

            stylePreviewCardBinding.run {
                val context: Context = root.context
                styleImage.loadGif(quoteStyle.backgroundURL)
                styleText.run {
                    FontUtils.getTypeFace(context, quoteStyle.font)?.let {
                        setTypeface(it, quoteStyle.fontStyle.getTypefaceStyle())
                    }
                    defineTextAlignment(quoteStyle.textAlignment)
                    setTextColor(Color.parseColor(quoteStyle.textColor))
                }

                styleCard.setOnClickListener {
                    onSelectStyle.invoke(quoteStyle)
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
            StyleCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else StylePreviewHolder(
            StylePreviewCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
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