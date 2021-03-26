package com.creat.motiv.profile.view


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentProfileBinding
import com.ilustris.motiv.base.utils.hideBackButton
import com.ilustris.motiv.base.utils.showSupportActionBar
import com.creat.motiv.profile.view.binders.ProfileBinder
import com.creat.motiv.quote.view.binder.QuotesListBinder
import com.creat.motiv.utilities.MotivPreferences
import com.creat.motiv.utilities.NEW_QUOTE_TUTORIAL
import com.creat.motiv.tutorial.HomeTutorialDialog
import com.creat.motiv.tutorial.ProfileTutorialDialog
import com.creat.motiv.utilities.PROFILE_TUTORIAL
import com.creat.motiv.view.fragments.FAVORITES_FRAG_TAG
import com.creat.motiv.view.fragments.FavoritesFragment
import com.creat.motiv.view.fragments.SettingsFragment
import com.google.firebase.auth.FirebaseAuth
import com.ilustris.motiv.base.utils.activity
import com.ilustris.motiv.base.utils.setMotivTitle
import kotlinx.android.synthetic.main.activity_main.*


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
                context?.let {
                    it.startActivity(Intent(it, SettingsActivity::class.java))
                }
                return false
            }
            R.id.navigation_favorites -> {
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, FavoritesFragment(), FAVORITES_FRAG_TAG)?.commit()
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
        }
    }

    private fun showTutorial() {
        val motivPreferences = MotivPreferences(requireContext())
        if (!motivPreferences.checkTutorial(PROFILE_TUTORIAL)) {
            ProfileTutorialDialog(requireActivity()).buildDialog()
        }
    }


}
