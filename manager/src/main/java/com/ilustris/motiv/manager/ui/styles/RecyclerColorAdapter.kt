package com.ilustris.motiv.manager.ui.styles

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.animations.slideInBottom


import com.ilustris.motiv.base.utils.ColorUtils
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.ColorCardBinding
import java.util.*


class RecyclerColorAdapter(private val context: Context,
                           private val onColorPick: ((String) -> Unit)) : RecyclerView.Adapter<RecyclerColorAdapter.MyViewHolder>() {

    private val colorsList = ColorUtils.getColors(context)
    private var selectedTextColor: String? = null
    private var selectedShadowColor: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val colorCardBinding: ColorCardBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.color_card, parent, false)
        return MyViewHolder(colorCardBinding)
    }


    fun updateTextColor(textcolor: String) {
        selectedTextColor = textcolor
        notifyDataSetChanged()
    }

    fun updateShadowColor(shadowColor: String) {
        selectedShadowColor = shadowColor
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.colorCardBinding.run {
            val colorValue = colorsList[position]
            this.colorcard.setCardBackgroundColor(Color.parseColor(colorValue))
            colorcard.setOnClickListener {
                onColorPick(colorValue)
            }
            if (selectedTextColor == colorValue) {
                currentColor.setImageResource(R.drawable.ic_baseline_text_fields_24)
                colorcard.isChecked = true
                colorcard.isSelected = true
            } else if (selectedShadowColor == colorValue) {
                currentColor.setImageResource(R.drawable.ic_baseline_blur_on_24)
                colorcard.isChecked = true
                colorcard.isSelected = true
            } else {
                currentColor.setImageResource(0)
            }
            if (colorcard.visibility == View.GONE) {
                colorcard.slideInBottom()
            }

        }

    }


    override fun getItemCount(): Int = colorsList.size

    class MyViewHolder(val colorCardBinding: ColorCardBinding) : RecyclerView.ViewHolder(colorCardBinding.root)
}



