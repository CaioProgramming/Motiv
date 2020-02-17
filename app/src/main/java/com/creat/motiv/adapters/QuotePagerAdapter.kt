package com.creat.motiv.adapters

import android.content.Context
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
import com.firebase.ui.auth.data.model.User

class QuotePagerAdapter(private val profilePresenter: ProfilePresenter): PagerAdapter() {


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
        val userDB = UserDB(profilePresenter)
        if (position == 0){
            userDB.finduserquotes(profilePresenter.user.uid!!,quoteRecyclerBinding.quotesrecyclerview)
        }else{
            userDB.findfavorites(profilePresenter.user.uid!!,quoteRecyclerBinding.quotesrecyclerview)
        }


        return quoteRecyclerBinding.root
    }
}