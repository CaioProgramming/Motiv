package com.creat.motiv.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.Model.Beans.Likes
import com.creat.motiv.R
import de.hdodenhof.circleimageview.CircleImageView


class LikeAdapter(private val likesList: List<Likes>, private val activity: Activity) : RecyclerView.Adapter<LikeAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(activity)
        view = mInflater.inflate(R.layout.like_card, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val likes = likesList[holder.adapterPosition]
        Log.println(Log.INFO, "loaded like ", likes.username)
        holder.nome.text = likes.username
        Glide.with(activity).load(likes.userpic).error(activity.getDrawable(R.drawable.notfound)).into(holder.pic)
        val `in` = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
        holder.pic.startAnimation(`in`)


    }


    override fun getItemCount(): Int {
        return likesList.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nome: TextView
        var pic: CircleImageView


        init {
            nome = view.findViewById(R.id.username)
            pic = view.findViewById(R.id.userpic)


        }
    }
}

