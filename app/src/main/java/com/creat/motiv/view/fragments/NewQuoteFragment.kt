package com.creat.motiv.view.fragments

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.NewquotepopupBinding
import com.creat.motiv.presenter.QuoteFormPresenter
import com.google.firebase.auth.FirebaseAuth


class NewQuoteFragment: Fragment() {
    private var popupbind:NewquotepopupBinding? = null
    var quoteFormPresenter: QuoteFormPresenter? = null
    var user = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        popupbind = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.newquotepopup,null,false)
        setHasOptionsMenu(true)
        quoteFormPresenter = QuoteFormPresenter(activity!!, popupbind!!, user)
        quoteFormPresenter?.showup()
        return popupbind?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.newquotemenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.salvar) {
            quoteFormPresenter?.salvar()
        }
        return super.onOptionsItemSelected(item)
    }




}