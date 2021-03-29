package com.creat.motiv.profile.view


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentProfileBinding
import com.ilustris.motiv.base.utils.hideBackButton
import com.ilustris.motiv.base.utils.showSupportActionBar
import com.creat.motiv.quote.view.binder.QuotesListBinder
import com.creat.motiv.utilities.MotivPreferences
import com.creat.motiv.tutorial.ProfileTutorialDialog
import com.creat.motiv.utilities.PROFILE_TUTORIAL
import com.creat.motiv.view.fragments.FAVORITES_FRAG_TAG
import com.creat.motiv.view.fragments.FavoritesFragment
import com.google.firebase.auth.FirebaseAuth
import com.ilustris.motiv.base.utils.activity
import com.ilustris.motiv.base.utils.setMotivTitle


const val PROFILE_FRAG_TAG = "PROFILE_FRAGMENT"

class ProfileFragment : Fragment() {
    var fragmentbind: FragmentProfileBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentbind = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.fragment_profile, null, false)
        setHasOptionsMenu(true)
        return fragmentbind?.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profilemenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_settings -> {
                activity?.supportFragmentManager?.run {
                    beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out)
                            .replace(R.id.nav_host_fragment, SettingsFragment(), SETTINGS_FRAG_TAG)
                            .commit()
                }
                return false
            }
            R.id.navigation_favorites -> {
                activity?.supportFragmentManager?.run {
                    beginTransaction()
                            .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.slide_out_left)
                            .replace(R.id.nav_host_fragment, FavoritesFragment(), FAVORITES_FRAG_TAG)
                            .commit()
                }

            }
        }

        return super.onOptionsItemSelected(item)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.activity()?.let {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let { user ->
                it.setMotivTitle(user.displayName ?: "")
                fragmentbind?.let { profileBind ->
                    QuotesListBinder(profileBind.quotesView).getUserQuotes(user.uid)
                }
            }
            showTutorial()
            it.showSupportActionBar()
            it.hideBackButton()
        }
    }

    private fun showTutorial() {
        val motivPreferences = MotivPreferences(requireContext())
        if (!motivPreferences.checkTutorial(PROFILE_TUTORIAL)) {
            ProfileTutorialDialog(requireActivity()).buildDialog()
        }
    }


}
