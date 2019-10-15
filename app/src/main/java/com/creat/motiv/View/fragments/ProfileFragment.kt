package com.creat.motiv.View.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.creat.motiv.R
import com.creat.motiv.Utils.Alert
import com.creat.motiv.Utils.Pref
import com.creat.motiv.presenter.ProfilePresenter
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private var preferences: Pref? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        preferences = Pref(Objects.requireNonNull<Context>(context))


        val v = inflater.inflate(R.layout.fragment_profile, container, false)


        Tutorial()

        val profilePresenter = ProfilePresenter(activity!!, this)
        profilePresenter.initview(v)
        return v

    }


    private fun Tutorial() {
        val novo = Objects.requireNonNull<Bundle>(Objects.requireNonNull<FragmentActivity>(activity).intent.extras).getBoolean("novo")

        if (novo) {

            val preferences = Pref(Objects.requireNonNull<Context>(context))
            if (!preferences.profiletutorialstate()) {
                preferences.setProfileTutorial(true)
                val a = Alert(activity!!)
                a.Message(activity!!.getDrawable(R.drawable.ic_mobile_post_monochrome), getString(R.string.profile_intro))

            }


        }
    }










}// Required empty public constructor
