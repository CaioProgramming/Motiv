package com.ilustris.motiv.manager.features.cover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.MediaType
import com.giphy.sdk.ui.pagination.GPHContent
import com.giphy.sdk.ui.views.GPHGridCallback
import com.giphy.sdk.ui.views.GiphyGridView
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.adapters.RecyclerCoverAdapter
import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.manager.databinding.FragmentCoversBinding
import com.ilustris.motiv.manager.features.cover.alert.CoverBottomSheetAlert
import com.ilustris.ui.extensions.gone
import com.ilustris.ui.extensions.showSnackBar
import com.silent.ilustriscore.core.model.ViewModelBaseState
import timber.log.Timber

class CoversFragment : SearchView.OnQueryTextListener, Fragment() {

    var coversBinding: FragmentCoversBinding? = null
    val coversViewModel by lazy { CoversViewModel(requireActivity().application) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        coversBinding = FragmentCoversBinding.inflate(inflater)
        return coversBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coversBinding?.initView()
    }

    private fun FragmentCoversBinding.initView() {
        searchview.setQuery("Aesthetic", true)
        observeViewModel()
        coversViewModel.getAllData()
    }

    private fun observeViewModel() {
        coversViewModel.viewModelState.observe(viewLifecycleOwner) {
            when (it) {
                ViewModelBaseState.DataDeletedState -> {
                    coversViewModel.getAllData()
                }
                is ViewModelBaseState.DataRetrievedState -> TODO()
                is ViewModelBaseState.DataListRetrievedState -> {
                    setupRecycler(it.dataList as ArrayList<Cover>)
                }
                is ViewModelBaseState.DataSavedState -> {
                    coversViewModel.getAllData()
                }
                is ViewModelBaseState.FileUploadedState -> TODO()
                is ViewModelBaseState.ErrorState -> {
                    view?.showSnackBar(it.dataException.code.message)
                }

                else -> {}
            }
        }
    }

    private fun setupRecycler(covers: ArrayList<Cover>) {
        coversBinding?.coversRecycler?.run {
            adapter = RecyclerCoverAdapter(covers) {
                BottomSheetAlert(
                    requireContext(),
                    "Não gostou?",
                    "Caso queira apagar esse fundo basta confirmar",
                    {
                        coversViewModel.deleteData(it.id)
                    }).buildDialog()
            }
            layoutManager = LinearLayoutManager(requireContext())
            slideInBottom()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        coversBinding?.run {
            if (!query.isNullOrEmpty()) {
                gifsGridView.content = GPHContent.searchQuery(query, mediaType = MediaType.gif)
                gifsGridView.direction = GiphyGridView.HORIZONTAL
                gifsGridView.spanCount = 2
                gifsGridView.callback = object : GPHGridCallback {
                    override fun contentDidUpdate(resultCount: Int) {
                        Timber.i(javaClass.simpleName, "contentDidUpdate: new gifs $resultCount")
                        if (resultCount > 0) {
                            gifsGridView.slideInBottom()
                        } else {
                            gifsGridView.gone()
                        }
                    }

                    override fun didSelectMedia(media: Media) {
                        media.images.downsizedMedium?.gifUrl?.let { gif ->
                            CoverBottomSheetAlert(
                                requireContext(),
                                "Gostou desse gif?",
                                "Se gostou confirma ai pra salvar",
                                gif,
                                okClick = {
                                    coversViewModel.saveData(Cover(url = gif))
                                }).buildDialog()

                        }
                    }
                }

            }
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        coversBinding = null
    }

}