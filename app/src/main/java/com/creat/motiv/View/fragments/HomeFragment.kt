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
import com.creat.motiv.presenter.HomePresenter
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val v = inflater.inflate(R.layout.fragment_home, container, false)
        tutorial()
        val presenter = HomePresenter(activity!!)
        presenter.initview(v)
        return v


    }
    private fun tutorial() {
        var novo = Objects.requireNonNull<Bundle>(Objects.requireNonNull<FragmentActivity>(activity).intent.extras).getBoolean("novo")

        if (novo) {
            val preferences = Pref(Objects.requireNonNull<Context>(context))
            if (!preferences.hometutorialstate()) {
                preferences.setHomeTutorial(true)
                val alert = Alert(activity!!)
                alert.Message(activity!!.getDrawable(R.drawable.ic_drawkit_notebook_man_monochrome), getString(R.string.home_intro))

            }


        }
        novo = false

    }
}