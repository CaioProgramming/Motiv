package com.ilustris.motiv.base.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.databinding.DefaultBottomsheetBinding

class BottomSheetAlert : BottomSheetDialogFragment() {

    var title: String? = null
    var message: String? = null
    var okClick: (() -> Unit)? = null
    var cancelClick: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.default_bottomsheet, container).rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DefaultBottomsheetBinding.bind(view).run {
            title?.let {
                titleTextview.text = it
            }
            message?.let {
                messageTextView.text = it
            }
            okButton.setOnClickListener {
                okClick?.invoke()
                dismiss()
            }
            cancelButton.setOnClickListener {
                cancelClick?.invoke()
                dismiss()
            }
        }

    }

    companion object {
        fun buildAlert(fragmentManager: FragmentManager,
                       title: String? = null,
                       message: String? = null,
                       okClick: (() -> Unit)? = null,
                       cancelClick: (() -> Unit)? = null) {

            BottomSheetAlert().apply {
                this.title = title
                this.message = message
                this.okClick = okClick
                this.cancelClick = cancelClick
            }.show(fragmentManager, "BOTTOMSHEET_ALERT")

        }
    }

}