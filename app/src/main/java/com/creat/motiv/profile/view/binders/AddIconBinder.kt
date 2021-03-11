package com.creat.motiv.profile.view.binders

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.creat.motiv.databinding.ActivityAddIconsBinding
import com.creat.motiv.profile.model.beans.Pics
import com.creat.motiv.profile.presenter.PicsPresenter
import com.creat.motiv.utilities.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.silent.ilustriscore.core.model.DTOMessage
import com.silent.ilustriscore.core.presenter.BasePresenter
import com.silent.ilustriscore.core.utilities.OperationType
import com.silent.ilustriscore.core.view.BaseView
import gun0912.tedbottompicker.TedBottomPicker


class AddIconBinder(override val context: Context, override val viewBind: ActivityAddIconsBinding) : BaseView<Pics>(), PermissionListener {

    var previewAdapter = RecyclerGalleryAdapter(context = context, openPicker = ::openPicker)
    var savedCount = 0
    init {
        initView()
    }


    private fun addIcons(picList: ArrayList<String>) {
        picList.forEach {
            if (it != NEW_PIC) {
                presenter.saveData(Pics(it))
            }
        }
    }

    override fun getCallBack(dtoMessage: DTOMessage) {
        super.getCallBack(dtoMessage)
        if (dtoMessage.operationType == OperationType.DATA_SAVED) {
            previewAdapter.updateSaved(savedCount)
            savedCount++
            if (savedCount == previewAdapter.pictureList.size - 1) {
                snackmessage(context, message = "Ícones adicionados com sucesso!")
            }
        }

    }

    override fun onLoading() {
        super.onLoading()
        viewBind.loading.fadeIn()
        viewBind.saveIcons.isEnabled = false
    }

    override fun onLoadFinish() {
        super.onLoadFinish()
        viewBind.loading.fadeOut()
        viewBind.saveIcons.isEnabled = true
    }

    override fun initView() {

        viewBind.iconsPreviewRecycler.adapter = previewAdapter
        viewBind.saveIcons.setOnClickListener {
            if (previewAdapter.pictureList.isNotEmpty()) {
                addIcons(previewAdapter.pictureList)
            } else {
                snackmessage(context, message = "Você não selecionou nenhuma imagem!")
            }
        }
        if (allpermmitted()) {
            openPicker()
        } else {
            requestPermissions()
        }
    }


    private fun requestPermissions() {
        TedPermission.with(context)
                .setPermissionListener(this)
                .setDeniedMessage("Se você não aceitar essa permissão não poderá adicionar os ícones...\n\nPor favor ligue as permissões em [Configurações] > [Permissões]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()
    }


    private fun allpermmitted(): Boolean {
        val read = TedPermission.isGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        val write = TedPermission.isGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return read && write
    }


    fun updateSelection(images: ArrayList<String>) {
        images.addAll(previewAdapter.pictureList)
        if (previewAdapter.pictureList.size == 5) images.remove(NEW_PIC)
        previewAdapter.updateList(images)
    }

    private fun openPicker() {
        val act = context as FragmentActivity
        TedBottomPicker.with(act)
                .setPeekHeight(1600)
                .showTitle(false)
                .showCameraTile(false)
                .setSelectMaxCount(6)
                .setCompleteButtonText("Ok")
                .setEmptySelectionText("Nenhuma imagem selecionada")
                .showMultiImage {
                    val uris = ArrayList<String>()
                    it.forEach { uri ->
                        uri.path?.let { it1 -> uris.add(it1) }
                    }
                    updateSelection(uris)
                }
    }


    override fun onPermissionGranted() {
        openPicker()
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        val act = context as Activity
        context.finish()
    }

    override val presenter: BasePresenter<Pics> = PicsPresenter(this)
}