package com.creat.motiv.View.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.presenter.FavoritesPresenter


/**
 * A simple [Fragment] subclass.
 */
class FavoritesFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val v = inflater.inflate(R.layout.fragment_favorites, container, false)

        v.postDelayed({
            val presenter = FavoritesPresenter(activity!!)
            presenter.initview(this.view!!)
        }, 300)
        return v


    }


}// Required empty public constructor