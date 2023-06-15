package com.creat.motiv.features.home


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.features.home.adapter.QuoteAction
import com.creat.motiv.features.home.adapter.QuoteRecyclerAdapter
import com.creat.motiv.features.share.QuoteShareDialog
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.beans.quote.Quote
import com.ilustris.motiv.base.beans.quote.QuoteAdapterData
import com.ilustris.motiv.base.beans.quote.QuoteListViewState
import com.ilustris.motiv.base.databinding.QuoteRecyclerBinding
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.dialog.listdialog.ListDialog
import com.ilustris.motiv.base.dialog.listdialog.dialogItems
import com.ilustris.motiv.base.utils.PagerStackTransformer
import com.ilustris.motiv.base.utils.activity
import com.ilustris.motiv.base.utils.hideBackButton
import com.ilustris.motiv.base.utils.loadImage
import com.ilustris.motiv.base.utils.showSupportActionBar
import com.ilustris.motiv.manager.ManagerActivity
import com.ilustris.ui.alert.DialogStyles
import com.ilustris.ui.extensions.gone
import com.ilustris.ui.extensions.showSnackBar
import com.ilustris.ui.extensions.visible
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.model.ViewModelBaseState


class HomeFragment : Fragment() {
    private var loadingAd = false
    private var homeBinding: FragmentHomeBinding? = null
    private val homeViewModel by lazy { HomeViewModel(requireActivity().application) }
    private var quoteRecyclerAdapter = QuoteRecyclerAdapter(ArrayList(), ::selectQuote)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding!!.root
    }

    private fun initializeHome() {
        homeBinding?.run {
            quotesView.quotesrecyclerview.run {
                adapter = quoteRecyclerAdapter
                offscreenPageLimit = 3
                setPageTransformer(PagerStackTransformer(3))
            }
            (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
            writingIcon.setOnClickListener {
                navigateToNewQuote()
            }
            searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    quoteRecyclerAdapter.filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }


            })
        }
        homeViewModel.getHomeQuotes()
        homeViewModel.fetchUser()
    }

    private fun observeViewModel() {
        homeViewModel.homeViewState.observe(viewLifecycleOwner) {
            when (it) {
                is HomeViewState.UserRetrieved -> setupUser(it.user)
                is HomeViewState.UsersRetrieved -> {
                    homeBinding?.quotesView?.quotesrecyclerview?.run {
                        if (childCount > 15) {
                            quoteRecyclerAdapter.loadOnNextPage(
                                QuoteAdapterData(
                                    Quote.usersQuote(),
                                    users = it.users
                                ), currentItem
                            )
                        }
                    }
                }
                HomeViewState.EnableSearch -> {
                    homeBinding?.searchview?.fadeIn()
                }
            }
        }
        homeViewModel.quoteListViewState.observe(viewLifecycleOwner) {
            when (it) {
                is QuoteListViewState.QuoteDataRetrieve -> {
                    homeBinding?.quotesView?.setupQuotes(it.quotedata)
                }
                is QuoteListViewState.QuoteOptionsRetrieve -> {
                    openOptions(it.dialogItems)
                }
                is QuoteListViewState.RequestDelete -> {
                    BottomSheetAlert(
                        requireContext(),
                        "Remover Post?",
                        "Você está prestes a deletar, seu post", {
                            homeViewModel.deleteData(it.quote.id)
                        }
                    ).buildDialog()
                }
                is QuoteListViewState.RequestEdit -> {
                    navigateToNewQuote(it.quote)
                }
                is QuoteListViewState.RequestReport -> {
                    DefaultAlert(
                        requireContext(),
                        "Denúnciar publicação",
                        "Você deseja denúnciar essa publicação? Vamos analisá-la e tomar as ações necessárias!",
                        okClick = {
                            homeViewModel.reportQuote(it.quote)
                            view?.showSnackBar("Denúncia enviada com sucesso!")
                        }
                    ).buildDialog()
                }
                is QuoteListViewState.RequestShare -> {
                    QuoteShareDialog(requireContext(), it.quoteShareData).buildDialog()
                }
            }
        }
        homeViewModel.viewModelState.observe(viewLifecycleOwner) {
            when (it) {
                is ViewModelBaseState.ErrorState -> {
                    handleError(it.dataException)
                }
                ViewModelBaseState.LoadingState -> {
                    togglePager(false)
                }
                ViewModelBaseState.DataDeletedState -> {
                    quoteRecyclerAdapter.clearAdapter()
                    homeViewModel.getHomeQuotes()
                }

                is ViewModelBaseState.DataUpdateState -> {
                    quoteRecyclerAdapter.clearAdapter()
                    homeViewModel.getHomeQuotes()
                }

                else -> {}

            }
        }
    }

    private fun navigateToNewQuote(quote: Quote? = null) {
        lifecycleScope.launchWhenResumed {
            val bundle = bundleOf("quote" to quote)
            findNavController().navigate(R.id.navigation_new_quote, bundle)
        }
    }

    private fun setupUser(user: User) {
        homeBinding?.userpic?.run {
            loadImage(user.picurl)
            if (user.admin) borderColor =
                ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
            fadeIn()
            setOnClickListener {
                navigateToProfile(user.uid)
            }
            if (user.admin) {
                setOnLongClickListener {
                    val i = Intent(context, ManagerActivity::class.java).apply {
                        putExtra("User", data)
                    }
                    requireContext().startActivity(i)
                    false
                }
            }
        }

    }

    private fun togglePager(enable: Boolean) {
        homeBinding?.quotesView?.quotesrecyclerview?.isEnabled = enable

    }

    private fun handleError(dataException: DataException) {
        view?.showSnackBar(dataException.code.message, backColor = Color.RED)

        if (dataException.code == ErrorType.AUTH) {

        } else {
        }
    }

    private fun selectQuote(quoteAdapterData: QuoteAdapterData, quoteAction: QuoteAction) {
        when (quoteAction) {
            QuoteAction.OPTIONS -> homeViewModel.fetchQuoteOptions(quoteAdapterData)
            QuoteAction.LIKE -> likeQuote(quoteAdapterData.quote)
            QuoteAction.USER -> {
                navigateToProfile(quoteAdapterData.user.uid)
            }
        }
    }

    private fun selectUser(user: User) {
        navigateToProfile(user.uid)
    }

    private fun QuoteRecyclerBinding.setupQuotes(quotedata: QuoteAdapterData) {
        quoteRecyclerAdapter.refreshData(quotedata)
        loading.gone()
        quotesrecyclerview.visible()
        quotesrecyclerview.run {
            visible()
        }
    }

    private fun navigateToProfile(uid: String) {
        val bundle = bundleOf("uid" to uid)
        findNavController().navigate(R.id.action_navigation_home_to_navigation_profile, bundle)
    }

    private fun openOptions(dialogItems: dialogItems) {
        ListDialog(
            requireContext(), dialogItems,
            { index, dialogItem ->
                dialogItem.action.invoke()
            }, DialogStyles.BOTTOM_NO_BORDER
        ).buildDialog()
    }

    private fun likeQuote(quote: Quote) {
        homeViewModel.favoriteQuote(quote)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.activity()?.run {
            showSupportActionBar()
            hideBackButton()
        }
        initializeHome()
        observeViewModel()
    }


}