package com.creat.motiv.presenter

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.creat.motiv.Model.QuotesDB
import com.creat.motiv.R
import com.creat.motiv.Utils.NewQuotepopup
import com.creat.motiv.contract.ViewContract

/**
 * @Author Kotlin MVP Plugin
 * @Date 2019/10/15
 * @Description input description
 **/
class HomePresenter(val activity: Activity) : ViewContract, SearchView.OnQueryTextListener {


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


    override fun initview(v: View) {
        composesrecycler = v.findViewById(R.id.composesrecycler)
        refreshLayout = v.findViewById(R.id.refresh)
        addquote = v.findViewById(R.id.addquote)
        // search = activity.findViewById(R.id.search)
        composesrecycler?.postDelayed({
            carregar()
        }, 100)
        search?.setOnQueryTextListener(this)
        addquote?.setOnClickListener {
            val newQuotepopup = NewQuotepopup(activity)
            newQuotepopup.showup()
        }
        refreshLayout?.setOnRefreshListener {
            if (refreshLayout!!.isRefreshing) {
                carregar()
            }
        }

    }

    var search: SearchView? = null
    private var addquote: TextView? = null
    private var composesrecycler: RecyclerView? = null
    private var refreshLayout: SwipeRefreshLayout? = null

    override fun carregar() {
        val quotesDB = QuotesDB(activity)
        quotesDB.refreshlayout = refreshLayout
        quotesDB.recyclerView = composesrecycler
        quotesDB.carregar()
    }

    private fun pesquisar(pesquisa: String) {
        val quotesDB = QuotesDB(activity)
        quotesDB.recyclerView = composesrecycler
        quotesDB.pesquisar(pesquisa)
    }

}
