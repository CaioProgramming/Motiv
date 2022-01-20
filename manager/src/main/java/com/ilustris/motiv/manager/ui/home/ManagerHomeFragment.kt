package com.ilustris.motiv.manager.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilustris.motiv.base.beans.quote.QuoteAdapterData
import com.ilustris.motiv.base.beans.quote.QuoteListViewState
import com.ilustris.motiv.base.utils.activity
import com.ilustris.motiv.base.utils.hideSupportActionBar
import com.ilustris.motiv.base.utils.showSupportActionBar
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.FragmentManagerHomeBinding
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.showSnackBar

class ManagerHomeFragment : Fragment() {

    private var managerHomeBinding: FragmentManagerHomeBinding? = null
    private val managerViewModel = ManagerViewModel()
    var managerAdapter = QuoteManagerAdapter(ArrayList(), ::selectQuote)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        managerHomeBinding = FragmentManagerHomeBinding.inflate(inflater)
        return managerHomeBinding?.root
    }

    private fun selectQuote(quoteAdapterData: QuoteAdapterData, quoteAction: QuoteAction) {
        when (quoteAction) {
            QuoteAction.OPTIONS -> managerViewModel.fetchQuoteOptions(quoteAdapterData)
        }
    }

    private fun observeViewModel() {
        managerViewModel.quoteListViewState.observe(this, {
            when (it) {
                is QuoteListViewState.QuoteDataRetrieve -> {
                    managerAdapter.refreshData(it.quotedata)
                }
                is QuoteListViewState.QuoteOptionsRetrieve -> {

                }
                is QuoteListViewState.RequestDelete -> {
                    managerViewModel.deleteData(it.quote.id)
                }
                is QuoteListViewState.RequestReport -> {
                    managerViewModel.report(it.quote)
                }
                is QuoteListViewState.RequestShare -> TODO()
            }
        })

        managerViewModel.viewModelState.observe(this, {
            when (it) {
                is ViewModelBaseState.ErrorState -> view?.showSnackBar(it.dataException.code.message)
            }
        })
    }

}