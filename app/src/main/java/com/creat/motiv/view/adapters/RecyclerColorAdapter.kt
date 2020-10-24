package com.creat.motiv.view.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.ColorCardBinding
import com.creat.motiv.utils.SelectedViewType
import com.creat.motiv.utils.toHex
import java.util.*




class RecyclerColorAdapter(private val colorsList: ArrayList<Int>,
                           private val context: Context,
                           private val onColorPick: ((PickedColor) -> Unit)) : RecyclerView.Adapter<RecyclerColorAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val colorCardBinding = ColorCardBinding.inflate(LayoutInflater.from(context), null, false)
        return MyViewHolder(colorCardBinding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.colorCardBinding.run {
            this.colorcard.setCardBackgroundColor(ColorStateList.valueOf(colorsList[position]))
            val animation = AnimationUtils.loadAnimation(context, R.anim.pop_in)
            colorcard.startAnimation(animation)
            colorcard.setOnClickListener {
                val options = arrayOf("Texto", "Plano de fundo")
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Plano de fundo ou texto")
                        .setItems(options) { dialogInterface, i ->
                            val hexColor = toHex(colorsList[position])
                            onColorPick(PickedColor(hexColor, if (i == 0) SelectedViewType.TEXT else SelectedViewType.BACKGROUND))
                        }
                builder.show()
            }
        }

    }


    override fun getItemCount(): Int {
        return colorsList.size
    }

    class MyViewHolder(val colorCardBinding: ColorCardBinding) : RecyclerView.ViewHolder(colorCardBinding.root)
}



