package com.creat.motiv.View.fragments


import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.creat.motiv.R
import com.creat.motiv.Utils.Alert
import com.creat.motiv.Utils.Pref
import com.creat.motiv.presenter.HomePresenter
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    var presenter: HomePresenter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val v = inflater.inflate(R.layout.fragment_home, container, false)
        tutorial()
        setHasOptionsMenu(true)
        return v


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.homemenu, menu)

        val searchitem: MenuItem = menu.findItem(R.id.search)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchitem.actionView as SearchView
        searchView.setBackgroundColor(Color.TRANSPARENT)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        presenter = HomePresenter(activity!!)
        presenter!!.search = searchView
        presenter?.initview(view!!)



        super.onCreateOptionsMenu(menu, inflater)
    }



    private fun tutorial() {
        var novo = Objects.requireNonNull<Bundle>(Objects.requireNonNull<FragmentActivity>(activity).intent.extras).getBoolean("novo")

        if (novo) {
            val preferences = Pref(Objects.requireNonNull<Context>(context))
            if (!preferences.hometutorialstate()) {
                preferences.setHomeTutorial(true)
                val alert = Alert(activity!!)
                alert.Message(activity!!.getDrawable(R.drawable.ic_drawkit_notebook_man_monochrome), getString(R.string.home_intro))

            }


        }
        novo = false

    }
}