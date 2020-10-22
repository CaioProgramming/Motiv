package com.creat.motiv.view.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.creat.motiv.R
import com.creat.motiv.databinding.PicsLayoutBinding
import com.creat.motiv.model.Beans.Pics
import com.google.firebase.auth.FirebaseAuth

class RecyclerPicAdapter(private val pictureList: List<Pics>,
                         private val context: Context,
                         private val onSelectPick: (Pics) -> Unit) : RecyclerView.Adapter<RecyclerPicAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val picsBind: PicsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pics_layout, parent, false)
        return MyViewHolder(picsBind.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pic = pictureList[holder.adapterPosition]
        val `in` = AnimationUtils.loadAnimation(context, R.anim.pop_out)
        holder.loading.visibility = View.VISIBLE
        Glide.with(context).load(pic.uri).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                holder.pic.visibility = View.GONE
                holder.message.visibility = View.VISIBLE
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                val `in` = AnimationUtils.loadAnimation(context, R.anim.pop_in)


                if (resource != null) {
                    val out = AnimationUtils.loadAnimation(context, R.anim.pop_out)
                    holder.loading.startAnimation(out)
                    holder.loading.visibility = View.GONE
                    holder.pic.setImageDrawable(resource)
                    holder.pic.startAnimation(`in`)

                    return true
                } else {
                    holder.loading.visibility = View.VISIBLE
                }

                return false
            }
        }).into(holder.pic)
        holder.pic.startAnimation(`in`)
        holder.delete.visibility = View.GONE

        holder.pic.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            onSelectPick(pic)
        }

    }


    override fun getItemCount(): Int {
        return if (pictureList.size == 0) {
            0
        } else {
            pictureList.size
        }
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var card: CardView
        var pic: ImageView
        var delete: ImageButton
        var message: TextView
        var loading: ProgressBar

        init {
            card = itemView.findViewById(R.id.card)
            delete = itemView.findViewById(R.id.delete)
            pic = itemView.findViewById(R.id.pic)
            message = itemView.findViewById(R.id.error)
            loading = itemView.findViewById(R.id.loading)


        }
    }
}
