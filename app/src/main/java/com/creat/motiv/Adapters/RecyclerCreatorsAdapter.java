package com.creat.motiv.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.Developers;
import com.creat.motiv.R;

import java.util.ArrayList;

public class RecyclerCreatorsAdapter extends RecyclerView.Adapter<RecyclerCreatorsAdapter.MyViewHolder>  {
    private Context mContext;
    private ArrayList <Developers> mData;
    private Activity mActivity;


    public RecyclerCreatorsAdapter( Context mContext,ArrayList<Developers> mData,
                                   Activity mActivity ) {
        this.mContext = mContext;
        this.mData = mData;
        this.mActivity = mActivity;
     }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.creators_card,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Animation in = AnimationUtils.loadAnimation(mContext,R.anim.pop_in);
        Glide.with(mActivity).load(mData.get(position).getPhotouri()).into(holder.profilepic);
        holder.profilepic.startAnimation(in);
        holder.nome.setText(mData.get(position).getNome());
        holder.cargo.setText(mData.get(position).getCargo());
        holder.linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActivity == null) {
                    return;
                }
                Uri uri = Uri.parse(mData.get(position).getLinkedin());
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


    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profilepic;
        TextView nome, cargo;
        ImageButton linkedin;


        private MyViewHolder(View view) {
            super(view);
            profilepic = view.findViewById(R.id.profilepic);
            nome = view.findViewById(R.id.nome);
            cargo = view.findViewById(R.id.cargo);
            linkedin = view.findViewById(R.id.linkedin);






        }
    }
}
