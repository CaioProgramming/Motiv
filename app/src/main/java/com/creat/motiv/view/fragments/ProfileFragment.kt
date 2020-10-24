package com.creat.motiv.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentProfileBinding
import com.creat.motiv.view.binders.ProfileBinder


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    var fragmentbind: FragmentProfileBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentbind = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.fragment_profile, null, false)
        return fragmentbind?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProfileBinder(requireContext(), fragmentbind!!, null)
    }


}
