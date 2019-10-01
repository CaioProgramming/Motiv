package com.creat.motiv.Adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.creat.motiv.Beans.Pics;
import com.creat.motiv.Database.UserDB;
import com.creat.motiv.Fragments.ProfileFragment;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Alert;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerPicAdapter extends RecyclerView.Adapter<RecyclerPicAdapter.MyViewHolder>  {
    private List<Pics> mData;
    private Activity mActivity;
    private ProfileFragment profileFragment;
    private BottomSheetDialog dialog;
    public RecyclerPicAdapter(List<Pics> mData,
                              Activity mActivity, ProfileFragment pfragment, BottomSheetDialog myDialog) {
        this.mData = mData;
        this.mActivity = mActivity;
        this.profileFragment = pfragment;
        this.dialog = myDialog;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        view = mInflater.inflate(R.layout.pics,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Pics pic = mData.get(holder.getAdapterPosition());
        Animation in = AnimationUtils.loadAnimation(mActivity, R.anim.pop_out);
        holder.loading.setVisibility(View.VISIBLE);
        Glide.with(mActivity).load(pic.getUri()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.pic.setVisibility(View.GONE);
                holder.message.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Animation in = AnimationUtils.loadAnimation(mActivity, R.anim.pop_in);


                if (resource != null) {
                    Animation out = AnimationUtils.loadAnimation(mActivity, R.anim.pop_out);
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
        holder.delete.setVisibility(View.GONE);

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Alert a = new Alert(mActivity);
                dialog.dismiss();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (user.getPhotoUrl() == Uri.parse(mData.get(position).getUri())) {
                        a.Message(a.erroricon, "Seu ícone de perfil já é este!");

                    } else {
                        UserDB db = new UserDB(mActivity);
                        db.changeuserpic(profileFragment, pic.getUri());

                    }
                } else {
                    a.Message(mActivity.getDrawable(R.drawable.ic_broken_link), "Você está desconectado!");
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


    class MyViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        ImageView pic;
        ImageButton delete;
        TextView message;
        ProgressBar loading;
        MyViewHolder(View view) {
            super(view);
            card = itemView.findViewById(R.id.card);
            delete = itemView.findViewById(R.id.delete);
            pic = itemView.findViewById(R.id.pic);
            message = itemView.findViewById(R.id.error);
            loading = itemView.findViewById(R.id.loading);






        }
    }
}
