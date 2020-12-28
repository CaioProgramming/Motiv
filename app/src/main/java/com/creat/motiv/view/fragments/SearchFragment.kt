package com.creat.motiv.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentSearchBinding
import com.creat.motiv.utilities.fadeIn
import com.creat.motiv.utilities.fadeOut
import com.creat.motiv.utilities.slideInBottom
import com.creat.motiv.utilities.snackmessage
import com.creat.motiv.view.adapters.SearchResultAdapter


class SearchFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {


    private lateinit var searchBinding: FragmentSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        searchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, null, false)
        return searchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context is AppCompatActivity) {
            val activity: AppCompatActivity = context as AppCompatActivity
            activity.supportActionBar?.hide()
        }
        searchBinding.searchview.run {
            setOnQueryTextListener(this@SearchFragment)
            requestFocus()
            setOnCloseListener {
                searchBinding.searchResultRecyclerview.fadeOut()
                false
            }
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query == null) {
            snackmessage(context = requireContext(), message = "Não da para pesquisar o vazio :(")
            return false
        }
        searchBinding.searchResultRecyclerview.apply {
            searchBinding.searchIllustration.fadeOut()
            adapter = SearchResultAdapter(requireContext(), query) {
                snackmessage(requireContext(), "Nenhum resultado encontrado")
                searchBinding.searchIllustration.fadeIn()
            }
            slideInBottom()
        }
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        searchBinding.searchResultRecyclerview.fadeOut()
        if (searchBinding.searchIllustration.visibility == View.GONE) {
            searchBinding.searchIllustration.fadeIn()
        }
        return false
    }
}