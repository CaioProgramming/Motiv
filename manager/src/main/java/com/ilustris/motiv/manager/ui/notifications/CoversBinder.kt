package com.ilustris.motiv.manager.ui.notifications

import android.util.Log
import android.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.pagination.GPHContent
import com.giphy.sdk.ui.views.GPHGridCallback
import com.ilustris.animations.fadeIn
import com.ilustris.motiv.base.adapters.RecyclerCoverAdapter
import com.ilustris.motiv.base.beans.Cover
import com.ilustris.motiv.base.dialog.BottomSheetAlert
import com.ilustris.motiv.base.model.CoversPresenter
import com.ilustris.motiv.manager.databinding.FragmentCoversBinding
import com.silent.ilustriscore.core.view.BaseView

class CoversBinder(override val viewBind: FragmentCoversBinding, val fragmentManager: FragmentManager) : BaseView<Cover>(), androidx.appcompat.widget.SearchView.OnQueryTextListener {
    override val presenter = CoversPresenter(this)

    init {
        initView()
    }

    override fun initView() {
        presenter.loadData()
        viewBind.searchView.run {
            setOnQueryTextListener(this@CoversBinder)
            setQuery("Aesthetic", true)
        }
    }

    override fun showListData(list: List<Cover>) {
        super.showListData(list)
        viewBind.coversRecycler.run {
            adapter = RecyclerCoverAdapter(list) {
                BottomSheetAlert.buildAlert(fragmentManager, "Não gostou?", "Caso queira apagar esse ícone basta confirmar", { presenter.deleteData(it) })
            }
            layoutManager = LinearLayoutManager(context)
            fadeIn()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            if (query.isNotBlank()) {
                viewBind.gifsGridView.content = GPHContent.searchQuery(it)
                viewBind.gifsGridView.callback = object : GPHGridCallback {
                    override fun contentDidUpdate(resultCount: Int) {
                        Log.i(javaClass.simpleName, "contentDidUpdate: new gifs $resultCount")
                    }

                    override fun didSelectMedia(media: Media) {
                        media.bitlyGifUrl?.let {
                            BottomSheetAlert
                                    .buildAlert(fragmentManager, "Gostou desse gif?", "Se gostou confirma ai pra salvar", okClick = {
                                        presenter.saveData(Cover(url = it))
                                    })
                        }

                    }
                }
            }
        }
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }

}