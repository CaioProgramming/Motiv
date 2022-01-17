package com.ilustris.motiv.manager.ui.styles

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.motiv.base.utils.ColorUtils
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.ColorCardBinding
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.visible
import java.util.*


class RecyclerColorAdapter(
    private val context: Context,
    private val selectedIcon: Int,
    private val onColorPick: ((String) -> Unit)
) : RecyclerView.Adapter<RecyclerColorAdapter.MyViewHolder>() {

    private val colorsList = ColorUtils.getColors(context)
    private var selectedTextColor: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val colorCardBinding: ColorCardBinding =
            ColorCardBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(colorCardBinding)
    }

    fun updateSelectedColor(textcolor: String) {
        selectedTextColor = textcolor
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.colorCardBinding.run {
            val colorValue = colorsList[position]
            currentColor.setImageDrawable(ContextCompat.getDrawable(context, selectedIcon))
            colorcard.setCardBackgroundColor(Color.parseColor(colorValue))
            colorcard.setOnClickListener {
                onColorPick(colorValue)
                selectedTextColor = colorValue
                notifyDataSetChanged()
            }
            colorcard.isChecked = true
            colorcard.isSelected = selectedTextColor == colorValue
            if (selectedTextColor == colorValue) {
                currentColor.visible()
            } else {
                currentColor.gone()
            }
        }

    }


    override fun getItemCount(): Int = colorsList.size

    class MyViewHolder(val colorCardBinding: ColorCardBinding) : RecyclerView.ViewHolder(colorCardBinding.root)
}



