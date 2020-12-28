package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.creat.motiv.R
import com.creat.motiv.databinding.QuoteHeaderViewBinding
import com.creat.motiv.utilities.fadeIn
import com.creat.motiv.utilities.fadeOut
import com.creat.motiv.view.binders.DeveloperBinder

class AboutAdapter(val context: Context) : RecyclerView.Adapter<AboutAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val quoteHeaderViewBinding: QuoteHeaderViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.quote_header_view, parent, false)
        return MyViewHolder(quoteHeaderViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.quotescardBinding.title.text = if (position == 0) "Desenvolvedores" else "Referências"
        if (position == 0) {
            holder.quotescardBinding.title.text = "Desenvolvedores"
            DeveloperBinder(context, holder.quotescardBinding.quotesRecycler)
        } else {
            holder.quotescardBinding.quotesRecycler.quotesrecyclerview.run {
                layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
                adapter = RecyclerReferencesAdapter(context)
                holder.quotescardBinding.quotesRecycler.loading.fadeOut()
                fadeIn()

            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }


    class MyViewHolder(val quotescardBinding: QuoteHeaderViewBinding) : RecyclerView.ViewHolder(quotescardBinding.root)


}