package com.creat.motiv.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Pref;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerReferencesAdapter extends RecyclerView.Adapter<RecyclerReferencesAdapter.MyViewHolder>  {
    private List<String> mData;
    private Activity mActivity;


    public RecyclerReferencesAdapter( List<String> mData,
                                     Activity mActivity) {
        this.mData = mData;
        this.mActivity = mActivity;
     }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        view = mInflater.inflate(R.layout.references, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

            holder.nome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(mData.get(position));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    mActivity.startActivity(intent);
                }
            });
        switch (position) {
            case 0:
                Glide.with(mActivity).load(mActivity.getString(R.string.dribblelogo)).into(holder.logo);
                holder.nome.setText("Dribble");
                break;
            case 1:
                Glide.with(mActivity).load(mActivity.getString(R.string.flaticonlogo)).into(holder.logo);
                holder.nome.setText("Flaticon");
                break;
            case 2:
                Glide.with(mActivity).load(mActivity.getString(R.string.materialiologo)).into(holder.logo);
                holder.nome.setText("Material.io");
                break;
            case 3:
                Glide.with(mActivity).load(mActivity.getString(R.string.undrawlogo)).into(holder.logo);
                holder.nome.setText("Undraw");
                break;
        }
        Pref preferences = new Pref(mActivity);
        int white = Color.WHITE;


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
        CircleImageView logo;
        public MyViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.brandtext);
            logo = view.findViewById(R.id.brand);



        }
    }
}
