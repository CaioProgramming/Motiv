package com.creat.motiv.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.creat.motiv.R
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.view.binders.QuotesListBinder

class QuotePagerAdapter(val context: Context) : PagerAdapter() {


    override fun getCount(): Int {
        return 2//To change body of created functions use File | Settings | File Templates.
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeViewInLayout(`object` as CardView)
    }



    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val quoteRecyclerBinding: QuoteRecyclerBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.quote_recycler, null, false)

        QuotesListBinder(context, quoteRecyclerBinding, position == 1)
        container.addView(quoteRecyclerBinding.root)
        return quoteRecyclerBinding.root
    }


}