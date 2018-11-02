package com.creat.motiv.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.creat.motiv.Beans.Fonts;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Tools;
import com.github.mmin18.widget.RealtimeBlurView;

import java.util.List;

public class RecyclerFontAdapter extends RecyclerView.Adapter<RecyclerFontAdapter.MyViewHolder>  {
    private Context mContext;
    private List<Fonts> mData;
    private RealtimeBlurView blurView;
    private BottomSheetDialog myDialog;
    private EditText quote,author;
    private TextView select;

    public RecyclerFontAdapter(Context mContext, RealtimeBlurView blurView, BottomSheetDialog myDialog,
                               EditText quote, EditText author, TextView select) {
        this.mContext = mContext;
        this.mData = Tools.fonts(mContext);
        this.blurView = blurView;
        this.myDialog = myDialog;
        this.quote = quote;
        this.author = author;
        this.select = select;

     }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.fonts,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        if (mData.get(position) == null){
            holder.layout.setVisibility(View.INVISIBLE);
        }
        holder.font.setText(mData.get(position).getFontname());
        holder.fontview.setTypeface(mData.get(position).getFont());

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select.setText(String.valueOf(position));
                    quote.setTypeface(mData.get(position).getFont());
                    author.setTypeface(mData.get(position).getFont());
                    myDialog.dismiss();
                    myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            blurView.setBlurRadius(0);
                            blurView.setOverlayColor(Color.TRANSPARENT);
                        }
                    });
                 }
            });
    }



    @Override
    public int getItemCount() {
        if(mData.size() == 0){
            return 0;

        }else{
            return mData.size();}
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView font,fontview;
        CardView layout;
        public MyViewHolder(View view) {
            super(view);
            font = itemView.findViewById(R.id.font);
            fontview = itemView.findViewById(R.id.fonttest);
            layout = itemView.findViewById(R.id.layout);






        }
    }
}
