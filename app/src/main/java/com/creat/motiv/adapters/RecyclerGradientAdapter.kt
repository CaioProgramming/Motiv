package com.creat.motiv.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
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
        val gradientCardBinding: GradientCardBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.gradient_card, parent, false)
        return GradientViewHolder(gradientCardBinding)
    }

    override fun onBindViewHolder(holder: GradientViewHolder, position: Int) {
        val gradient = gradientList[position]
        val gradientCardBinding = holder.gradientCardBinding
        gradientCardBinding.gradientview.start = gradient.startcolor
        gradientCardBinding.gradientview.end = gradient.endcolor
        gradientCardBinding.colorcard.setOnClickListener {
            background.start = gradient.startcolor
            background.end = gradient.endcolor
        }
        val animation = AnimationUtils.loadAnimation(activity, R.anim.fui_slide_in_right)
        gradientCardBinding.colorcard.startAnimation(animation)
    }

    class GradientViewHolder(val gradientCardBinding: GradientCardBinding) : RecyclerView.ViewHolder(gradientCardBinding.root)
}



