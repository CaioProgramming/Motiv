package com.creat.motiv.View.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.Model.Beans.Likes
import com.creat.motiv.Model.Beans.Quotes
import com.creat.motiv.R
import com.creat.motiv.presenter.FavoritesPresenter
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class FavoritesFragment : Fragment() {


    private var myquotesrecycler: RecyclerView? = null
    private var loading: ProgressBar? = null
    private var favcount: TextView? = null
    private var allquotes: ArrayList<Quotes>? = null
    private var likequotes: ArrayList<Quotes>? = null
    private val likesArrayList = ArrayList<Likes>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val v = inflater.inflate(R.layout.fragment_favorites, container, false)
        myquotesrecycler = v.findViewById(R.id.composesrecycler)
        favcount = v.findViewById(R.id.favtext)
        val favoritesPresenter = FavoritesPresenter(activity!!)
        favoritesPresenter.initview(v)
        return v


    }




}// Required empty public constructor