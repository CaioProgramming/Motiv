package com.creat.motiv.quote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.creat.motiv.R
import com.creat.motiv.databinding.NewquotepopupBinding
import com.ilustris.motiv.base.utils.hideBackButton
import com.ilustris.motiv.base.utils.showSupportActionBar
import com.creat.motiv.quote.view.binder.EditQuoteBinder


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
            EditQuoteBinder(viewBind = it, quote = null)
        }
    }


}