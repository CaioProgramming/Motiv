package com.ilustris.motiv.manager.ui.styles

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.motiv.base.beans.FontStyle
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.TabFontViewBinding

class StyleFontsAdapter(val onFontSelect: (Int) -> Unit) :
    RecyclerView.Adapter<StyleFontsAdapter.FontViewHolder>() {

    var selectedFont = 0
    var fontStyle = FontStyle.REGULAR

    fun updateFont(font: Int, newFontStyle: FontStyle) {
        selectedFont = font
        fontStyle = newFontStyle
        notifyDataSetChanged()
    }


    inner class FontViewHolder(private val tabFontViewBinding: TabFontViewBinding) :
        RecyclerView.ViewHolder(tabFontViewBinding.root) {
        fun bind() {
            tabFontViewBinding.run {
                val context = tabFontViewBinding.root.context
                fontText.typeface = FontUtils.getTypeFace(context, adapterPosition)
                if (adapterPosition == selectedFont) {
                    fontText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                    background.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorAccent
                        )
                    )
                } else {
                    fontText.setTextColor(ContextCompat.getColor(context, R.color.material_grey800))
                    background.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.material_grey500
                        )
                    )
                }
                background.setOnClickListener {
                    onFontSelect.invoke(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        val fontViewBinding = DataBindingUtil.inflate<TabFontViewBinding>(
            LayoutInflater.from(parent.context),
            R.layout.tab_font_view,
            parent,
            false
        )
        return FontViewHolder(fontViewBinding)
    }

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = FontUtils.fonts.size

}