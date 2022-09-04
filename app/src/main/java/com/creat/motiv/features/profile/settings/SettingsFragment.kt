package com.creat.motiv.features.profile.settings

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentSettingsBinding
import com.creat.motiv.features.profile.alerts.CoverPickerDialog
import com.creat.motiv.features.profile.alerts.IconPickerDialog
import com.firebase.ui.auth.AuthUI
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.beans.Style.Companion.adminStyle
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.base.utils.loadImage
import com.ilustris.motiv.manager.ManagerActivity
import com.ilustris.ui.extensions.showSnackBar
import com.silent.ilustriscore.core.model.ViewModelBaseState

class SettingsFragment : Fragment() {

    private val args: SettingsFragmentArgs by navArgs()
    private val settingsViewModel by lazy { SettingsViewModel(requireActivity().application) }
    var settingsBinding: FragmentSettingsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsBinding = FragmentSettingsBinding.inflate(inflater)
        return settingsBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsBinding?.setupUser(args.user)
        observeViewModel()
    }

    private fun observeViewModel() {
        settingsViewModel.settingsViewState.observe(viewLifecycleOwner) {
            when (it) {
                is SettingsViewState.CoversRetrieved -> {
                    CoverPickerDialog(requireContext(), it.covers) { cover ->
                        settingsViewModel.updateUserCover(it.requiredUser, cover.url)
                    }.buildDialog()
                }
                is SettingsViewState.IconsRetrieved -> {
                    IconPickerDialog(requireContext(), it.icons) { icon ->
                        settingsViewModel.updateUserPic(it.requiredUser, icon.uri)
                    }.buildDialog()
                }
            }
        }
        settingsViewModel.viewModelState.observe(viewLifecycleOwner) {
            when (it) {
                is ViewModelBaseState.DataUpdateState -> {
                    settingsBinding?.setupUser(it.data as User)
                }
                is ViewModelBaseState.ErrorState -> {
                    view?.showSnackBar(it.dataException.code.message, backColor = Color.RED)
                }
                else -> {

                }
            }
        }
    }

    private fun FragmentSettingsBinding.setupUser(user: User) {
        userBackground.loadGif(user.cover)
        userpic.loadImage(user.picurl)
        userNameEditText.run {
            addTextChangedListener {
                saveButton.isEnabled =
                    (userNameEditText.text.isNotEmpty() && this.text.toString() != user.name)
            }
            setText(user.name)
        }
        saveButton.setOnClickListener {
            settingsViewModel.editData(user.apply {
                name = userNameEditText.text.toString()
            })
        }
        userpic.setOnClickListener {
            settingsViewModel.requestIcons(user)
        }
        userBackground.setOnClickListener {
            settingsViewModel.requestCovers(user)
        }
        adminBackground.loadGif(adminStyle.backgroundURL)
        if (user.admin) {
            adminView.setOnClickListener {
                val i = Intent(context, ManagerActivity::class.java).apply {
                    putExtra("User", data)
                }
                requireContext().startActivity(i)
            }
            val matrix = ColorMatrix().apply {
                reset()
            }
            val filter = ColorMatrixColorFilter(matrix)
            userpic.run {
                borderWidth = 5
                borderColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
            }
            adminBackground.colorFilter = filter
            adminView.fadeIn()
        }
        aboutButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_settings_to_aboutFragment)
        }
        singOutButton.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
                requireActivity().finishAffinity()
            }
        }
        deleteAccountButton.setOnClickListener {
            DefaultAlert(
                requireContext(),
                "Tem certeza?",
                "Você está prestes a deletar sua conta, essa ação não pode ser revertida",
                okClick = {
                    AuthUI.getInstance().delete(requireContext()).addOnCompleteListener {
                        requireActivity().finishAffinity()
                    }
                }).buildDialog()
        }
        userID.text = user.uid
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsBinding = null
    }

}