package com.creat.motiv.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.model.beans.Developers
import java.util.*

class RecyclerCreatorsAdapter(private val mData: ArrayList<Developers>,
                              private val mActivity: Activity) : RecyclerView.Adapter<RecyclerCreatorsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(mActivity)
        view = mInflater.inflate(R.layout.references, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val `in` = AnimationUtils.loadAnimation(mActivity, R.anim.pop_in)


    }


    override fun getItemCount(): Int {
        return if (mData.size == 0) {
            0

        } else {
            mData.size
        }
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nome: TextView


        init {
            nome = view.findViewById(R.id.name)


        }
    }
}
