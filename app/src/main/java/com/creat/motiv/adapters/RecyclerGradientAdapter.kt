package com.creat.motiv.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.GradientCardBinding
import com.creat.motiv.model.Beans.Gradient
import com.mikhaellopez.gradientview.GradientView
import java.util.*

class RecyclerGradientAdapter(private val gradientList: ArrayList<Gradient>, private val activity: Activity, private val background: GradientView) : RecyclerView.Adapter<RecyclerGradientAdapter.GradientViewHolder>() {


    override fun getItemCount(): Int {
        return gradientList.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradientViewHolder {
        return GradientViewHolder(LayoutInflater
                .from(activity)
                .inflate(R.layout.gradient_card, parent, false))
    }

    override fun onBindViewHolder(holder: GradientViewHolder, position: Int) {
        val gradientCardBinding = holder.gradientCardBinding
        gradientCardBinding.gradient = gradientList[position]
        gradientCardBinding.colorcard.setOnClickListener {
            val gradient = gradientList[position]
            background.start = gradient.startcolor
            background.end = gradient.endcolor
        }
        val animation = AnimationUtils.loadAnimation(activity, R.anim.fui_slide_in_right)
        holder.gradientCardBinding.colorcard.startAnimation(animation)
    }

    class GradientViewHolder(val gradientCardBinding: GradientCardBinding) : RecyclerView.ViewHolder(gradientCardBinding.root)
}



