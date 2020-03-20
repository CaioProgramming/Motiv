package com.creat.motiv.presenter

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.creat.motiv.contract.ViewContract
import com.creat.motiv.model.Beans.Version
import com.creat.motiv.model.QuotesDB
import com.creat.motiv.view.adapters.RecyclerAdapter
import com.creat.motiv.view.fragments.HomeFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * @Author Kotlin MVP Plugin
 * @Date 2019/10/15
 * @Description input description
 **/
class HomePresenter(val activity: Activity, val homeFragment: HomeFragment) : ViewContract, android.widget.SearchView.OnQueryTextListener {

    val quotesDB = QuotesDB(activity)
    private var disposable: Disposable? = null

    private fun searchObservable(): Observable<String> {
        val subject = PublishSubject.create<String>()
        search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                s?.let { pesquisar(it) }
                search?.clearFocus()
                return false
            }

            override fun onQueryTextChange(text: String): Boolean {
                if (text.isNotBlank()) {
                    pesquisar(text)
                } else {
                    carregar()
                }
                return false
            }
        })
        return subject

    }


    private fun observeSearchView() {
        disposable = searchObservable()
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter { t -> !t.isEmpty() && t.length >= 3 }
                .map { text -> text.toLowerCase().trim() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { query -> pesquisar(query) }
    }


    private fun checkversion() {
        val manager = activity.packageManager
        val info = manager.getPackageInfo(activity.packageName, PackageManager.GET_ACTIVITIES)
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
                    homeFragment.banner?.visibility = View.VISIBLE
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    override fun initview() {
        checkversion()
        // search = activity.findViewById(R.id.search)
        //search?.let { observeSearchView() }

        homeFragment.refresh?.setOnRefreshListener {
            if (homeFragment.refresh.isRefreshing) {
                carregar()
            }
        }
        homeFragment.composesrecycler?.postDelayed({
            carregar()
        }, 100)

        homeFragment.update?.setOnClickListener {
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.creat.motiv")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            activity.startActivity(intent)
        }
        homeFragment.dismiss?.setOnClickListener {
            homeFragment.banner.visibility = View.GONE
        }
        search?.setOnSearchClickListener {
            quoteadapter?.quotesList!!.clear()
            quoteadapter?.notifyDataSetChanged()
        }

    }

    var search: SearchView? = null
    var quoteadapter: RecyclerAdapter? = null


    private fun setupRecycler(recyclerView: RecyclerView) {
        quoteadapter = RecyclerAdapter(null, activity)
        val llm = LinearLayoutManager(activity, VERTICAL, false)
        recyclerView.adapter = quoteadapter
        recyclerView.layoutManager = llm
        recyclerView.setHasFixedSize(true)
    }

    override fun carregar() {
        setupRecycler(homeFragment.homeBinding!!.composesrecycler)
        quotesDB.refreshlayout = homeFragment.refresh
        quotesDB.recyclerAdapter = quoteadapter
        quotesDB.carregar()
    }

    private fun pesquisar(pesquisa: String) {
        val handler = Handler()
        handler.postDelayed({ quotesDB.pesquisar(pesquisa) }, 3500)
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

}
