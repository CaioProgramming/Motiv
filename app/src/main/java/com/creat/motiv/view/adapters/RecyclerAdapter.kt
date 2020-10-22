package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.QuotescardBinding
import com.creat.motiv.model.Beans.Quote
import com.creat.motiv.presenter.BasePresenter
import com.creat.motiv.view.binders.QuoteCardBinder
import com.mikhaellopez.rxanimation.fadeIn


class RecyclerAdapter(val presenter: BasePresenter<Quote>, var quoteList: List<Quote> = emptyList(),
                      val context: Context) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val quotescardBinding = QuotescardBinding.inflate(LayoutInflater.from(context), null, false)
        return MyViewHolder(quotescardBinding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (quoteList.isNotEmpty()) {
            QuoteCardBinder(quoteList[position], context, holder.quotescardBinding, presenter)
        } else {
            holder.quotescardBinding.card.fadeIn().subscribe()
            val fade = AnimationUtils.loadAnimation(context, R.anim.fade_in_repeat)
            holder.quotescardBinding.card.startAnimation(fade)
        }

    }

    override fun getItemCount(): Int {
        return if (quoteList.size > 0) quoteList.size else 1
    }


    class MyViewHolder(val quotescardBinding: QuotescardBinding) : RecyclerView.ViewHolder(quotescardBinding.root)
}