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

import com.creat.motiv.Beans.Color;
import com.creat.motiv.R;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerColorAdapter extends RecyclerView.Adapter<RecyclerColorAdapter.MyViewHolder> {
    ArrayList<Color> ColorsList;
    private Context mContext;
    private LinearLayout background;
    private TextView textView,author;
    private ValueEventListener databaseReference;
    private Activity mActivity;
   TextView textcolor,backcolor;

    public RecyclerColorAdapter(ArrayList<Color> colorsList, Context mContext, LinearLayout background, TextView textView, TextView author,
                                  Activity mActivity,TextView texcolor,TextView backcolor) {
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

            final int color = android.graphics.Color.parseColor(""+ColorsList.get(position).getValue()+"");
            System.out.println(color);
        Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.pop_out);

            holder.colorcard.setBackgroundColor(color);
            holder.colorcard.startAnimation(animation);
        holder.colorcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final RealtimeBlurView blurView = mActivity.findViewById(R.id.rootblur);

                String options[]={
                  "Texto",
                  "Plano de fundo"
                };
                final int color = android.graphics.Color.parseColor(ColorsList.get(position).getValue());

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
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        blurView.setBlurRadius(0);
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        blurView.setBlurRadius(0);
                    }
                });

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



