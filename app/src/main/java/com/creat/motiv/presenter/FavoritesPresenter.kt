package com.creat.motiv.presenter

import android.app.Activity
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.Model.QuotesDB
import com.creat.motiv.R
import com.creat.motiv.View.fragments.FavoritesFragment
import com.creat.motiv.contract.ViewContract
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesPresenter(val favoritesFragment: FavoritesFragment) : ViewContract {


    override fun initview() {

        carregar();

    }
    override fun carregar() {
        val quotesDB = QuotesDB(favoritesFragment.activity!!)
        quotesDB.recyclerView = favoritesFragment.favoritesrecycler
        quotesDB.likecount = favoritesFragment.favtext
        quotesDB.carregarlikes()


    }

}
