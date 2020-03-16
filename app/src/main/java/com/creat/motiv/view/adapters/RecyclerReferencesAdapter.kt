package com.creat.motiv.view.adapters

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.model.Beans.Artists
import com.creat.motiv.utils.Tools
import java.util.*


class RecyclerReferencesAdapter(private val mActivity: Activity) : RecyclerView.Adapter<RecyclerReferencesAdapter.MyViewHolder>() {
    private val mData: ArrayList<Artists>


    init {
        mData = Tools.references(mActivity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(mActivity)
        view = mInflater.inflate(R.layout.references_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val a = mData[holder.adapterPosition]
        holder.nome.text = a.nome
        holder.nome.setOnClickListener {
            val uri = Uri.parse(a.uri)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            mActivity.startActivity(intent)
        }
        Log.println(Log.INFO, "REFERENCE color", a.color.toString())
        holder.nome.setTextColor(a.color)


    }


    override fun getItemCount(): Int {

        return mData.size
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nome: TextView

        init {
            nome = view.findViewById(R.id.reference)


        }
    }
}
