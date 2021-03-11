package com.creat.motiv.profile.icon

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.ilustris.motiv.base.beans.Pics
import com.creat.motiv.utilities.NEW_PIC
import com.ilustris.animations.popIn
import com.ilustris.animations.slideInRight
import com.ilustris.motiv.base.databinding.PicsLayoutBinding

class RecyclerPicAdapter(private var pictureList: ArrayList<Pics> = ArrayList(),
                         private val context: Context,
                         private val onSelectPick: (Pics) -> Unit, private val isAdmin: Boolean = false) : RecyclerView.Adapter<RecyclerPicAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val picsBind: PicsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pics_layout, parent, false)
        return MyViewHolder(picsBind)
    }


    fun loadPictures(pictures: List<Pics>) {
        Log.i(javaClass.simpleName, "loadPictures: adding ${pictures.size} \n $pictures")
        if (isAdmin) {
            pictureList.add(Pics.addPic())
        }
        pictureList.addAll(pictures)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val picture = pictureList[holder.adapterPosition]
        holder.picsLayoutBinding.run {
            if (picture.id != NEW_PIC) {
                Glide.with(context).load(picture.uri).into(pic)
                card.setOnClickListener {
                    onSelectPick(picture)
                }
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.material_grey200))
            }
            card.popIn()

        }
    }


    override fun getItemCount(): Int = pictureList.size


    inner class MyViewHolder(val picsLayoutBinding: PicsLayoutBinding) : RecyclerView.ViewHolder(picsLayoutBinding.root)
}
