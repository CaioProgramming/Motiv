package com.creat.motiv.presenter

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import androidx.appcompat.widget.SearchView
import com.creat.motiv.model.QuotesDB
import com.creat.motiv.utils.NewQuotepopup
import com.creat.motiv.view.fragments.HomeFragment
import com.creat.motiv.contract.ViewContract
import com.creat.motiv.model.Beans.Version
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

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

    private fun checkversion() {
        val manager = activity.packageManager
        val info = manager.getPackageInfo(activity!!.packageName, PackageManager.GET_ACTIVITIES)
        val versionName = info.versionName
        var version = Version()


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
        search?.setOnQueryTextListener(this)

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
