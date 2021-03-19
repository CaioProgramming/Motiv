package com.creat.motiv.profile.view


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentProfileBinding
import com.ilustris.motiv.base.beans.Pics
import com.creat.motiv.utilities.SELECTED_ICON
import com.creat.motiv.utilities.SELECT_ICON
import com.ilustris.motiv.base.utils.hideBackButton
import com.ilustris.motiv.base.utils.showSupportActionBar
import com.creat.motiv.profile.view.binders.ProfileBinder


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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_ICON && resultCode == RESULT_OK && data != null) {
            profileBinder.presenter.changeProfilePic(data.getSerializableExtra(SELECTED_ICON) as Pics)
        }
    }

}
