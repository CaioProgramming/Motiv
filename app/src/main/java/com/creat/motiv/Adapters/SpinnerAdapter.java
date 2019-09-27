package com.creat.motiv.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.creat.motiv.R;
import com.creat.motiv.Utils.Tools;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private List<Typeface> mData;
     private EditText quote,author;
    private TextView select;
     private LayoutInflater layoutInflater;
    Spinner selector;

    public SpinnerAdapter(Context mContext, EditText quote, EditText author, TextView select, Spinner selector) {
        this.mContext = mContext;
        this.mData = Tools.fonts(mContext);
        this.quote = quote;
        this.author = author;
        this.select = select;
        this.selector = selector;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
          convertView =  layoutInflater.inflate(R.layout.fonts,null);
          TextView fontview = convertView.findViewById(R.id.fonttest);
           fontview.setTypeface(mData.get(position));
            convertView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   quote.setTypeface(mData.get(position));
                   author.setTypeface(mData.get(position));
                   select.setText(String.valueOf(position));
                   selector.setSelection(position,true);

               }
           });

       }
        return convertView;
    }
}
