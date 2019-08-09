package com.creat.motiv.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.Developers;
import com.creat.motiv.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        view = mInflater.inflate(R.layout.references, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Animation in = AnimationUtils.loadAnimation(mContext,R.anim.pop_in);
        Glide.with(mActivity).load(mData.get(position).getPhotouri()).into(holder.profilepic);
        holder.profilepic.startAnimation(in);
        holder.nome.setText(mData.get(position).getNome());



    }



    @Override
    public int getItemCount() {
        if(mData.size() == 0){
            return 0;

        }else{
            return mData.size();}
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profilepic;
        TextView nome;



        private MyViewHolder(View view) {
            super(view);
            profilepic = view.findViewById(R.id.brand);
            nome = view.findViewById(R.id.brandtext);







        }
    }
}
