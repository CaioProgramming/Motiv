package com.creat.motiv.view.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.quote.EditQuoteActivity
import com.creat.motiv.quote.view.binder.QuotesListBinder
import com.creat.motiv.utilities.HOME_TUTORIAL
import com.creat.motiv.utilities.MotivPreferences
import com.creat.motiv.tutorial.HomeTutorialDialog
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


const val HOME_FRAG_TAG = "HOME_FRAGMENT"
class HomeFragment : Fragment() {
    var quotesListBinder: QuotesListBinder? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false).rootView
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.activity()?.run {
            setMotivTitle(getString(R.string.app_name))
            showSupportActionBar()
            hideBackButton()
        }
        FragmentHomeBinding.bind(view).run {
            quotesListBinder = QuotesListBinder(quotesView).apply {
                addSplashQuote()
            }
        }
        initializeHome()
    }

    private fun userLogged(): Boolean {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val providers = listOf(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build())
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.Motiv_Theme)
                    .build(), RC_SIGN_IN)
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

    fun initializeHome() {
        GlobalScope.launch(Dispatchers.IO) {
            delay(2000)
            GlobalScope.launch(Dispatchers.Main) {
                if (userLogged()) {
                    quotesListBinder?.initView()
                    showTutorial()
                }
            }
        }
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