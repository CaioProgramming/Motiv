package com.creat.motiv.presenter

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.Model.QuotesDB
import com.creat.motiv.R
import com.creat.motiv.contract.ViewContract

class FavoritesPresenter(val activity: Activity) : ViewContract {
    override fun carregar() {
        val quotesDB = QuotesDB(activity)
        quotesDB.usercount = favcount
        quotesDB.recyclerView = recyclerview
        quotesDB.CarregarLikes()

    }

    private var favcount: TextView? = null
    private var recyclerview: RecyclerView? = null

    override fun initview(v: View) {

        favcount = v.findViewById(R.id.favtext)
        recyclerview = v.findViewById(R.id.composesrecycler)
        favcount!!.postDelayed(Runnable {
            carregar()
        }, 100)

    }

}
