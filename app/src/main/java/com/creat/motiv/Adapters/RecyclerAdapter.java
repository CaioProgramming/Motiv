package com.creat.motiv.Adapters;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>  {
    private QuotesDB quotesDB;
    private Context mContext;
    private Dialog myDialog;
    private List<Quotes> mData;
    private RealtimeBlurView blurView;
    private GestureDetectorCompat mDetector;


    public RecyclerAdapter(QuotesDB quotesDB, Context mContext, List<Quotes> mData, RealtimeBlurView blurView) {
        this.quotesDB = quotesDB;
        this.mContext = mContext;
        this.mData = mData;
        this.blurView = blurView;
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
        holder.cardView.setVisibility(View.VISIBLE);
            if (mData.get(position).getQuote().equals("Ad")){
                AdView adView = new AdView(mContext);
                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
                AdRequest adRequest = new AdRequest.Builder().build();
               holder.adView.loadAd(adRequest);
                holder.adView.setVisibility(View.VISIBLE);
                holder.data.setVisibility(View.INVISIBLE);
            }
        Animation faAnimation = AnimationUtils.loadAnimation(mContext,R.anim.fade_in);
            if (mData.get(position).getBackgroundcolor() != 0){
                holder.cardView.setBackgroundColor(mData.get(position).getBackgroundcolor());
            }
            if (mData.get(position).getTextcolor() != 0){
                holder.quote.setTextColor(mData.get(position).getTextcolor());
                holder.author.setTextColor(mData.get(position).getTextcolor());
            }
            holder.quote.setText(mData.get(position).getQuote());
            holder.author.setText(mData.get(position).getAuthor());
            holder.quote.startAnimation(faAnimation);
            holder.author.startAnimation(faAnimation);
            if(mData.get(position).getCategoria().equals("Musica")){
                    holder.category.setBackgroundResource(R.color.md_deep_purple_300);

            }else if(mData.get(position).getCategoria().equals("Citação")){
                holder.category.setBackgroundResource(R.color.md_grey_300);
            }else if(mData.get(position).getCategoria().equals("Amor")){
                holder.category.setBackgroundResource(R.color.md_red_300);

            }else if(mData.get(position).getCategoria().equals("Motivação")){
                holder.category.setBackgroundResource(R.color.md_deep_orange_300);

            }else if(mData.get(position).getCategoria().equals("Nenhum")){
                holder.category.setBackgroundResource(R.color.black);

            }

            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (holder.cardView.isPressed()){
                    Popup(position);
                    return true;}
                    return false;
                }
            });

            holder.content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (holder.content.isPressed()){
                        Popup(position);
                        return true;}
                    return false;
                }
            });

            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quotesDB.Remover();
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
        AdView adView;
        CheckBox like;
        ImageButton remove;
        ScrollView data;
        TextView quote,author;
        RelativeLayout back;
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
            adView = view.findViewById(R.id.adView);
            data = view.findViewById(R.id.data);




        }
    }
}
