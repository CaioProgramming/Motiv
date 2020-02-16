package com.creat.motiv.presenter

import com.creat.motiv.model.QuotesDB
import com.creat.motiv.view.fragments.FavoritesFragment
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
       // quotesDB.carregarlikes()


    }

}
