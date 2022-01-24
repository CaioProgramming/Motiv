package com.ilustris.motiv.manager.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.ilustris.motiv.base.beans.quote.QuoteAdapterData
import com.ilustris.motiv.base.beans.quote.QuoteListViewState
import com.ilustris.motiv.base.databinding.QuoteRecyclerBinding
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.dialog.listdialog.ListDialog
import com.ilustris.motiv.base.dialog.listdialog.dialogItems
import com.ilustris.motiv.base.utils.PagerStackTransformer
import com.ilustris.motiv.manager.features.home.viewmodel.ManagerViewModel
import com.ilustris.motiv.manager.ui.home.QuoteAction
import com.ilustris.motiv.manager.ui.home.QuoteManagerAdapter
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.DialogStyles
import com.silent.ilustriscore.core.utilities.gone
import com.silent.ilustriscore.core.utilities.showSnackBar

class ManagerHomeFragment : Fragment() {

    private var managerHomeBinding: QuoteRecyclerBinding? = null
    private val managerViewModel = ManagerViewModel()
    var managerAdapter = QuoteManagerAdapter(ArrayList(), ::selectQuote)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        managerHomeBinding = QuoteRecyclerBinding.inflate(inflater)
        return managerHomeBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        managerHomeBinding?.setupView()
        observeViewModel()
        managerViewModel.getQuotes()
    }

    private fun QuoteRecyclerBinding.setupView() {
        quotesrecyclerview.run {
            adapter = managerAdapter
            offscreenPageLimit = 3
            setPageTransformer(PagerStackTransformer(3))
        }
    }

    private fun selectQuote(quoteAdapterData: QuoteAdapterData, quoteAction: QuoteAction) {
        when (quoteAction) {
            QuoteAction.OPTIONS -> managerViewModel.fetchQuoteOptions(quoteAdapterData)
        }
    }

    private fun openOptions(dialogItems: dialogItems) {
        ListDialog(
            requireContext(), dialogItems,
            { index, dialogItem ->
                dialogItem.action.invoke()
            }, DialogStyles.BOTTOM_NO_BORDER
        ).buildDialog()
    }

    private fun observeViewModel() {
        managerViewModel.quoteListViewState.observe(this, {
            when (it) {
                is QuoteListViewState.QuoteDataRetrieve -> {
                    managerHomeBinding?.run {
                        if (loading.isVisible) loading.gone()
                    }
                    managerAdapter.refreshData(it.quotedata)
                }
                is QuoteListViewState.QuoteOptionsRetrieve -> {
                    openOptions(it.dialogItems)
                }
                is QuoteListViewState.RequestDelete -> {
                    BottomSheetAlert(
                        requireContext(),
                        "Remover Post?",
                        "Você está prestes a deletar, seu post", {
                            managerViewModel.deleteData(it.quote.id)
                        }
                    ).buildDialog()
                }
                is QuoteListViewState.RequestReport -> {
                    DefaultAlert(
                        requireContext(),
                        "Denúnciar publicação",
                        "Você deseja denúnciar essa publicação? Vamos analisá-la e tomar as ações necessárias!",
                        okClick = {
                            managerViewModel.report(it.quote)
                            view?.showSnackBar("Denúncia enviada com sucesso!")
                        }
                    ).buildDialog()
                }
            }
        })

        managerViewModel.viewModelState.observe(this, {
            when (it) {
                is ViewModelBaseState.ErrorState -> view?.showSnackBar(it.dataException.code.message)
            }
        })
    }

}