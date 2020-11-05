package com.creat.motiv.view.binders

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View.GONE
import androidx.core.widget.addTextChangedListener
import com.creat.motiv.R
import com.creat.motiv.databinding.ChangeNameViewBinding
import com.creat.motiv.model.DTOMessage
import com.creat.motiv.model.beans.User
import com.creat.motiv.presenter.UserPresenter
import com.creat.motiv.utilities.OperationType
import com.creat.motiv.utilities.fadeIn
import com.creat.motiv.utilities.fadeOut
import com.creat.motiv.view.BaseView

class ChangeNameBinder(override val context: Context, override val viewBind: ChangeNameViewBinding) : BaseView<User>() {


    init {
        initView()
    }

    override fun presenter() = UserPresenter(this)

    override fun onLoading() {
        viewBind.loading.fadeIn()
    }

    override fun onLoadFinish() {
        viewBind.loading.fadeOut()
    }

    override fun getCallBack(dtoMessage: DTOMessage) {
        super.getCallBack(dtoMessage)
        if (dtoMessage.operationType == OperationType.DATA_UPDATED) {
            viewBind.changename.fadeOut()
            viewBind.changenMainView.setBackgroundColor(context.resources.getColor(R.color.material_blue500))
            viewBind.changeStatus.fadeOut()
            viewBind.saveStatus.fadeIn()
            Handler(Looper.myLooper()!!).postDelayed({
                viewBind.changenMainView.setBackgroundColor(context.resources.getColor(R.color.oblack))
                viewBind.changename.fadeIn()
                viewBind.changeStatus.fadeIn()
            }, 5000)
        }
    }


    override fun initView() {
        viewBind.run {
            changename.hint = presenter().currentUser!!.displayName
            saveName.setOnClickListener {
                presenter().changeUserName(viewBind.changename.text.toString())
            }
            changename.addTextChangedListener {
                if (changename.text.isNotBlank()) {
                    if (changeStatus.visibility == GONE) {
                        changeStatus.fadeIn()
                    }
                } else changeStatus.fadeOut()
            }
        }
    }


}