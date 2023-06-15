package com.ilustris.motiv.manager.features.style

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.motiv.base.beans.NEW_STYLE_ID
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.dialog.listdialog.DialogData
import com.ilustris.motiv.base.dialog.listdialog.ListDialog
import com.ilustris.motiv.manager.R
import com.ilustris.motiv.manager.databinding.StylesRecyclerBinding
import com.ilustris.motiv.manager.features.style.adapter.StylePreviewAdapter
import com.ilustris.motiv.manager.features.style.viewmodel.StylesViewModel
import com.ilustris.ui.alert.DialogStyles
import com.ilustris.ui.extensions.showSnackBar
import com.silent.ilustriscore.core.model.ViewModelBaseState

class StylesFragment : Fragment() {

    private val stylesViewModel by lazy { StylesViewModel(requireActivity().application) }
    var stylesRecyclerBinding: StylesRecyclerBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        stylesRecyclerBinding = StylesRecyclerBinding.inflate(inflater)
        return stylesRecyclerBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        stylesViewModel.getAllData()
    }

    private fun observeViewModel() {
        stylesViewModel.viewModelState.observe(viewLifecycleOwner) {
            when (it) {
                ViewModelBaseState.DataDeletedState -> {
                    stylesViewModel.getAllData()
                }

                is ViewModelBaseState.DataListRetrievedState -> {
                    val styles = it.dataList as ArrayList<Style>
                    styles.add(0, Style.newStyle)
                    stylesRecyclerBinding?.setupRecycler(it.dataList as ArrayList<Style>)
                }

                is ViewModelBaseState.ErrorState -> {
                    view?.showSnackBar(it.dataException.code.message, backColor = Color.RED)
                }

                else -> {}

            }
        }
    }

    private fun StylesRecyclerBinding.setupRecycler(styles: ArrayList<Style>) {
        stylesRecycler.adapter = StylePreviewAdapter(styles, false, null)
        { style, index ->

            if (style.id != NEW_STYLE_ID) {
                ListDialog(requireContext(), listOf(DialogData("Editar") {
                    goToStyle(style)
                }, DialogData("Excluir") {
                    DefaultAlert(
                        requireContext(),
                        "Você tem certeza",
                        "Clique no botão para confirmar que quer deletar o estilo",
                        okClick = {
                            stylesViewModel.deleteData(style.id)
                        }
                    ).buildDialog()
                }), { index, data ->
                    data.action.invoke()
                }, DialogStyles.BOTTOM_NO_BORDER).buildDialog()
            } else {
                goToStyle()
            }


        }
        stylesRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
    }


    private fun goToStyle(style: Style? = null) {
        val bundle = bundleOf("style" to style)
        findNavController().navigate(R.id.action_navigation_styles_to_newStyleFragment, bundle)

    }
}