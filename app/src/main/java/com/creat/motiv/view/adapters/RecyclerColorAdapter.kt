package com.creat.motiv.view.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.ColorCardBinding
import java.util.*


enum class SelectedViewType {
    BACKGROUND, TEXT
}

class PickedColor(
        val color: Int,
        val selectedView: SelectedViewType
)

class RecyclerColorAdapter(private val colorsList: ArrayList<Int>,
                           private val context: Context,
                           private val onColorPick: ((PickedColor) -> Unit)) : RecyclerView.Adapter<RecyclerColorAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val colorCardBinding = ColorCardBinding.inflate(LayoutInflater.from(context), null, false)

        return MyViewHolder(colorCardBinding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.colorCardBinding.run {
            color = colorsList[position]
            val animation = AnimationUtils.loadAnimation(context, R.anim.pop_in)
            colorcard.startAnimation(animation)
            colorcard.setOnClickListener {
                val options = arrayOf("Texto", "Plano de fundo")
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Plano de fundo ou texto")
                        .setItems(options) { dialogInterface, i ->
                            if (i == 0) {
                                onColorPick(PickedColor(colorsList[position], SelectedViewType.TEXT))
                            } else {
                                onColorPick(PickedColor(colorsList[position], SelectedViewType.BACKGROUND))
                            }
                        }
                builder.show()
            }

        }

    }


    override fun getItemCount(): Int {
        return if (colorsList.size == 0) {
            0
        } else {
            colorsList.size
        }
    }

    class MyViewHolder(val colorCardBinding: ColorCardBinding) : RecyclerView.ViewHolder(colorCardBinding.root)
}



