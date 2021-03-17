package com.ilustris.motiv.manager.ui.styles

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.animations.bounce
import com.ilustris.animations.fadeIn
import com.ilustris.animations.popIn
import com.ilustris.motiv.base.beans.DEFAULT_TEXT_COLOR
import com.ilustris.motiv.base.beans.TextAlignment
import com.ilustris.motiv.base.beans.TextSize
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.defineTextAlignment
import com.ilustris.motiv.base.utils.defineTextSize
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.StyleTextPageLayoutBinding

class StyleFontAdapter : RecyclerView.Adapter<StyleFontAdapter.FontHolder>() {

    var styleTextColor: String = DEFAULT_TEXT_COLOR
    var currentAlignment = TextAlignment.CENTER
    var currentTextSize = TextSize.DEFAULT

    fun updateTextColor(newColor: String) {
        styleTextColor = newColor
        notifyDataSetChanged()
    }

    fun updateAlignment(textAlignment: TextAlignment) {
        currentAlignment = textAlignment
        notifyDataSetChanged()
    }

    fun updateTextSize(textSize: TextSize) {
        currentTextSize = textSize
        notifyDataSetChanged()
    }

    inner class FontHolder(private val styleTextPageLayoutBinding: StyleTextPageLayoutBinding) : RecyclerView.ViewHolder(styleTextPageLayoutBinding.root) {

        fun bind() {
            val context = itemView.context
            styleTextPageLayoutBinding.styleText.run {
                defineTextAlignment(currentAlignment)
                defineTextSize(currentTextSize)
                typeface = FontUtils.getTypeFace(context, adapterPosition)
                setTextColor(Color.parseColor(styleTextColor))
                fadeIn()
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontHolder {
        return FontHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.style_text_page_layout, parent, false))
    }

    override fun onBindViewHolder(holder: FontHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return FontUtils.fonts.size
    }

}