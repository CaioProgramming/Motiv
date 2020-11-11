package com.creat.motiv.view.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView

import com.creat.motiv.R
import com.creat.motiv.utilities.Tools

class SpinnerAdapter(private val mContext: Context, private val quote: EditText, private val author: EditText, private val select: TextView, internal var selector: Spinner) : BaseAdapter() {
    private val mData: List<Typeface>
    private val layoutInflater: LayoutInflater

    init {
        this.mData = Tools.fonts(mContext)
        this.layoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.fonts, null)
            val fontview = convertView!!.findViewById<TextView>(R.id.fonttest)
            fontview.typeface = mData[position]
            convertView.setOnClickListener {
                quote.typeface = mData[position]
                author.typeface = mData[position]
                select.text = position.toString()
                selector.setSelection(position, true)
            }

        }
        return convertView
    }
}
