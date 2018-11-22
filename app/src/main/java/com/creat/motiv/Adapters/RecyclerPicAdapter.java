package com.creat.motiv.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.Pics;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.creat.motiv.Updateuseract;
import com.github.mmin18.widget.RealtimeBlurView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerPicAdapter extends RecyclerView.Adapter<RecyclerPicAdapter.MyViewHolder>  {
    private QuotesDB quotesDB;
    private Context mContext;
    private List<Pics> mData;
    private ArrayList<Quotes> myquotes;
    private RealtimeBlurView blurView;
    private BottomSheetDialog myDialog;
    private Activity mActivity;


    public RecyclerPicAdapter(QuotesDB quotesDB, Context mContext, List<Pics> mData, RealtimeBlurView blurView,
                              Activity mActivity,BottomSheetDialog myDialog,ArrayList<Quotes> myquotes) {
        this.quotesDB = quotesDB;
        this.mContext = mContext;
        this.mData = mData;
        this.blurView = blurView;
        this.mActivity = mActivity;
        this.myDialog = myDialog;
        this.myquotes = myquotes;
     }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.pics,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Animation in = AnimationUtils.loadAnimation(mContext,R.anim.slide_in);

        Glide.with(mActivity).load(mData.get(position).getUri()).into(holder.pic);
        holder.pic.startAnimation(in);

            holder.pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(mActivity, Updateuseract.class);
                    update.putExtra("photouri",mData.get(position).getUri());
                    mContext.startActivity(update);
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
        ImageView pic;
        public MyViewHolder(View view) {
            super(view);
            pic = itemView.findViewById(R.id.pic);






        }
    }
}
