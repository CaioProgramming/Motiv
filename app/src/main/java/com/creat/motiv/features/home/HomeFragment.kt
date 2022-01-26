package com.creat.motiv.features.home


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.features.home.adapter.QuoteAction
import com.creat.motiv.features.home.adapter.QuoteRecyclerAdapter
import com.creat.motiv.features.share.QuoteShareDialog
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
import com.ilustris.motiv.base.utils.AdvertiseHelper
import com.ilustris.motiv.base.utils.PagerStackTransformer
import com.ilustris.motiv.base.utils.RC_SIGN_IN
import com.ilustris.motiv.base.utils.activity
import com.ilustris.motiv.base.utils.hideBackButton
import com.ilustris.motiv.base.utils.loadImage
import com.ilustris.motiv.base.utils.showSupportActionBar
import com.ilustris.motiv.manager.features.ManagerActivity
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.*


class HomeFragment : Fragment() {
    private var loadingAd = false
    private var homeBinding: FragmentHomeBinding? = null
    private val homeViewModel = HomeViewModel()
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
        }
        homeViewModel.getHomeQuotes()
        homeViewModel.fetchUser()
    }

    private fun observeViewModel() {
        homeViewModel.homeViewState.observe(this, {
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
            }
        })
        homeViewModel.quoteListViewState.observe(this, {
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
        })
        homeViewModel.viewModelState.observe(this, {
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

            }
        })
    }

    private fun navigateToNewQuote(quote: Quote? = null) {
        val bundle = bundleOf("quote" to quote)
        findNavController().navigate(R.id.navigation_new_quote, bundle)
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
        if (dataException.code == ErrorType.AUTH) {
            LoginHelper.signIn(
                requireActivity() as AppCompatActivity, listOf(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build()
                ),
                R.style.Motiv_Theme,
                R.mipmap.ic_launcher
            )
        } else {
            view?.showSnackBar(dataException.code.message, backColor = Color.RED)
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
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (currentItem >= 10 && currentItem % 10 == 0 && state == ViewPager.SCROLL_STATE_IDLE && !loadingAd) {
                        loadingAd = true
                        AdvertiseHelper(requireContext()).loadAd({
                            quoteRecyclerAdapter.loadOnNextPage(
                                QuoteAdapterData(
                                    Quote.advertiseQuote(),
                                        advertise = it
                                    ), currentItem
                                )
                                loadingAd = false
                            }, {
                                loadingAd = false
                                Log.e("Home", "Ad error: Erro ao carregar anúncio")
                            })
                        }
                    }
                })
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

    private fun userLogged(): Boolean {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val providers = listOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder().build()
            )
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.Motiv_Theme)
                    .build(), RC_SIGN_IN
            )
            return false
        } else {
            if (!user.isEmailVerified) {
                DefaultAlert(
                    context = requireContext(),
                    title = "Email não verificado",
                    message = "Seu email não foi verificado, se não for confirmado você não poderá fazer posts, clique em confirmar para reenviar o email.",
                    icon = R.drawable.fui_ic_mail_white_24dp,
                    okClick = { user.sendEmailVerification() }).buildDialog()

            }
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                initializeHome()
            } else {
                if (response != null) {
                    DefaultAlert(requireContext(), "Atenção", "Ocorreu um erro ao realizar o login",
                        okClick = { userLogged() }).buildDialog()

                }
            }
        }
    }
}