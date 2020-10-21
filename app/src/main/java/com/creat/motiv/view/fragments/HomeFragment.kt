package com.creat.motiv.view.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentHomeBinding
import com.creat.motiv.presenter.HomePresenter
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.Pref
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    var presenter: HomePresenter? = null
    var homeBinding: FragmentHomeBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        tutorial()
        return homeBinding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeBinding.run {
            presenter = context?.let { this?.let { it1 -> HomePresenter(it1, it) } }
        }
    }


    private fun tutorial() {
        var novo = Objects.requireNonNull<Bundle>(Objects.requireNonNull<FragmentActivity>(activity).intent.extras).getBoolean("novo")

        if (novo) {
            val preferences = Pref(Objects.requireNonNull<Context>(context))
            if (!preferences.hometutorialstate()) {
                preferences.setHomeTutorial(true)
                val alert = Alert(activity!!)
                alert.message(activity!!.getDrawable(R.drawable.ic_drawkit_notebook_man_monochrome), getString(R.string.home_intro))

            }


        }

    }
}