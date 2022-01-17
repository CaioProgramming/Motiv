package com.creat.motiv.view.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.ReferencesLayoutBinding
import com.creat.motiv.features.about.data.Reference
import com.ilustris.motiv.base.utils.WEB_URL
import com.ilustris.animations.slideInBottom


class RecyclerReferencesAdapter(private val references: List<Reference>) :
    RecyclerView.Adapter<RecyclerReferencesAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.references_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return references.size
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            ReferencesLayoutBinding.bind(itemView).run {
                val context = itemView.context
                val reference = references[bindingAdapterPosition]
                referenceBackground.setBackgroundResource(reference.background)
                referenceCard.setOnClickListener {
                    val uri = Uri.parse("$WEB_URL${reference.url}")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                }
                referenceCard.slideInBottom()
            }
        }
    }
}
