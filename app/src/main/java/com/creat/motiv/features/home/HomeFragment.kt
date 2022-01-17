package com.creat.motiv.features.home


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.features.home.adapter.PagerStackTransformer
import com.creat.motiv.features.home.adapter.QuoteAction
import com.creat.motiv.features.home.adapter.QuoteRecyclerAdapter
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.animations.popOut
import com.ilustris.motiv.base.beans.Quote
import com.ilustris.motiv.base.beans.QuoteAdapterData
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.utils.*
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.LoginHelper
import com.silent.ilustriscore.core.utilities.delayedFunction
import com.silent.ilustriscore.core.utilities.showSnackBar


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
                findNavController().navigate(R.id.action_navigation_home_to_newQuoteFragment)
            }
        }
        homeViewModel.getHomeQuotes()
        homeViewModel.fetchUser()
    }

    private fun observeViewModel() {
        homeViewModel.homeViewState.observe(this, {
            when (it) {
                is HomeViewState.QuoteDataRetrieve -> {
                    setupQuotes(it.quotedata)
                }

                is HomeViewState.UserRetrieved -> {
                    setupUser(it.user)
                }
                is HomeViewState.UsersRetrieved -> {
                    homeBinding?.quotesView?.quotesrecyclerview?.run {
                        quoteRecyclerAdapter.loadOnNextPage(
                            QuoteAdapterData(
                                Quote.usersQuote(),
                                users = it.users
                            ), currentItem
                        )

                    }
                }
            }
        })
        homeViewModel.viewModelState.observe(this, {
            when (it) {
                is ViewModelBaseState.ErrorState -> {
                    view?.showSnackBar("Ocorreu um erro inesperado")
                    handleError(it.dataException)
                }
                ViewModelBaseState.LoadingState -> {
                    homeBinding?.quotesView?.loading?.fadeIn()
                    togglePager(false)
                }
            }
        })
    }

    private fun setupUser(user: User) {
        homeBinding?.userpic?.run {
            loadImage(user.picurl)
            if (user.admin) borderColor =
                ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
            fadeIn()
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
        }
    }

    private fun selectQuote(quoteAdapterData: QuoteAdapterData, quoteAction: QuoteAction) {
        when (quoteAction) {
            QuoteAction.OPTIONS -> openOptions()
            QuoteAction.LIKE -> likeQuote()
            QuoteAction.USER -> {
                navigateToProfile(quoteAdapterData.user.uid)
            }
        }
    }

    private fun selectUser(user: User) {

    }

    private fun setupQuotes(quotedata: QuoteAdapterData) {
        quoteRecyclerAdapter.refreshData(quotedata)
        homeBinding?.quotesView?.loading?.run {
            if (isVisible) {
                fadeOut()
            }
        }
        homeBinding?.quotesView?.quotesrecyclerview?.run {
            if (quoteRecyclerAdapter.itemCount > 1 && currentItem == 0) {
                delayedFunction(5000) {
                    setCurrentItem(1, true)
                }
            }
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position % 10 == 0 && !loadingAd) {
                        loadingAd = true
                        AdvertiseHelper(requireContext()).loadAd({
                            quoteRecyclerAdapter.loadOnNextPage(
                                QuoteAdapterData(
                                    Quote.advertiseQuote(),
                                    advertise = it
                                ), position
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

    }

    private fun openOptions() {

    }

    private fun likeQuote() {

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