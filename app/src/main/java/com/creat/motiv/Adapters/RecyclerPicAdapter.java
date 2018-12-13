package com.creat.motiv.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.creat.motiv.Beans.Pics;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.List;

public class RecyclerPicAdapter extends RecyclerView.Adapter<RecyclerPicAdapter.MyViewHolder>  {
    private QuotesDB quotesDB;
    private Context mContext;
    private List<Pics> mData;
    private ArrayList<Quotes> myquotes;
    private RealtimeBlurView blurView;
    private BottomSheetDialog myDialog;
    private TextView message, remove;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Activity mActivity;


    public RecyclerPicAdapter(QuotesDB quotesDB, Context mContext, List<Pics> mData, RealtimeBlurView blurView,
                              Activity mActivity, BottomSheetDialog myDialog, ArrayList<Quotes> myquotes,
                              TextView message, TextView remove, ProgressBar progressBar, RecyclerView recyclerView) {
        this.quotesDB = quotesDB;
        this.mContext = mContext;
        this.mData = mData;
        this.blurView = blurView;
        this.mActivity = mActivity;
        this.myDialog = myDialog;
        this.myquotes = myquotes;
        this.message = message;
        this.remove = remove;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
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
        Animation in = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom);

        Glide.with(mActivity).load(mData.get(position).getUri()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource != null) {
                    Animation in = AnimationUtils.loadAnimation(mContext, R.anim.pop_in);
                    holder.pic.setImageDrawable(resource);
                    holder.pic.startAnimation(in);
                    return true;
                }

                return false;
            }
        }).into(holder.pic);
        holder.pic.startAnimation(in);

            holder.pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    remove.setVisibility(View.GONE);
                    CountDownTimer timer = new CountDownTimer(3000, 100) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {

                        }
                    }.start();
                   UserProfileChangeRequest  profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(mData.get(position).getUri())).build();
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    for (int i = 0; i < myquotes.size(); i++) {
                                        quotesDB = new QuotesDB(mActivity, null);
                                        quotesDB.AlterarFoto(mActivity, myquotes.get(i).getId(), String.valueOf(user.getPhotoUrl()));

                                    }
                                    progressBar.setVisibility(View.VISIBLE);
                                    CountDownTimer timer = new CountDownTimer(6000, 100) {
                                        @Override
                                        public void onTick(long l) {

                                        }

                                        @Override
                                        public void onFinish() {

                                            remove.setVisibility(View.GONE);
                                            message.setVisibility(View.VISIBLE);
                                            message.setBackgroundColor(Color.WHITE);
                                            message.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                                            progressBar.setVisibility(View.GONE);
                                            Animation out = AnimationUtils.loadAnimation(mContext, R.anim.fab_scale_down);
                                            Animation in = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                                            message.startAnimation(in);
                                            recyclerView.startAnimation(out);
                                            recyclerView.setVisibility(View.GONE);
                                        }
                                    }.start();

                                }

                            }
                        });

                    }

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
        MyViewHolder(View view) {
            super(view);
            pic = itemView.findViewById(R.id.pic);






        }
    }
}
