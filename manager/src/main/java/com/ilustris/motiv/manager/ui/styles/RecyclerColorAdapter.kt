package com.ilustris.motiv.manager.ui.styles

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.ilustris.motiv.base.utils.blurView
import com.ilustris.motiv.base.utils.unblurView
import com.ilustris.motiv.base.utils.ColorUtils.toHex
import com.ilustris.motiv.base.SelectedViewType
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.ColorCardBinding
import java.util.*




class RecyclerColorAdapter(private val colorsList: ArrayList<Int>,
                           private val context: Context,
                           private val onColorPick: ((String) -> Unit)) : RecyclerView.Adapter<RecyclerColorAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val colorCardBinding: ColorCardBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.color_card, parent, false)
        return MyViewHolder(colorCardBinding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.colorCardBinding.run {
            this.colorcard.backgroundTintList = ColorStateList.valueOf(colorsList[position])
            val animation = AnimationUtils.loadAnimation(context, R.anim.fui_slide_in_right)
            colorcard.startAnimation(animation)
            colorcard.setOnClickListener {
                val hexColor = toHex(colorsList[position])
                onColorPick(hexColor)
            }
        }

    }


    override fun getItemCount(): Int = colorsList.size

    class MyViewHolder(val colorCardBinding: ColorCardBinding) : RecyclerView.ViewHolder(colorCardBinding.root)
}



