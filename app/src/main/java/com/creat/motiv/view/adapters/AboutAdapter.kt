package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.QuoteHeaderViewBinding
import com.creat.motiv.developer.view.RecyclerDeveloperAdapter
import com.creat.motiv.features.about.data.AboutData
import com.ilustris.animations.fadeIn

class AboutAdapter(val aboutDataList: ArrayList<AboutData>) :
    RecyclerView.Adapter<AboutAdapter.AboutViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder {
        return AboutViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.quote_header_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = aboutDataList.size


    inner class AboutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
            QuoteHeaderViewBinding.bind(itemView).run {
                val aboutData = aboutDataList[bindingAdapterPosition]

                title.text = aboutData.header
                recyclerView.run {
                    aboutData.developers?.let {
                        layoutManager =
                            GridLayoutManager(itemView.context, 1, RecyclerView.HORIZONTAL, false)
                        adapter = RecyclerDeveloperAdapter(it)
                    }
                    aboutData.references?.let { adapter = RecyclerReferencesAdapter(it) }
                }
            }
        }
    }


}