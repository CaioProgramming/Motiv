package com.creat.motiv.Fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.creat.motiv.Database.QuotesDB
import com.creat.motiv.R
import com.creat.motiv.Utils.Alert
import com.creat.motiv.Utils.NewQuotepopup
import com.creat.motiv.Utils.Pref
import com.google.firebase.auth.FirebaseAuth
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private var composesrecycler: RecyclerView? = null
    private var novo: Boolean? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    internal var user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val v = inflater.inflate(R.layout.fragment_home, container, false)
        v.findViewById<View>(R.id.home)
        v.findViewById<View>(R.id.appbarlayout)
        refreshLayout = v.findViewById(R.id.refresh)
        val search = v.findViewById<SearchView>(R.id.search)
        search.setOnQueryTextListener(this)
        search.setOnCloseListener {
            Carregar()
            false
        }
        val addquote = v.findViewById<TextView>(R.id.addquote)
        addquote.setOnClickListener {
            val newQuotepopup = NewQuotepopup(activity)
            newQuotepopup.showup()
        }

        novo = Objects.requireNonNull<Bundle>(Objects.requireNonNull<FragmentActivity>(activity).intent.extras).getBoolean("novo")


        composesrecycler = v.findViewById(R.id.composesrecycler)

        tutorial()


        refreshLayout!!.setOnRefreshListener {
            if (refreshLayout!!.isRefreshing) {
                Carregar()
            }
        }


        return v


    }


    override fun onResume() {

        Carregar()
        show()


        super.onResume()
    }


    private fun tutorial() {

        if (novo!!) {
            val preferences = Pref(Objects.requireNonNull<Context>(context))
            if (!preferences.hometutorialstate()) {
                preferences.setHomeTutorial(true)
                val alert = Alert(activity!!)
                alert.Message(activity!!.getDrawable(R.drawable.ic_drawkit_notebook_man_monochrome), getString(R.string.home_intro))

            }


        }
        novo = false

    }


    private fun Carregar() {
        val quotesDB = QuotesDB(activity!!)
        quotesDB.Carregar(composesrecycler!!, refreshLayout!!)
    }

    private fun show() {
        val a = Alert(Objects.requireNonNull<FragmentActivity>(activity))
        a.loading()

    }

    override fun onQueryTextSubmit(query: String): Boolean {
        composesrecycler!!.removeAllViews()
        if (!query.isEmpty()) {
            val quotesDB = QuotesDB(activity!!)
            quotesDB.Pesquisar(query, composesrecycler!!)
        } else {
            Carregar()
        }
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        composesrecycler!!.removeAllViews()
        if (!newText.isEmpty()) {
            val quotesDB = QuotesDB(activity!!)
            quotesDB.Pesquisar(newText, composesrecycler!!)
        } else {
            Carregar()
        }
        return false
    }
}// Required empty public constructor