package com.creat.motiv.view.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.quote.view.binder.QuotesListBinder
import com.creat.motiv.utilities.HOME_TUTORIAL
import com.creat.motiv.utilities.MotivPreferences
import com.creat.motiv.tutorial.HomeTutorialDialog
import com.ilustris.motiv.base.utils.RC_SIGN_IN
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilustris.motiv.base.dialog.DefaultAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    var quotesListBinder: QuotesListBinder? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.fragment_home, container, false).rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context is AppCompatActivity) {
            val activity: AppCompatActivity = context as AppCompatActivity
            activity.supportActionBar?.show()
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
        val motivPreferences = MotivPreferences(requireContext())
        if (!motivPreferences.checkTutorial(HOME_TUTORIAL)) {
            HomeTutorialDialog(requireActivity()).buildDialog()
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