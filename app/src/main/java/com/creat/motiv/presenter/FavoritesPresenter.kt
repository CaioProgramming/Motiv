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
    private var composesrecycler: RecyclerView? = null
    override fun initview(v: View) {
        favcount = v.findViewById(R.id.favtext)
        composesrecycler = v.findViewById(R.id.favoritesrecycler)
        composesrecycler?.postDelayed({
            carregar()
        }, 100)

    }

    override fun carregar() {
        if (composesrecycler != null) {
            val quotesDB = QuotesDB(activity)
            quotesDB.usercount = favcount
            quotesDB.recyclerView = composesrecycler
            quotesDB.Carregar()
        }

    }

}
