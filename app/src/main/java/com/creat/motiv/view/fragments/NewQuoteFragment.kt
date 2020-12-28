package com.creat.motiv.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.NewquotepopupBinding
import com.creat.motiv.utilities.hideBackButton
import com.creat.motiv.utilities.showSupportActionBar
import com.creat.motiv.view.binders.EditQuoteBinder


class NewQuoteFragment: Fragment() {
    private var popupbind: NewquotepopupBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        popupbind = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.newquotepopup, null, false)
        return popupbind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.hideBackButton()
        context?.showSupportActionBar()
        popupbind?.let {
            context?.let { it1 -> EditQuoteBinder(context = it1, viewBind = it, quote = null) }
        }
    }


}