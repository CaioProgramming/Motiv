package com.creat.motiv.features.radio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.RadioPageCollapsedLayoutBinding
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.beans.Radio
import com.ilustris.motiv.base.utils.loadGif
import com.ilustris.ui.extensions.gone
import com.ilustris.ui.extensions.visible

class RadioAdapter(
    val radioList: ArrayList<Radio>,
    val onRadioClick: (Radio) -> Unit
) : RecyclerView.Adapter<RadioAdapter.RadioHolder>() {

    var currentRadio: Radio? = null
    var isPlaying: Boolean = false
    var enabled: Boolean = true

    fun updateCurrentRadio(radio: Radio) {
        this.currentRadio = radio
        val index = radioList.indexOf(radio)
        radioList.removeAt(index)
        radioList.add(0, radio)
        notifyItemInserted(0)
    }


    inner class RadioHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
            val context = itemView.context
            val radio = radioList[adapterPosition]
            RadioPageCollapsedLayoutBinding.bind(itemView).run {
                if (enabled) {
                    itemView.setOnClickListener {
                        updateEnabled(false)
                        onRadioClick(radio)
                    }
                }
                radioName.run {
                    text = radio.name

                }
                if (currentRadio != radio) {
                    radioName.setTextColor(ContextCompat.getColor(context, R.color.nwhite))
                    playAnimation.gone()
                } else {
                    if (isPlaying) {
                        visualizerBack.fadeIn()
                        playAnimation.visible()
                        playAnimation.playAnimation()
                    } else {
                        visualizerBack.fadeOut()
                        playAnimation.gone()
                        playAnimation.pauseAnimation()
                    }
                    radioName.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                visualizerBack.loadGif(radio.visualizer)
                visualizer.loadGif(radio.visualizer) {
                    if (!visualizerCard.isVisible) {
                        visualizerCard.slideInBottom()
                    }
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioHolder {
        return RadioHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.radio_page_collapsed_layout, parent, false),
        )
    }

    override fun onBindViewHolder(holder: RadioHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return radioList.size
    }

    fun updatePlaying(playing: Boolean) {
        isPlaying = playing
        notifyDataSetChanged()
    }

    fun updateEnabled(enable: Boolean) {
        enabled = enable
        notifyDataSetChanged()
    }

}