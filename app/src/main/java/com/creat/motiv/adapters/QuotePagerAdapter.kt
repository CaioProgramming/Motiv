package com.creat.motiv.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.creat.motiv.R
import com.creat.motiv.databinding.QuoteRecyclerBinding
import com.creat.motiv.model.UserDB
import com.creat.motiv.presenter.ProfilePresenter

class QuotePagerAdapter(private val uid:String,val activity: Activity): PagerAdapter() {


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
        val userdb = UserDB()
        if(position == 0){
            userdb.finduserquotes(uid,quoteRecyclerBinding)
        }else{
            userdb.findfavorites(uid,quoteRecyclerBinding)
        }
        container.addView(quoteRecyclerBinding.root)
        return quoteRecyclerBinding.root
    }


}