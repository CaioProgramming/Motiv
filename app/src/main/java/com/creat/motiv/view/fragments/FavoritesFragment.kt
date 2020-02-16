package com.creat.motiv.view.fragments


import android.os.Bundle
import android.util.Log
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

        Log.v("INFO", "the view has been created")


        v.post{
            val favoritesPresenter = FavoritesPresenter(this)
            favoritesPresenter.initview()
        }
        return v


    }

}// Required empty public constructor