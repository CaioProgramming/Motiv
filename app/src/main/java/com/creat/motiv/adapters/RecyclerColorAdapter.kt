package com.creat.motiv.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import java.util.*

class RecyclerColorAdapter(private val ColorsList: ArrayList<Int>, private val activity: Activity, private val background: LinearLayout, private val textView: TextView, private val author: TextView, private val textcolor: TextView, private val backcolor: TextView, private val textbutton: ImageButton, private val backgroundbutton: ImageButton) : RecyclerView.Adapter<RecyclerColorAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(activity)
        view = mInflater.inflate(R.layout.color_card, parent, false)

        return RecyclerColorAdapter.MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        println(ColorsList[position])
        val animation = AnimationUtils.loadAnimation(activity, R.anim.pop_in)

        holder.colorcard.backgroundTintList = ColorStateList.valueOf(ColorsList[position])
        holder.colorcard.startAnimation(animation)
        holder.colorcard.setOnClickListener {
            val options = arrayOf("Texto", "Plano de fundo")
            val color = ColorsList[position]

            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Plano de fundo ou texto")

                    .setItems(options) { dialogInterface, i ->
                        if (i == 0) {
                            textView.setTextColor(color)
                            author.setTextColor(color)
                            textcolor.text = color.toString()
                            textbutton.imageTintList = ColorStateList.valueOf(color)
                            println(textcolor.text)
                        } else {
                            background.visibility = View.INVISIBLE
                            background.setBackgroundColor(color)
                            val cx = background.right
                            val cy = background.top
                            val radius = Math.max(background.width, background.height)
                            val anim = ViewAnimationUtils.createCircularReveal(background, cx, cy,
                                    0f, radius.toFloat())
                            background.visibility = View.VISIBLE
                            anim.start()
                            background.setBackgroundColor(color)
                            backgroundbutton.imageTintList = ColorStateList.valueOf(color)

                            backcolor.text = color.toString()
                            println(backcolor.text)
                        }
                    }
            builder.show()
        }

    }


    override fun getItemCount(): Int {
        return if (ColorsList.size == 0) {
            0
        } else {
            ColorsList.size
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var colorcard: CardView

        init {
            colorcard = itemView.findViewById(R.id.colorcard)
        }
    }
}



