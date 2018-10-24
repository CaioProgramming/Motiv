package com.creat.motiv;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.creat.motiv.Adapters.ViewPagerAdapter;

public class NewUser extends AppCompatActivity {
    ViewPagerAdapter adapter;
    TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        final Button start = findViewById(R.id.start);
          tabs = findViewById(R.id.tabs);
        ViewPager slides = findViewById(R.id.slides);
        tabs.setupWithViewPager(slides,true);
        adapter = new ViewPagerAdapter(this);
        slides.setAdapter(adapter);
        setupTabIcons();
         start.setVisibility(View.INVISIBLE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comecar();
            }
        });


        slides.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 7 ){
                    start.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 7 ){
                    start.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void setupTabIcons() {
        tabs.setSelectedTabIndicatorColor(Color.TRANSPARENT);

        for (int i = 0; i  < 7; i++){
            tabs.getTabAt(i).setIcon(R.drawable.dot);
        }

    }
    private void Comecar(){
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("novo",true);
        startActivity(i);
        this.finish();

    }
}
