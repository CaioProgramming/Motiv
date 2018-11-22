package com.creat.motiv.Adapters;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.creat.motiv.R;
import com.github.mmin18.widget.RealtimeBlurView;

import java.util.ArrayList;

public class RecyclerColorAdapter extends RecyclerView.Adapter<RecyclerColorAdapter.MyViewHolder> {
    ArrayList<Integer> ColorsList;
    private Context mContext;
    private LinearLayout background;
    private TextView textView,author;
    private Activity mActivity;
   private TextView textcolor,backcolor;

    public RecyclerColorAdapter(ArrayList<Integer> colorsList, Context mContext, LinearLayout background, TextView textView, TextView author,
                                Activity mActivity, TextView texcolor, TextView backcolor) {
        ColorsList = colorsList;
        this.mContext = mContext;
        this.background = background;
        this.textView = textView;
        this.mActivity = mActivity;
        this.author = author;
        this.textcolor = texcolor;
        this.backcolor = backcolor;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.color_card,parent,false);

        return new RecyclerColorAdapter.MyViewHolder(view);
    }

    @NonNull




    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

             System.out.println(ColorsList.get(position));
        Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_bottom);

            holder.colorcard.setBackgroundColor(ColorsList.get(position));
            holder.colorcard.startAnimation(animation);
        holder.colorcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final RealtimeBlurView blurView = mActivity.findViewById(R.id.rootblur);

                String options[]={
                  "Texto",
                  "Plano de fundo"
                };
                final int color =  ColorsList.get(position);

                AlertDialog.Builder builder= new AlertDialog.Builder(mActivity);
                builder.setTitle("Plano de fundo ou texto")

                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0){
                                    textView.setTextColor(color);
                                    author.setTextColor(color);
                                    textcolor.setText(String.valueOf(color));

                                    System.out.println(textcolor.getText());
                                }else{
                                    background.setVisibility(View.INVISIBLE);
                                    background.setBackgroundColor(color);
                                    int cx = background.getRight();
                                    int cy = background.getTop();
                                    int radius = Math.max(background.getWidth(), background.getHeight());
                                    Animator anim = ViewAnimationUtils.createCircularReveal(background, cx, cy,
                                            0, radius);
                                    background.setVisibility(View.VISIBLE);
                                    anim.start();
                                    background.setBackgroundColor(color);
                                    backcolor.setText(String.valueOf(color));
                                    System.out.println(backcolor.getText());
                                }
                            }
                        });
                builder.show();


            }
        });

    }


    @Override
    public int getItemCount() {
        if (ColorsList.size() == 0){
            return 0;
        }else{
            return ColorsList.size();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CardView colorcard;
        Button morecolors;
        public MyViewHolder(View itemView) {
            super(itemView);
            colorcard = itemView.findViewById(R.id.colorcard);
            morecolors = itemView.findViewById(R.id.morecolors);
        }
    }
}



