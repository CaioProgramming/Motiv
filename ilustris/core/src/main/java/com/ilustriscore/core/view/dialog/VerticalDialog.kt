package com.ilustriscore.core.view.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.creat.motiv.utilities.DIALOG_MANAGER
import com.creat.motiv.utilities.blurView
import com.creat.motiv.utilities.unblurView
import com.ilustriscore.R
import com.ilustriscore.databinding.VerticalDialogBinding

class VerticalDialog(val blurryView: Int) : DialogFragment(), DialogInterface.OnShowListener, DialogInterface.OnDismissListener {


    private lateinit var verticalDialogObject: VerticalDialogObject
    private lateinit var okClick: () -> Unit
    private var cancelClick: (() -> Unit)? = null
    private lateinit var verticalDialogBinding: VerticalDialogBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        verticalDialogBinding = DataBindingUtil.inflate(inflater, R.layout.vertical_dialog, container, false)
        verticalDialogBinding.vdObject = verticalDialogObject
        return verticalDialogBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verticalDialogBinding.run {
            cancelButton.setOnClickListener {
                cancelClick?.invoke()
                dismiss()
            }
            okButton.setOnClickListener {
                okClick.invoke()
                dismiss()
            }
        }
    }

    override fun onShow(dialogInterface: DialogInterface) {
        activity?.let {
            blurView(it, blurryView)
        }
    }

    override fun onDismiss(dialogInterface: DialogInterface) {
        activity?.let {
            unblurView(it, blurryView)
        }
    }

    companion object {

        fun build(
                blurryView: Int,
                fragmentManager: FragmentManager,
                message: String,
                cancelMessage: String? = null,
                okMessage: String? = null,
                okClick: () -> Unit,
                cancelClick: (() -> Unit)? = null) {
            return VerticalDialog(blurryView).apply {
                this.verticalDialogObject = VerticalDialogObject(message, cancelMessage, okMessage)
                this.okClick = okClick
                cancelClick?.let {
                    this.cancelClick = cancelClick
                }
            }.show(fragmentManager, DIALOG_MANAGER)
        }

    }


}