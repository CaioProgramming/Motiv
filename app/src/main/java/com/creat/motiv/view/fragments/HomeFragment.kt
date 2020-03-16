package com.creat.motiv.view.fragments


import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.Pref
import com.creat.motiv.presenter.HomePresenter
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    var presenter: HomePresenter? = null
    var homeBinding:FragmentHomeBinding ? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {



        homeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)


        tutorial()
        setHasOptionsMenu(true)
        return  homeBinding!!.root


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.homemenu, menu)
        presenter = activity?.let { HomePresenter(it,this) }
        val searchitem: MenuItem = menu.findItem(R.id.search)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchitem.actionView as SearchView
        searchView.setBackgroundResource(R.drawable.searchfield)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = "Pesquise frases e autores"
        presenter!!.search = searchView
        presenter!!.initview()



        super.onCreateOptionsMenu(menu, inflater)
    }



    private fun tutorial() {
        var novo = Objects.requireNonNull<Bundle>(Objects.requireNonNull<FragmentActivity>(activity).intent.extras).getBoolean("novo")

        if (novo) {
            val preferences = Pref(Objects.requireNonNull<Context>(context))
            if (!preferences.hometutorialstate()) {
                preferences.setHomeTutorial(true)
                val alert = Alert(activity!!)
                alert.message(activity!!.getDrawable(R.drawable.ic_drawkit_notebook_man_monochrome), getString(R.string.home_intro))

            }


        }

    }
}