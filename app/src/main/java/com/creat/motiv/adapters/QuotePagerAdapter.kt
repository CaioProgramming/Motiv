package com.creat.motiv.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.creat.motiv.R
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.model.Beans.QuoteHead
import com.creat.motiv.model.Beans.Quotes
import com.creat.motiv.model.UserDB
import com.creat.motiv.presenter.ProfilePresenter
import com.firebase.ui.auth.data.model.User

class QuotePagerAdapter(private val quoteshead: ArrayList<QuoteHead>,private val activity: Activity): PagerAdapter() {


    override fun getCount(): Int {
        return 2//To change body of created functions use File | Settings | File Templates.
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeViewInLayout(`object` as RelativeLayout)
    }



    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val quoteRecyclerBinding:QuoteRecyclerBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.quote_recycler,null,false)

        if (quoteshead.size > 0) {
            val recyclerAdapter = RecyclerAdapter(quoteshead[position].quoteslist,activity)
            quoteRecyclerBinding.quotesrecyclerview.adapter = recyclerAdapter
            quoteRecyclerBinding.quotesrecyclerview.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,true)
        }

        return quoteRecyclerBinding.root
    }
}