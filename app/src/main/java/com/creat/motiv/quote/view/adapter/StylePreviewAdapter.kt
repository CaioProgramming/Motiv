package com.creat.motiv.quote.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.beans.NEW_STYLE_ID
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.defineTextAlignment
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.StyleCardBinding
import com.ilustris.motiv.manager.databinding.StylePreviewCardBinding

class StylePreviewAdapter(var styles: List<Style>,
                          private var selectedStyle: String? = null,
                          private val onSelectStyle: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                    onSelectStyle.invoke(adapterPosition)
                }
                if (styleCard.visibility == View.GONE) {
                    styleCard.fadeIn()
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StylePreviewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.style_preview_card, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StylePreviewHolder).bind(styles[position])
    }

    override fun getItemCount(): Int {
        return styles.size
    }

}