package com.ilustris.motiv.base.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilustris.motiv.base.beans.Icon
import com.ilustris.motiv.base.utils.NEW_PIC
import com.ilustris.animations.popIn
import com.ilustris.animations.repeatFade
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.PicsLayoutBinding

class RecyclerPicAdapter(
    private var pictureList: ArrayList<Icon> = ArrayList(),
    private val onSelectPick: (Icon) -> Unit, private val requestNewPic: (() -> Unit)? = null
) : RecyclerView.Adapter<RecyclerPicAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val context = parent.context
        val picsBind: PicsLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.pics_layout,
            parent,
            false
        )
        return MyViewHolder(picsBind)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val picture = pictureList[holder.adapterPosition]
        holder.bind(picture)
    }


    override fun getItemCount(): Int = pictureList.size


    inner class MyViewHolder(private val picsLayoutBinding: PicsLayoutBinding) : RecyclerView.ViewHolder(picsLayoutBinding.root) {
        fun bind(picture: Icon) {
            val context = picsLayoutBinding.root.context
            picsLayoutBinding.run {
                if (picture.id != NEW_PIC) {
                    Glide.with(context).load(picture.uri).into(pic)
                    card.setOnClickListener {
                        onSelectPick.invoke(picture)
                    }
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.material_grey200
                        )
                    )
                } else {
                    pic.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_baseline_arrow_upward_24
                        )
                    )
                    card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.material_blue300))
                    pic.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.material_white))
                    pic.repeatFade()
                    card.setOnClickListener {
                        requestNewPic?.invoke()
                    }
                }
                card.popIn()
            }
        }
    }
}
