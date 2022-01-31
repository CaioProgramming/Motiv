package com.creat.motiv.features.newquote.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.animations.popIn
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.utils.defineTextAlignment
import com.ilustris.motiv.base.utils.getTypefaceStyle
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.StylePreviewCardBinding

class StylePreviewAdapter(
    var styles: ArrayList<Style>,
    private var selectedStyle: String? = null,
    private val onSelectStyle: (Int, Style) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun refreshStyle(style: Style) {
        styles.add(style)
        notifyItemInserted(itemCount)
    }

    fun updateStyle(index: Int, typeface: Typeface?) {
        styles[index].typeface = typeface
        notifyItemChanged(index)
    }

    fun setSelectedStyle(style: String) {
        selectedStyle = style
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
                    onSelectStyle.invoke(bindingAdapterPosition, quoteStyle)
                }
                if (!styleCard.isVisible) {
                    styleCard.popIn()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StylePreviewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.style_preview_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StylePreviewHolder).bind(styles[position])
    }

    override fun getItemCount(): Int {
        return styles.size
    }

}