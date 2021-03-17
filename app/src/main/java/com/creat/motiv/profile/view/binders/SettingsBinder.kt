package com.creat.motiv.profile.view.binders

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.FragmentSettingsBinding
import com.creat.motiv.profile.cover.CoverPickerDialog
import com.creat.motiv.profile.icon.view.IconPickerDialog
import com.creat.motiv.utilities.Alert
import com.google.firebase.auth.FirebaseAuth
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.motiv.base.utils.DialogStyles
import com.ilustris.motiv.base.beans.QuoteStyle.Companion.adminStyle
import com.ilustris.motiv.base.beans.User
import com.ilustris.motiv.base.presenter.UserPresenter
import com.ilustris.motiv.base.utils.FontUtils
import com.ilustris.motiv.base.utils.activity
import com.ilustris.motiv.manager.ManagerActivity
import com.silent.ilustriscore.core.view.BaseView


class SettingsBinder(
        val uid: String,
        override val viewBind: FragmentSettingsBinding) : BaseView<User>() {

    override val presenter: UserPresenter = UserPresenter(this)

    init {
        initView()
    }

    override fun initView() {
        viewBind.settingsContainer.left
        presenter.getUser(uid)
    }

    override fun showData(data: User) {
        super.showData(data)
        viewBind.run {

            provider.text = presenter.user?.email ?: "-"
            userID.text = data.uid
            userNameEditText.run {
                setText(data.name)
                addTextChangedListener {
                    if (this.text.isNotEmpty() && this.text.toString() != data.name) {
                        if (saveButton.visibility == View.GONE) {
                            saveButton.fadeIn()
                        }
                    } else {
                        saveButton.fadeOut()
                    }
                }
                saveButton.setOnClickListener {
                    presenter.changeUserName(this.text.toString())
                }
            }
            userpic.run {
                Glide.with(context).load(data.picurl).into(this)
                setOnClickListener {
                    IconPickerDialog(context) { pic ->
                        presenter.changeProfilePic(pic)
                    }.buildDialog()

                }
            }
            userBackground.run {
                Glide.with(context).asGif().centerCrop().load(data.cover).into(this)
                setOnClickListener {
                    CoverPickerDialog(context) { cover ->
                        data.cover = cover.url
                        presenter.updateData(data)
                    }.buildDialog()

                }
            }

            Glide.with(context).asGif().centerCrop().load(adminStyle.backgroundURL).into(adminBackground)
            if (data.admin) {
                adminText.typeface = FontUtils.getTypeFace(context, adminStyle.font)
                adminView.setOnClickListener {
                    val i = Intent(context, ManagerActivity::class.java).apply {
                        putExtra("User", data)
                    }
                    val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                            android.util.Pair(viewBind.adminText, context.getString(com.ilustris.motiv.base.R.string.quote_transaction)))
                    context.startActivity(i, options.toBundle())
                }
                val matrix = ColorMatrix().apply {
                    reset()
                }
                val filter = ColorMatrixColorFilter(matrix)

                adminBackground.colorFilter = filter
            } else {
                val matrix = ColorMatrix().apply {
                    setSaturation(0f)
                }
                val filter = ColorMatrixColorFilter(matrix)

                adminBackground.colorFilter = filter
            }
            adminView.fadeIn()

            singOutButton.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                (context as AppCompatActivity?)?.finish()
            }
        }
    }

}