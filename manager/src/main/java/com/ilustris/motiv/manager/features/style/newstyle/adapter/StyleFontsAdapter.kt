package com.ilustris.motiv.manager.features.style.newstyle.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.TabFontViewBinding

class StyleFontsAdapter(val typefaces: List<Typeface>, val onFontSelect: (Int) -> Unit) :
    RecyclerView.Adapter<StyleFontsAdapter.FontViewHolder>() {

    var selectedFont = 0

    fun updateFont(font: Int) {
        selectedFont = font
        notifyDataSetChanged()
    }


    inner class FontViewHolder(private val tabFontViewBinding: TabFontViewBinding) :
        RecyclerView.ViewHolder(tabFontViewBinding.root) {
        fun bind() {
            tabFontViewBinding.run {
                val context = tabFontViewBinding.root.context
                fontText.typeface = typefaces[bindingAdapterPosition]
                if (bindingAdapterPosition == selectedFont) {
                    fontText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))

                } else {
                    fontText.setTextColor(ContextCompat.getColor(context, R.color.material_grey800))

                }
                itemView.setOnClickListener {
                    onFontSelect.invoke(bindingAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        val fontViewBinding = TabFontViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FontViewHolder(fontViewBinding)
    }

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = typefaces.size

}