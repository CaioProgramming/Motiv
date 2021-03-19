package com.ilustris.motiv.manager.ui.covers

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
import com.ilustris.motiv.base.utils.activity
import com.ilustris.motiv.manager.databinding.FragmentCoversBinding
import com.silent.ilustriscore.core.view.BaseView

class CoversBinder(override val viewBind: FragmentCoversBinding) : BaseView<Cover>(), androidx.appcompat.widget.SearchView.OnQueryTextListener {
    override val presenter = CoversPresenter(this)

    init {
        initView()
    }

    override fun initView() {
        presenter.loadData()
        viewBind.searchview.run {
            setOnQueryTextListener(this@CoversBinder)
            setQuery("Aesthetic", true)
        }
    }

    override fun showListData(list: List<Cover>) {
        super.showListData(list)
        viewBind.coversRecycler.run {
            adapter = RecyclerCoverAdapter(list) { cover ->
                BottomSheetAlert(context, "Não gostou?", "Caso queira apagar esse ícone basta confirmar", {
                    presenter.deleteData(cover)
                }).buildDialog()
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
                        media.images.downsizedMedium?.gifUrl?.let { gif ->
                            CoverBottomSheetAlert(context, "Gostou desse gif?", "Se gostou confirma ai pra salvar", gif, okClick = {
                                presenter.saveData(Cover(url = gif))
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