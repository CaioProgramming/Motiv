package com.creat.motiv.features.newquote.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.defineTextAlignment
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

    fun updateStyles(list: List<Style>) {
        styles.clear()
        styles.addAll(styles)
        notifyDataSetChanged()
    }

    fun setSelectedStyle(style: String) {
        selectedStyle = style
        notifyDataSetChanged()

    }

    inner class StylePreviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(quoteStyle: Style) {
            StylePreviewCardBinding.bind(itemView).run {
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
                    onSelectStyle.invoke(bindingAdapterPosition, quoteStyle)
                }
                if (!styleCard.isVisible) {
                    styleCard.slideInBottom()
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