package com.ilustris.motiv.manager.features.icon

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilustris.motiv.base.adapters.RecyclerIconAdapter
import com.ilustris.motiv.base.beans.Icon
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.utils.NEW_PIC
import com.ilustris.motiv.manager.databinding.FragmentIconsBinding
import com.ilustris.motiv.manager.features.icon.dialog.AddIconsDialog
import com.ilustris.motiv.manager.features.icon.viewmodel.IconsViewModel
import com.ilustris.motiv.manager.features.icon.viewmodel.IconsViewState
import com.ilustris.ui.extensions.showSnackBar
import com.silent.ilustriscore.core.model.ViewModelBaseState

class IconsFragment : Fragment() {

    var fragmentIconsBinding: FragmentIconsBinding? = null
    private val iconsViewModel by lazy { IconsViewModel(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentIconsBinding = FragmentIconsBinding.inflate(inflater)
        return fragmentIconsBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        iconsViewModel.getAllData()
    }

    private fun observeViewModel() {
        iconsViewModel.viewModelState.observe(viewLifecycleOwner) {
            when (it) {
                is ViewModelBaseState.DataListRetrievedState -> {
                    val iconList = it.dataList as ArrayList<Icon>
                    iconList.add(0, Icon.newPic)
                    fragmentIconsBinding?.managerRecycler?.adapter =
                        RecyclerIconAdapter(iconList) { icon ->
                            if (icon.id == NEW_PIC) {
                                AddIconsDialog.buildNewIconsDialog(childFragmentManager) { icons ->
                                    iconsViewModel.uploadIcons(icons)
                                }
                            } else {
                                BottomSheetAlert(
                                    requireContext(),
                                    "Tem certeza?",
                                    "Gostaria de remover esse ícone?",
                                    {
                                        iconsViewModel.deleteData(icon.id)
                                    }).buildDialog()
                            }
                        }
                }
                is ViewModelBaseState.ErrorState -> view?.showSnackBar(
                    it.dataException.code.message,
                    backColor = Color.RED
                )
                ViewModelBaseState.DataDeletedState -> {
                    iconsViewModel.getAllData()
                }
                else -> {}
            }
        }
        iconsViewModel.iconsViewState.observe(viewLifecycleOwner) {
            when (it) {
                IconsViewState.IconsUploaded -> iconsViewModel.getAllData()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentIconsBinding = null
    }

}