package com.creat.motiv.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.Tutorial;
import com.creat.motiv.R;
import com.huxq17.swipecardsview.BaseCardAdapter;

import java.util.ArrayList;

public class TutorialAdapter extends BaseCardAdapter{
    private ArrayList<Tutorial> tutorials;
    private Context context;

    public TutorialAdapter(ArrayList<Tutorial> tutorials, Context context) {
        this.tutorials = tutorials;
        this.context = context;
    }

    @Override
    public int getCount() {

        System.out.println("tutoriais " + tutorials.size());
        return tutorials.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.tutorial_card;
    }

    @Override
    public void onBindData(int position, View cardview) {
        Animation in = AnimationUtils.loadAnimation(context,R.anim.pop_in);

        CardView cardView = (CardView) cardview.findViewById(R.id.tutorialcardview);
        cardView.startAnimation(in);
        ImageView tutoimage = (ImageView) cardview.findViewById(R.id.tutorialimage);
        TextView textuto = (TextView) cardview.findViewById(R.id.tutorialtext);
        Tutorial tuto = tutorials.get(position);

        Glide.with(context).load(tuto.getImageuri()).into(tutoimage);
        textuto.setText(tuto.getStep());




    }
}
