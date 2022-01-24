package com.ilustris.motiv.manager.features.style

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.motiv.base.beans.NEW_STYLE_ID
import com.ilustris.motiv.base.beans.Style
import com.ilustris.motiv.base.dialog.DefaultAlert
import com.ilustris.motiv.base.dialog.listdialog.DialogData
import com.ilustris.motiv.base.dialog.listdialog.ListDialog
import com.ilustris.motiv.manager.databinding.StylesRecyclerBinding
import com.ilustris.motiv.manager.features.style.adapter.StylePreviewAdapter
import com.ilustris.motiv.manager.features.style.viewmodel.StylesViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.DialogStyles
import com.silent.ilustriscore.core.utilities.showSnackBar

class StylesFragment : Fragment() {

    val stylesViewModel = StylesViewModel()
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
        stylesViewModel.viewModelState.observe(this, {
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
            }
        })
    }

    private fun StylesRecyclerBinding.setupRecycler(styles: ArrayList<Style>) {
        stylesRecycler.adapter = StylePreviewAdapter(styles, false, null) {

            if (it.id != NEW_STYLE_ID) {
                ListDialog(requireContext(), listOf(DialogData("Editar") {
                    editStyle(it)
                }, DialogData("Excluir") {
                    DefaultAlert(
                        requireContext(),
                        "Você tem certeza",
                        "Clique no botão para confirmar que quer deletar o estilo",
                        okClick = {
                            stylesViewModel.deleteData(it.id)
                        }
                    ).buildDialog()
                }), { index, data ->
                    data.action.invoke()
                }, DialogStyles.BOTTOM_NO_BORDER).buildDialog()
            } else {
                goToNewStyle()
            }


        }
        stylesRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
    }

    private fun goToNewStyle() {

    }

    private fun editStyle(it: Style) {

    }
}