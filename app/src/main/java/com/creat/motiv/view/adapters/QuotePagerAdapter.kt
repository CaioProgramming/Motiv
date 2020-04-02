package com.creat.motiv.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.viewpager.widget.PagerAdapter
import com.creat.motiv.R
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.model.UserDB
import com.creat.motiv.presenter.ProfilePresenter

class QuotePagerAdapter(private val uid:String,private val profilePresenter: ProfilePresenter): PagerAdapter() {


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
        val quoteRecyclerBinding:QuoteRecyclerBinding = DataBindingUtil.inflate(LayoutInflater.from(profilePresenter.activity),
                R.layout.quote_recycler,null,false)
        val recyclerAdapter = RecyclerAdapter(null, profilePresenter.activity)
        quoteRecyclerBinding.quotesrecyclerview.adapter = recyclerAdapter
        quoteRecyclerBinding.quotesrecyclerview.layoutManager = LinearLayoutManager(profilePresenter.activity, VERTICAL, false)
        if(position == 0){
            UserDB(recyclerAdapter).finduserquotes(uid, quoteRecyclerBinding.notfound)
        }else{
            UserDB(recyclerAdapter).findfavorites(uid, quoteRecyclerBinding.notfound)
        }
        container.addView(quoteRecyclerBinding.root)
        return quoteRecyclerBinding.root
    }


}