package com.creat.motiv.features.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.creat.motiv.databinding.FragmentSearchBinding
import com.ilustris.motiv.base.utils.hideSupportActionBar
import com.silent.ilustriscore.core.utilities.showSnackBar


const val SEARCH_FRAG_TAG = "SEARCH_FRAGMENT"

class SearchFragment : Fragment(), SearchView.OnQueryTextListener {


    private var searchBinding: FragmentSearchBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return searchBinding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        searchBinding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.hideSupportActionBar()
        searchBinding?.searchview?.run {
            setOnQueryTextListener(this@SearchFragment)
            setOnCloseListener {
                false
            }
            requestFocus()
            setOnCloseListener {
                false
            }
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query == null) {
            view?.let { view?.showSnackBar(message = "Não da para pesquisar o vazio :(") }
            return false
        }

        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        return false
    }
}