package com.creat.motiv.Adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.creat.motiv.Beans.Likes;
import com.creat.motiv.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.MyViewHolder> {
    private List<Likes> likesList;
    private Activity activity;

    public LikeAdapter(List<Likes> likesList, Activity activity) {
        this.likesList = likesList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(activity);
        view = mInflater.inflate(R.layout.like_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Likes likes = likesList.get(holder.getAdapterPosition());
        Log.println(Log.INFO, "loaded like ", likes.getUsername());
        holder.nome.setText(likes.getUsername());
        Glide.with(activity).load(likes.getUserpic()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.pic.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_astronaut));
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.pic.setImageDrawable(resource);
                return false;
            }
        }).into(holder.pic);
        final Animation in = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
        holder.pic.startAnimation(in);


    }


    @Override
    public int getItemCount() {
        return likesList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        CircleImageView pic;


        MyViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.username);
            pic = view.findViewById(R.id.userpic);


        }
    }
}

