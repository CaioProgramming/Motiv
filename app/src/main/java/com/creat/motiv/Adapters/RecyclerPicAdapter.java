package com.creat.motiv.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
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

        Glide.with(mActivity).load(mData.get(position).getUri()).into(holder.pic);



            holder.pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(mData.get(position).getUri())).build();
                    if (user != null) {
                        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    for (int i = 0; i< myquotes.size();i++){
                                        quotesDB = new QuotesDB();
                                        quotesDB.AlterarFoto(mActivity,myquotes.get(i).getId(), String.valueOf(user.getPhotoUrl()));
                                    }
                                    Snacky.builder().setActivity(Objects.requireNonNull(mActivity)).success().setText("Foto de perfil alterada").show();
                                    myDialog.dismiss();
                                    myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            Animation out = AnimationUtils.loadAnimation(mContext,R.anim.mi_fade_out);
                                            blurView.startAnimation(out);
                                            blurView.setOverlayColor(Color.TRANSPARENT);
                                            blurView.setBlurRadius(0);
                                        }
                                    });

                                } else {
                                    Snacky.builder().setActivity(Objects.requireNonNull(mActivity)).success().setText("Erro " + task.getException()).show();
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

        public MyViewHolder(View view) {
            super(view);
            pic = itemView.findViewById(R.id.pic);






        }
    }
}
