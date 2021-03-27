package com.creat.motiv.view.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.profile.view.ProfileFragment
import com.creat.motiv.quote.EditQuoteActivity
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
import com.ilustris.motiv.base.utils.getToolbar
import com.ilustris.motiv.base.utils.setMotivTitle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


const val FAVORITES_FRAG_TAG = "FAVORITES_FRAGMENT"

class FavoritesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false).rootView
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
        if (context is AppCompatActivity) {
            val activity: AppCompatActivity = context as AppCompatActivity
            activity.run {
                supportActionBar?.show()
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                getToolbar()?.let {
                    it.setNavigationOnClickListener {
                        supportFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.slide_out_left)
                                .remove(this@FavoritesFragment)
                                .replace(R.id.nav_host_fragment, ProfileFragment())
                                .addToBackStack(null).commit()
                    }
                    setMotivTitle("Favoritos")
                }
            }
        }

        FragmentHomeBinding.bind(view).run {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                QuotesListBinder(quotesView).run {
                    getFavorites(it.uid)
                }
            }
        }
    }


}