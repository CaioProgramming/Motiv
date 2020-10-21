package com.creat.motiv.presenter

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.creat.motiv.contract.ViewContract
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.model.BaseModel
import com.creat.motiv.model.Beans.Quote
import com.creat.motiv.model.Beans.Version
import com.creat.motiv.model.QuoteModel
import com.creat.motiv.view.adapters.RecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class HomePresenter(val homeBinding: FragmentHomeBinding, val context: Context) :
        BasePresenter<Quote>(homeBinding),
        ViewContract, SearchView.OnQueryTextListener {


    private fun checkversion() {
        val manager = context.packageManager
        val info = manager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
        val versionName = info.versionName
        val version = Version()


        val versioncheck = FirebaseDatabase.getInstance().reference.child("version")

        versioncheck.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {
                    if (d != null) {
                        version.version = Objects.requireNonNull<Any>(d.value).toString()
                    }
                }
                if (version.version != versionName) {
                    //homeBinding.banner?.visibility = View.VISIBLE
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    override fun initview() {
        checkversion()
        // search = context.findViewById(R.id.search)
        //search?.let { observeSearchView() }

        homeBinding.refresh.setOnRefreshListener {
            if (homeBinding.refresh.isRefreshing) {
                carregar()
            }
        }
        homeBinding.composesrecycler.postDelayed({
            carregar()
        }, 100)


        homeBinding.homeSearch.setOnSearchClickListener {
            quoteadapter?.quoteList = emptyList()
            quoteadapter?.notifyDataSetChanged()
        }
        homeBinding.homeSearch.setOnCloseListener {
            carregar()
            false
        }
        homeBinding.homeSearch.setOnQueryTextListener(this)
    }


    var quoteadapter: RecyclerAdapter? = null


    private fun setupRecycler(recyclerView: RecyclerView) {
        quoteadapter = RecyclerAdapter(this, emptyList(), context)
        val llm = LinearLayoutManager(context, VERTICAL, false)
        recyclerView.adapter = quoteadapter
        recyclerView.layoutManager = llm
        recyclerView.setHasFixedSize(true)
    }

    override fun carregar() {
        setupRecycler(homeBinding.composesrecycler)
        model().getAllData()
    }

    private fun pesquisar(pesquisa: String) {
        model().query(pesquisa, "quote")
    }

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

    override fun onLoading() {
        TODO("Not yet implemented")
    }

    override fun onLoadFinish() {
        TODO("Not yet implemented")
    }

    override fun onDataRetrieve(data: List<Quote>) {
        homeBinding.composesrecycler.apply {
            adapter = RecyclerAdapter(this@HomePresenter, data.reversed(), context)
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
        }
    }

    override fun onSingleData(data: Quote) {
        TODO("Not yet implemented")
    }

    override fun onError() {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        TODO("Not yet implemented")
    }

    override fun model(): BaseModel<Quote> {
        return QuoteModel(this)
    }


}
