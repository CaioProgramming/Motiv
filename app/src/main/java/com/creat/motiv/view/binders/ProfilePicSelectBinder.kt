package com.creat.motiv.view.binders

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.creat.motiv.databinding.ProfilepicselectBinding
import com.creat.motiv.model.beans.Pics
import com.creat.motiv.presenter.PicsPresenter
import com.creat.motiv.utilities.*
import com.creat.motiv.view.BaseView
import com.creat.motiv.view.adapters.RecyclerPicAdapter
import com.opensooq.supernova.gligar.GligarPicker

class ProfilePicSelectBinder(val fragment: Fragment,
                             val admin: Boolean,
                             override val viewBind: ProfilepicselectBinding, val picSelected: (Pics) -> Unit) : BaseView<Pics>() {

    init {
        initView()
    }

    override fun presenter() = PicsPresenter(this)


    override fun initView() {
        presenter().loadData()
    }

    override fun onLoading() {
        super.onLoading()
        viewBind.loading.fadeIn()
    }

    fun uploadIcon(icon: String) {
        presenter().saveData(Pics(icon))
    }


    override fun onLoadFinish() {
        super.onLoadFinish()
        viewBind.loading.fadeOut()
        viewBind.mainView.fadeIn()
    }

    override fun showListData(list: List<Pics>) {
        super.showListData(list)
        if (admin) viewBind.addIcons.visible() else viewBind.addIcons.gone()
        viewBind.picsrecycler.layoutManager = GridLayoutManager(context, 2, VERTICAL, false)
        viewBind.picsrecycler.adapter = RecyclerPicAdapter(context = context, onSelectPick = picSelected, isAdmin = admin, pictureList = ArrayList(list))
        viewBind.addIcons.setOnClickListener {
            GligarPicker()
                    .requestCode(UPLOAD_ICON)
                    .withFragment(fragment)
                    .disableCamera(true)
                    .limit(1)
                    .show()

        }


    }

    override val context: Context
        get() = fragment.requireContext()
}