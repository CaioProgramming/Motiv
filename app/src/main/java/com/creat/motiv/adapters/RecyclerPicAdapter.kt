package com.creat.motiv.adapters

import android.app.Activity
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.creat.motiv.R
import com.creat.motiv.model.Beans.Pics
import com.creat.motiv.model.UserDB
import com.creat.motiv.presenter.ProfilePresenter
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.ColorUtils.INFO
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth

class RecyclerPicAdapter(private val mData: List<Pics>,
                         private val mActivity: Activity, private val profileFragment: ProfilePresenter?, private val dialog: BottomSheetDialog) : RecyclerView.Adapter<RecyclerPicAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(mActivity)
        view = mInflater.inflate(R.layout.pics, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pic = mData[holder.adapterPosition]
        val `in` = AnimationUtils.loadAnimation(mActivity, R.anim.pop_out)
        holder.loading.visibility = View.VISIBLE
        Glide.with(mActivity).load(pic.uri).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                holder.pic.visibility = View.GONE
                holder.message.visibility = View.VISIBLE
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                val `in` = AnimationUtils.loadAnimation(mActivity, R.anim.pop_in)


                if (resource != null) {
                    val out = AnimationUtils.loadAnimation(mActivity, R.anim.pop_out)
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
            val a = Alert(mActivity)
            dialog.dismiss()
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                if (user.photoUrl === Uri.parse(mData[position].uri)) {
                    a.snackmessage(INFO, "Seu ícone de perfil já é este!")

                } else {
                    val db = UserDB(profileFragment)
                    db.changeuserpic(pic.uri!!)

                }
            } else {
                a.message(mActivity.getDrawable(R.drawable.ic_broken_link), "Você está desconectado!")
            }
        }

    }


    override fun getItemCount(): Int {
        return if (mData.size == 0) {
            0

        } else {
            mData.size
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
