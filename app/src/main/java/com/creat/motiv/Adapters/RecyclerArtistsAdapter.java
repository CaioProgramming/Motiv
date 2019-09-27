package com.creat.motiv.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creat.motiv.Beans.Artists;
import com.creat.motiv.R;

import java.util.List;

public class RecyclerArtistsAdapter extends RecyclerView.Adapter<RecyclerArtistsAdapter.MyViewHolder>  {
    private Context mContext;
    private List<Artists> mData;
    private Activity mActivity;


    public RecyclerArtistsAdapter(Context mContext, List<Artists> mData,
                                  Activity mActivity) {
        this.mContext = mContext;
        this.mData = mData;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.references_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.nome.setText(mData.get(position).getNome());
        holder.nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(mData.get(position).getUri());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mActivity.startActivity(intent);
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
        TextView nome;
        LinearLayout colors;
        CardView layout;


        public MyViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.reference);
            layout = view.findViewById(R.id.layout);






        }
    }
}
