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
import com.creat.motiv.profile.cover.CoverPickerDialog
import com.creat.motiv.profile.icon.view.IconPickerDialog
import com.google.firebase.auth.FirebaseAuth
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.beans.Style.Companion.adminStyle
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.motiv.base.utils.loadImage
import com.ilustris.motiv.manager.features.ManagerActivity
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.showSnackBar

class SettingsFragment : Fragment() {

    private val args: SettingsFragmentArgs by navArgs()
    private val settingsViewModel = SettingsViewModel()
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
        settingsViewModel.settingsViewState.observe(this, {
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
        })
        settingsViewModel.viewModelState.observe(this, {
            when (it) {
                is ViewModelBaseState.DataUpdateState -> {
                    settingsBinding?.setupUser(it.data as User)
                }
                is ViewModelBaseState.ErrorState -> {
                    view?.showSnackBar(it.dataException.code.message, backColor = Color.RED)
                }
            }
        })
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
            adminText.typeface = FontUtils.getTypeFace(requireContext(), adminStyle.font)
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
            FirebaseAuth.getInstance().signOut()
            requireActivity().finishAffinity()
        }
        userID.text = user.uid
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsBinding = null
    }

}