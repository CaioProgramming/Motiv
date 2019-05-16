package com.creat.motiv.Adapters;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.creat.motiv.R;

import java.util.ArrayList;

public class RecyclerColorAdapter extends RecyclerView.Adapter<RecyclerColorAdapter.MyViewHolder> {
    private ArrayList<Integer> ColorsList;
    private Context mContext;
    private LinearLayout background;
    private TextView textView,author;
    private Activity mActivity;
   private TextView textcolor,backcolor;
   ImageButton textbutton,backgroundbutton;

    public RecyclerColorAdapter(ArrayList<Integer> colorsList, Context mContext, LinearLayout background, TextView textView, TextView author,
                                Activity mActivity, TextView texcolor, TextView backcolor,ImageButton textbutton,ImageButton backgroundbutton) {
        ColorsList = colorsList;
        this.mContext = mContext;
        this.background = background;
        this.textView = textView;
        this.mActivity = mActivity;
        this.author = author;
        this.textcolor = texcolor;
        this.backcolor = backcolor;
        this.textbutton = textbutton;
        this.backgroundbutton = backgroundbutton;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.color_card,parent,false);

        return new RecyclerColorAdapter.MyViewHolder(view);
    }





    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

             System.out.println(ColorsList.get(position));
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.pop_in);

        holder.colorcard.setBackgroundTintList(ColorStateList.valueOf(ColorsList.get(position)));
            holder.colorcard.startAnimation(animation);
        holder.colorcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                                    textbutton.setImageTintList(ColorStateList.valueOf(color));
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
                                    backgroundbutton.setImageTintList(ColorStateList.valueOf(color));

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

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView colorcard;

        MyViewHolder(View itemView) {
            super(itemView);
            colorcard = itemView.findViewById(R.id.colorcard);
        }
    }
}



