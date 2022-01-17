package com.creat.motiv.features.radio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.databinding.RadioPageLayoutBinding
import com.ilustris.motiv.base.beans.Radio

class StyleRadioAdapter(val radioList: List<Radio>, val onRadioClick: () -> Unit) : RecyclerView.Adapter<StyleRadioAdapter.RadioHolder>() {

    var isPlaying = false

    fun updatePlayerStatus(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        notifyDataSetChanged()
    }

    inner class RadioHolder(private val styleTextPageLayoutBinding: RadioPageLayoutBinding) : RecyclerView.ViewHolder(styleTextPageLayoutBinding.root) {

        fun bind() {
            val radio = radioList[adapterPosition]
            styleTextPageLayoutBinding.radioName.run {
                text = radio.name
                setOnClickListener {
                    onRadioClick.invoke()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioHolder {
        return RadioHolder(
            RadioPageLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RadioHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return radioList.size
    }

}