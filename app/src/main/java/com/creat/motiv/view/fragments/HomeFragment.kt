package com.creat.motiv.view.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.home.QuoteDataListViewState
import com.creat.motiv.home.QuoteListViewModel
import com.creat.motiv.quote.EditQuoteActivity
import com.creat.motiv.utilities.HOME_TUTORIAL
import com.creat.motiv.utilities.MotivPreferences
import com.creat.motiv.tutorial.HomeTutorialDialog
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilustris.motiv.base.beans.QuoteData
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.utils.*
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.showSnackBar


private var homeBinding: FragmentHomeBinding? = null
private val homeViewModel = QuoteListViewModel()

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding!!.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mainmenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.navigation_add -> {
                startActivity(Intent(context, EditQuoteActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeViewModel() {
        homeViewModel.quoteDataListViewState.observe(this, {
            when (it) {
                is QuoteDataListViewState.QuotesRetrieve -> {
                    setupQuotes(it.quotedataList)
                }
            }
        })
        homeViewModel.viewModelState.observe(this, {
            when (it) {
                is ViewModelBaseState.ErrorState -> {
                    view?.let {
                        it.showSnackBar("Ocorreu um erro inesperado")
                    }
                }
            }
        })
    }

    fun setupQuotes(quotedatas: ArrayList<QuoteData>) {

        showTutorial()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.activity()?.run {
            setMotivTitle(getString(R.string.app_name))
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

    private fun initializeHome() {
        homeViewModel.getQuotes()
    }

    private fun showTutorial() {
        try {
            context?.let {
                val motivPreferences = MotivPreferences(it)
                if (!motivPreferences.checkTutorial(HOME_TUTORIAL)) {
                    HomeTutorialDialog(it).buildDialog()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
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