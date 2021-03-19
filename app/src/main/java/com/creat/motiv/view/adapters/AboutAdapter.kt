package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.QuoteHeaderViewBinding
import com.creat.motiv.developer.view.DeveloperBinder
import com.ilustris.animations.fadeIn

class AboutAdapter(val context: Context) : RecyclerView.Adapter<AboutAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val quoteHeaderViewBinding: QuoteHeaderViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quote_header_view, parent, false)
        return MyViewHolder(quoteHeaderViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.quoteHeaderBinding.title.text = if (position == 0) "Desenvolvedores" else "Referências"
        if (position == 0) {
            holder.quoteHeaderBinding.title.text = "Desenvolvedores"
            DeveloperBinder(holder.quoteHeaderBinding).initView()
        } else {
            holder.quoteHeaderBinding.recyclerView.run {
                adapter = RecyclerReferencesAdapter(context)
                fadeIn()

            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }


    class MyViewHolder(val quoteHeaderBinding: QuoteHeaderViewBinding) : RecyclerView.ViewHolder(quoteHeaderBinding.root)


}