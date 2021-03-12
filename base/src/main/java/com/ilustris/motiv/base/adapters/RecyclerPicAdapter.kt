package com.ilustris.motiv.base.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilustris.motiv.base.beans.Pics
import com.creat.motiv.utilities.NEW_PIC
import com.ilustris.animations.popIn
import com.ilustris.animations.repeatBounce
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.PicsLayoutBinding

class RecyclerPicAdapter(private var pictureList: ArrayList<Pics> = ArrayList(),
                         private val onSelectPick: (Pics) -> Unit, private val requestNewPic: (() -> Unit)? = null) : RecyclerView.Adapter<RecyclerPicAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val context = parent.context
        val picsBind: PicsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pics_layout, parent, false)
        return MyViewHolder(picsBind)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val picture = pictureList[holder.adapterPosition]
        holder.bind(picture)
    }


    override fun getItemCount(): Int = pictureList.size


    inner class MyViewHolder(private val picsLayoutBinding: PicsLayoutBinding) : RecyclerView.ViewHolder(picsLayoutBinding.root) {
        fun bind(picture: Pics) {
            val context = picsLayoutBinding.root.context
            picsLayoutBinding.run {
                if (picture.id != NEW_PIC) {
                    Glide.with(context).load(picture.uri).into(pic)
                    card.setOnClickListener {
                        onSelectPick.invoke(picture)
                    }
                    card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.material_grey200))
                } else {
                    Glide.with(context).load(R.drawable.ic_up_arrow).into(pic)
                    pic.repeatBounce()
                    card.setOnClickListener {
                        requestNewPic?.invoke()
                    }
                }
                card.popIn()
            }
        }
    }
}
