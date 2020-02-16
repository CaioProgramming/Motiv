package com.creat.motiv.presenter

import android.app.Activity
import androidx.appcompat.widget.SearchView
import com.creat.motiv.model.QuotesDB
import com.creat.motiv.utils.NewQuotepopup
import com.creat.motiv.view.fragments.HomeFragment
import com.creat.motiv.contract.ViewContract
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @Author Kotlin MVP Plugin
 * @Date 2019/10/15
 * @Description input description
 **/
class HomePresenter(val activity: Activity,val homeFragment: HomeFragment) : ViewContract, SearchView.OnQueryTextListener {

    val quotesDB = QuotesDB(activity)

    override fun onQueryTextSubmit(query: String): Boolean {
        if (query.isEmpty()) {
            carregar()
            return false
        }
        pesquisar(query)
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (newText.isEmpty()) {
            carregar()
            return false
        }
        pesquisar(newText)
        return false //To change body of created functions use File | Settings | File Templates.
    }


    override fun initview() {

        // search = activity.findViewById(R.id.search)
        search?.setOnQueryTextListener(this)

        homeFragment.refresh?.setOnRefreshListener {
            if (homeFragment.refresh.isRefreshing) {
                carregar()
            }
        }
        homeFragment.composesrecycler?.postDelayed({
            carregar()
        }, 100)

    }

    var search: SearchView? = null


    override fun carregar() {
        quotesDB.refreshlayout = homeFragment.refresh
        quotesDB.recyclerView = homeFragment.composesrecycler
        quotesDB.carregar()
    }

    private fun pesquisar(pesquisa: String) {
        quotesDB.pesquisar(pesquisa)
    }

}
