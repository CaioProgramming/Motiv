package com.creat.motiv.view.binders

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.ProfileTopViewBinding
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.Alert
import com.creat.motiv.utilities.DialogStyles
import com.creat.motiv.utilities.gone
import com.creat.motiv.utilities.snackmessage
import com.creat.motiv.view.BaseView


class ProfileTopBinder(val uid: String,
                       val user: User? = null,
                       val isSettings: Boolean = false,
                       override val viewBind: ProfileTopViewBinding, override val context: Context) : BaseView<User>() {


    override fun presenter() = UserPresenter(this)


    override fun initView() {
        onLoading()
        viewBind.followButton.gone()
        viewBind.messageButton.gone()
        if (isSettings) {
            viewBind.followButton.gone()
        }
        if (user != null) {
            showData(user)
        } else {
            val firebaseUser = presenter().currentUser
            presenter().getUser(firebaseUser!!.uid)
        }
    }

    override fun onLoading() {
        super.onLoading()
        viewBind.run {
            profilepic.setImageResource(R.color.material_grey200)
            username.setBackgroundResource(R.color.material_grey200)
            username.setText("")
            username.clearFocus()
            topShimmer.showShimmer(true)
        }
    }

    override fun onLoadFinish() {
        super.onLoadFinish()
        viewBind.run {
            Handler().postDelayed({
                topShimmer.hideShimmer()
            }, 2000)

        }
    }

    override fun showData(data: User) {
        super.showData(data)
        viewBind.run {
            username.setBackgroundResource(R.color.transparent)
            userData = data
            profilepic.setOnClickListener {
                if (user?.uid == presenter().currentUser?.uid || isSettings) {
                    Alert(context as Activity, DialogStyles.BOTTOM_NO_BORDER).picturePicker(presenter(), data.admin)
                }
            }
            Glide.with(context).load(data.picurl).error(ContextCompat.getDrawable(context, R.drawable.ic__41_floppy_disk)).into(viewBind.profilepic)
            if (data.admin) {
                profilepic.borderColor = context.resources.getColor(R.color.material_yellow600)
                profilepic.borderWidth = 10

            } else {
                profilepic.borderColor = Color.TRANSPARENT
                profilepic.borderWidth = 0
            }
            if (data.uid == presenter().currentUser?.uid) {
                username.setOnEditorActionListener { _, actionId, event ->
                    if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                        if (!viewBind.username.text.isNullOrBlank()) {
                            presenter().changeUserName(viewBind.username.text.toString())
                        } else {
                            snackmessage(context, ContextCompat.getColor(context, R.color.material_red500), message = "Desculpa ainda não temos suporte a nomes inexistentes")
                        }
                    }
                    false
                }
            } else {
                viewBind.followButton.text = context.getString(R.string.follow_user)
                viewBind.username.isEnabled = false
            }
        }
    }

    init {
        initView()
    }
}