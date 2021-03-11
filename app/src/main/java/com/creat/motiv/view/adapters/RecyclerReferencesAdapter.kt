package com.creat.motiv.view.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.ReferencesLayoutBinding
import com.creat.motiv.model.beans.Reference
import com.creat.motiv.utilities.WEB_URL
import com.ilustris.animations.fadeIn


class RecyclerReferencesAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerReferencesAdapter.MyViewHolder>() {
    private val references = Reference.references


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val referencesLayoutBinding: ReferencesLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.references_layout, parent, false)
        return MyViewHolder(referencesLayoutBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ref = references[position]
        holder.referencesLayoutBinding.reference = ref
        holder.referencesLayoutBinding.referenceBackground.setBackgroundResource(ref.background)
        holder.referencesLayoutBinding.referenceCard.setOnClickListener {
            val uri = Uri.parse("$WEB_URL${ref.url}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }
        holder.referencesLayoutBinding.referenceCard.fadeIn()
    }


    override fun getItemCount(): Int {
        return references.size
    }


    inner class MyViewHolder(val referencesLayoutBinding: ReferencesLayoutBinding) : RecyclerView.ViewHolder(referencesLayoutBinding.root)
}
