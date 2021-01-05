package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.FontPagerBinding
import com.creat.motiv.utilities.TextUtils

class FontAdapter(val context: Context, var currentTextColor: Int) : RecyclerView.Adapter<FontAdapter.FontHolder>() {

    val fontList = TextUtils.fonts()

    inner class FontHolder(val fontPagerBinding: FontPagerBinding) : RecyclerView.ViewHolder(fontPagerBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontHolder {
        val fontPagerBinding: FontPagerBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.font_pager, parent, false)
        return FontHolder(fontPagerBinding)
    }

    override fun onBindViewHolder(holder: FontHolder, position: Int) {
        holder.fontPagerBinding.fontTextView.run {
            val font = fontList[position]
            text = font.name
            setTextColor(currentTextColor)
            typeface = TextUtils.getTypeFace(context, font.path)
        }
    }

    fun updateTextColor(newColor: Int) {
        currentTextColor = newColor
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return fontList.size
    }

}