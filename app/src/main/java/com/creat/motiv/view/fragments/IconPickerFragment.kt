package com.creat.motiv.view.fragments

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.creat.motiv.presenter.UserPresenter

class IconPickerFragment : DialogFragment() {


    lateinit var userPresenter: UserPresenter
    var adminUser: Boolean = false

    companion object {
        const val PICKER_TAG = "ICON_PICKER_FRAGMENT"

        fun build(fragmentManager: FragmentManager, presenter: UserPresenter, admin: Boolean) {
            return IconPickerFragment().apply {
                adminUser = admin
                userPresenter = presenter
            }.show(fragmentManager, PICKER_TAG)
        }
    }

}