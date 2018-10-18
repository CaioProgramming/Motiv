package com.creat.motiv.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.mateware.snacky.Snacky;

import static com.creat.motiv.Database.QuotesDB.path;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>  {
    private QuotesDB quotesDB;
    private Context mContext;
    private Dialog myDialog;
    private List<Quotes> mData;
    private RealtimeBlurView blurView;
    private Activity mActivity;
    private GestureDetectorCompat mDetector;


    public RecyclerAdapter(QuotesDB quotesDB, Context mContext, List<Quotes> mData, RealtimeBlurView blurView, Activity mActivity) {
        this.quotesDB = quotesDB;
        this.mContext = mContext;
        this.mData = mData;
        this.blurView = blurView;
        this.mActivity = mActivity;
     }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.quotescard,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Animation in = AnimationUtils.loadAnimation(mContext,R.anim.pop_in);
        holder.cardView.startAnimation(in);
        if (mData.get(position).getUsername() != null || mData.get(position).getUserphoto() != null ){
            System.out.println(mData.get(position).getUserphoto());
            System.out.println(mData.get(position).getUsername());
            Glide.with(mContext).load(mData.get(position).getUserphoto()).into(holder.userpic);
            holder.username.setText(mData.get(position).getUsername());
        }else {
            holder.username.setVisibility(View.INVISIBLE);
            holder.userpic.setVisibility(View.INVISIBLE);

        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (mData.get(position).getUserID().equals(user.getUid())){
                holder.remove.setVisibility(View.VISIBLE);
            }else{
                holder.remove.setVisibility(View.INVISIBLE);
            }
        Animation faAnimation = AnimationUtils.loadAnimation(mContext,R.anim.fade_in);
            if (mData.get(position).getBackgroundcolor() != 0){
                holder.back.setBackgroundColor(mData.get(position).getBackgroundcolor());
            }
            if (mData.get(position).getTextcolor() != 0){
                holder.quote.setTextColor(mData.get(position).getTextcolor());
                holder.author.setTextColor(mData.get(position).getTextcolor());
            }
            holder.quote.setText(mData.get(position).getQuote());
            holder.author.setText(mData.get(position).getAuthor());
            holder.quote.startAnimation(faAnimation);
            holder.author.startAnimation(faAnimation);
        switch (mData.get(position).getCategoria()) {
            case "Musica":
                holder.category.setBackgroundResource(R.color.md_deep_purple_300);

                break;
            case "Citação":
                holder.category.setBackgroundResource(R.color.md_grey_300);
                break;
            case "Amor":
                holder.category.setBackgroundResource(R.color.md_red_300);

                break;
            case "Motivação":
                holder.category.setBackgroundResource(R.color.md_deep_orange_300);

                break;
            case "Nenhum":
                holder.category.setBackgroundResource(R.color.black);

                break;
        }



            holder.remove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Remover(position, holder);

                }
            });






    }

    private void Remover(final int position, @NonNull final MyViewHolder holder) {
        DatabaseReference raiz;
        raiz = FirebaseDatabase.getInstance().getReference(path);
        raiz.child(mData.get(position).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                Animation out = AnimationUtils.loadAnimation(mContext, R.anim.pop_out);
                holder.cardView.startAnimation(out);
                holder.cardView.setVisibility(View.INVISIBLE);
                }else {
                    Snacky.builder().setActivity(mActivity).error().setText("Erro " + task.getException().getMessage()).show();

                }

            }
        });
    }

    private void Popup(int position) {
        blurView.setBlurRadius(25);
        blurView.setDownsampleFactor(10);
        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.quotepopup);
        TextView quote,author;
        LinearLayout quoteback;
        quote = myDialog.findViewById(R.id.quote);
        author = myDialog.findViewById(R.id.author);
        quoteback = myDialog.findViewById(R.id.content);
        quote.setText(mData.get(position).getQuote());
        author.setText(mData.get(position).getAuthor());
        quoteback.setBackgroundColor(mData.get(position).getBackgroundcolor());
        quote.setTextColor(mData.get(position).getTextcolor());
        author.setTextColor(mData.get(position).getTextcolor());
        myDialog.show();
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                blurView.setBlurRadius(0);
                blurView.setOverlayColor(Color.TRANSPARENT);
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
        CardView cardView;
        CheckBox like;
        ImageButton remove;
        ScrollView data;
        ImageView userpic;
        TextView quote,author,username;
        LinearLayout back;
        LinearLayout content,category;

        public MyViewHolder(View view) {
            super(view);
            remove = view.findViewById(R.id.remove);
            quote = view.findViewById(R.id.quote);
            author = view.findViewById(R.id.author);
            cardView = view.findViewById(R.id.card);
            back = view.findViewById(R.id.background);
            content = view.findViewById(R.id.content);
            category = view.findViewById(R.id.category);
            username = view.findViewById(R.id.username);
            userpic = view.findViewById(R.id.userpic);





        }
    }
}
