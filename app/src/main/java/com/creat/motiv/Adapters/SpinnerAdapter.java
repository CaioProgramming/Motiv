package com.creat.motiv.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.creat.motiv.R;
import com.creat.motiv.Utils.Tools;
import com.github.mmin18.widget.RealtimeBlurView;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private List<Typeface> mData;
     private EditText quote,author;
     private LayoutInflater layoutInflater;

    public SpinnerAdapter(Context mContext, List<Typeface> mData, EditText quote, EditText author, LayoutInflater layoutInflater) {
        this.mContext = mContext;
        this.mData = mData;
        this.quote = quote;
        this.author = author;
        this.layoutInflater =(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
           layoutInflater.inflate(R.layout.fonts,null);
          TextView fontview = convertView.findViewById(R.id.fonttest);
           fontview.setTypeface(mData.get(position));
           fontview.setText(Tools.frases[position]);

       }
        return convertView;
    }
}
