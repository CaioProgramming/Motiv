package com.creat.motiv.presenter

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.Model.QuotesDB
import com.creat.motiv.R
import com.creat.motiv.contract.ViewContract

class FavoritesPresenter(val activity: Activity) : ViewContract {


    private var favcount: TextView? = null
    private var recyclerView: RecyclerView? = null
    override fun initview(v: View) {
        favcount = v.findViewById(R.id.favtext)!!
        recyclerView = v.findViewById(R.id.favoritesrecycler)!!
        if (recyclerView != null) {
            carregar()
        }

    }

    override fun carregar() {
        val quotesDB = QuotesDB(activity)
        quotesDB.recyclerView = this.recyclerView!!
        quotesDB.likecount = this.favcount!!
        quotesDB.carregarlikes()


    }

}
