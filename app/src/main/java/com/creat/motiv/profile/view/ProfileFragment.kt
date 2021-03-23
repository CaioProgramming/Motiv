package com.creat.motiv.profile.view


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentProfileBinding
import com.ilustris.motiv.base.utils.hideBackButton
import com.ilustris.motiv.base.utils.showSupportActionBar
import com.creat.motiv.profile.view.binders.ProfileBinder
import com.creat.motiv.utilities.MotivPreferences
import com.creat.motiv.utilities.NEW_QUOTE_TUTORIAL
import com.creat.motiv.tutorial.HomeTutorialDialog
import com.creat.motiv.tutorial.ProfileTutorialDialog
import com.creat.motiv.utilities.PROFILE_TUTORIAL
import com.creat.motiv.view.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    var fragmentbind: FragmentProfileBinding? = null
    lateinit var profileBinder: ProfileBinder
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
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.showSupportActionBar()
        profileBinder = ProfileBinder(requireContext(), fragmentbind!!, null)
        showTutorial()
    }

    private fun showTutorial() {
        val motivPreferences = MotivPreferences(requireContext())
        if (!motivPreferences.checkTutorial(PROFILE_TUTORIAL)) {
            ProfileTutorialDialog(requireActivity()).buildDialog()
        }
    }


}
