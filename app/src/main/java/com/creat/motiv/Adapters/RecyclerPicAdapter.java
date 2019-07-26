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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Objects;

import de.mateware.snacky.Snacky;

public class RecyclerPicAdapter extends RecyclerView.Adapter<RecyclerPicAdapter.MyViewHolder>  {
    private QuotesDB quotesDB;
    private Context mContext;
    private List<Pics> mData;
    private ArrayList<Quotes> myquotes;
    private BottomSheetDialog myDialog;
    LinearLayout back;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Activity mActivity;
    private TextView message, remove, title;


    public RecyclerPicAdapter(QuotesDB quotesDB, Context mContext, List<Pics> mData, RealtimeBlurView blurView,
                              Activity mActivity, BottomSheetDialog myDialog, ArrayList<Quotes> myquotes,
                              TextView message, TextView remove, ProgressBar progressBar, RecyclerView recyclerView, TextView title, LinearLayout back) {
        this.quotesDB = quotesDB;
        this.mContext = mContext;
        this.mData = mData;
        RealtimeBlurView blurView1 = blurView;
        this.mActivity = mActivity;
        this.myDialog = myDialog;
        this.myquotes = myquotes;
        this.message = message;
        this.remove = remove;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
        this.title = title;
        this.back = back;
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
        Animation in = AnimationUtils.loadAnimation(mContext, R.anim.fab_scale_up);
        holder.loading.setVisibility(View.VISIBLE);


        if (mData.get(position) == null) {
            holder.delete.setVisibility(View.GONE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(null).build();
                    if (user != null) {
                        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    for (int i = 0; i < myquotes.size(); i++) {
                                        quotesDB = new QuotesDB();
                                        quotesDB.AlterarFoto(mActivity, myquotes.get(i).getId(), String.valueOf(user.getPhotoUrl()));
                                    }
                                    Snacky.builder().setActivity(Objects.requireNonNull(mActivity)).success().setText("Foto de perfil alterada").show();
                                    myDialog.dismiss();


                                } else {
                                    Snacky.builder().setActivity(mActivity).success().setText("Erro " + task.getException()).show();
                                }


                            }
                        });


                    }


                }
            });
            holder.pic.setVisibility(View.GONE);
        } else {


            Glide.with(mActivity).load(mData.get(position).getUri()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.pic.setVisibility(View.GONE);
                    holder.message.setVisibility(View.VISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    Animation in = AnimationUtils.loadAnimation(mContext, R.anim.pop_in);


                    if (resource != null) {
                        Animation out = AnimationUtils.loadAnimation(mContext, R.anim.fab_scale_down);
                        holder.loading.startAnimation(out);
                        holder.loading.setVisibility(View.GONE);
                        holder.pic.setImageDrawable(resource);
                        holder.pic.startAnimation(in);

                        return true;
                    } else {
                        holder.loading.setVisibility(View.VISIBLE);
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
                    back.setVisibility(View.GONE);
                    title.setVisibility(View.GONE);
                    remove.setVisibility(View.GONE);
                    CountDownTimer timer = new CountDownTimer(3000, 100) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {

                        }
                    }.start();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(mData.get(position).getUri())).build();
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.getPhotoUrl() == Uri.parse(mData.get(position).getUri())) {
                        message.setText("Seu ícone de perfil já é este!");
                        message.setVisibility(View.VISIBLE);
                        remove.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Animation out = AnimationUtils.loadAnimation(mContext, R.anim.fab_scale_down);
                        Animation in = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                        message.startAnimation(in);
                        recyclerView.startAnimation(out);
                        recyclerView.setVisibility(View.GONE);

                    } else {
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
                                            message.setBackgroundColor(Color.TRANSPARENT);
                                            message.setTextColor(Color.WHITE);
                                            progressBar.setVisibility(View.GONE);
                                            Animation out = AnimationUtils.loadAnimation(mContext, R.anim.fab_scale_down);
                                            Animation in = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                                            message.startAnimation(in);
                                            recyclerView.startAnimation(out);
                                            recyclerView.setVisibility(View.GONE);
                                            CountDownTimer timer2 = new CountDownTimer(5000, 100) {
                                                @Override
                                                public void onTick(long l) {

                                                }

                                                @Override
                                                public void onFinish() {
                                                    myDialog.dismiss();
                                                }
                                            }.start();
                                        }
                                    }.start();

                                }

                            }
                        });

                    }

                }
            });
        }

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
        ImageButton delete;
        TextView message;
        ProgressBar loading;
        MyViewHolder(View view) {
            super(view);
            delete = itemView.findViewById(R.id.delete);
            pic = itemView.findViewById(R.id.pic);
            message = itemView.findViewById(R.id.error);
            loading = itemView.findViewById(R.id.loading);






        }
    }
}
