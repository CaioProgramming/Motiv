package com.creat.motiv.features.newquote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creat.motiv.databinding.NewQuoteFragmentBinding


class NewQuoteFragment: Fragment() {
    private var newQuoteFragmentBinding: NewQuoteFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        newQuoteFragmentBinding = NewQuoteFragmentBinding.inflate(inflater, container, false)
        return newQuoteFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newQuoteFragmentBinding?.let {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        newQuoteFragmentBinding = null
    }


}