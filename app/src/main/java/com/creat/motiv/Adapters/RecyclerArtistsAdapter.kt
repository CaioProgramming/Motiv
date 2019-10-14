package com.creat.motiv.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.creat.motiv.Beans.Artists
import com.creat.motiv.R

class RecyclerArtistsAdapter(private val mContext: Context, private val mData: List<Artists>,
                             private val mActivity: Activity) : RecyclerView.Adapter<RecyclerArtistsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(mContext)
        view = mInflater.inflate(R.layout.references_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.nome.text = mData[position].nome
        holder.nome.setOnClickListener {
            val uri = Uri.parse(mData[position].uri)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            mActivity.startActivity(intent)
        }


    }


    override fun getItemCount(): Int {
        return if (mData.size == 0) {
            0

        } else {
            mData.size
        }
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var nome: TextView
        internal var colors: LinearLayout? = null
        internal var layout: CardView


        init {
            nome = view.findViewById(R.id.reference)
            layout = view.findViewById(R.id.layout)


        }
    }
}
