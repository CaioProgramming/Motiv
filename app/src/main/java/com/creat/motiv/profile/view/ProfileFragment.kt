package com.creat.motiv.profile.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    var fragmentbind: FragmentProfileBinding? = null
    lateinit var profileBinder: ProfileBinder
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentbind = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.fragment_profile, null, false)
        return fragmentbind?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.hideBackButton()
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
