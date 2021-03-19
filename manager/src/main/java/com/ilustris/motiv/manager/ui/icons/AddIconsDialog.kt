package com.ilustris.motiv.manager.ui.icons

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.ilustris.motiv.base.beans.Pics
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.ActivityAddIconsBinding
import com.silent.ilustriscore.core.utilities.showSnackBar
import gun0912.tedbottompicker.TedBottomPicker


class AddIconsDialog : PermissionListener, BottomSheetDialogFragment() {

    lateinit var onSaveClick: (ArrayList<Pics>) -> Unit
    var previewAdapter: RecyclerGalleryAdapter = RecyclerGalleryAdapter(openPicker = this@AddIconsDialog::openPicker)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_add_icons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ActivityAddIconsBinding.bind(view).run {
            iconsPreviewRecycler.run {
                previewAdapter = RecyclerGalleryAdapter(openPicker = {
                    openPicker()
                })
                adapter = previewAdapter
            }
            if (checkPermissions()) openPicker() else requestPermissions()
            saveIcons.setOnClickListener {
                onSaveClick.invoke(buildPics(previewAdapter.pictures))
            }
        }
    }

    private fun buildPics(uris: ArrayList<String>): ArrayList<Pics> {
        val pics: ArrayList<Pics> = ArrayList()
        uris.forEach {
            pics.add(Pics(uri = it))
        }
        return pics
    }

    private fun requestPermissions() {
        TedPermission.with(context)
                .setPermissionListener(this)
                .setDeniedMessage("Se você não aceitar essa permissão não poderá adicionar os ícones...\n\nPor favor ligue as permissões em [Configurações] > [Permissões]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()
    }

    private fun checkPermissions(): Boolean {
        val read = TedPermission.isGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        val write = TedPermission.isGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return read && write
    }


    private fun updateSelection(images: ArrayList<String>) {
        previewAdapter.updateList(images)
    }

    private fun openPicker() {
        if (previewAdapter.itemCount < 5) {
            val act = context as FragmentActivity
            TedBottomPicker.with(act)
                    .setPeekHeight(1000)
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
        } else {
            view?.let {
                showSnackBar(it.context, "Limite de ícones atingidos", rootView = it)
            }
        }
    }


    override fun onPermissionGranted() {
        openPicker()
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        dismiss()
    }

    companion object {
        fun buildNewIconsDialog(fragmentManager: FragmentManager, onSavePics: (ArrayList<Pics>) -> Unit) {
            AddIconsDialog().apply {
                onSaveClick = onSavePics
            }.show(fragmentManager, "NEW_ICONS_DIALOG")
        }
    }

}