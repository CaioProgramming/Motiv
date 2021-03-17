package com.creat.motiv.radio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.R
import com.creat.motiv.databinding.RadioPageLayoutBinding

class StyleRadioAdapter(val radioList: List<Radio>) : RecyclerView.Adapter<StyleRadioAdapter.RadioHolder>() {


    inner class RadioHolder(private val styleTextPageLayoutBinding: RadioPageLayoutBinding) : RecyclerView.ViewHolder(styleTextPageLayoutBinding.root) {

        fun bind() {
            val radio = radioList[adapterPosition]
            styleTextPageLayoutBinding.radioName.text = radio.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioHolder {
        return RadioHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.radio_page_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RadioHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return radioList.size
    }

}