package com.creat.motiv.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentSearchBinding
import com.creat.motiv.utilities.*
import com.creat.motiv.quote.view.binder.QuotesListBinder
import com.ilustris.motiv.base.utils.hideSupportActionBar
import com.silent.ilustriscore.core.utilities.showSnackBar


const val SEARCH_FRAG_TAG = "SEARCH_FRAGMENT"

class SearchFragment : Fragment(), SearchView.OnQueryTextListener {


    private lateinit var searchBinding: FragmentSearchBinding
    private var quotesListBinder: QuotesListBinder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        searchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, null, false)
        return searchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.hideSupportActionBar()
        quotesListBinder = QuotesListBinder(searchBinding.quotesView)
        quotesListBinder?.addSearchQuote()
        searchBinding.searchview.run {
            setOnQueryTextListener(this@SearchFragment)
            setOnCloseListener {
                quotesListBinder?.initView()
                false
            }
            requestFocus()
            setOnCloseListener {
                quotesListBinder?.addSearchQuote()
                false
            }
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query == null) {
            view?.let { showSnackBar(context = requireContext(), message = "Não da para pesquisar o vazio :(", rootView = it) }
            return false
        }
        quotesListBinder?.searchData(query)
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        return false
    }
}