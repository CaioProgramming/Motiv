package com.creat.motiv.Fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wooplr.spotlight.SpotlightView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import static com.creat.motiv.Database.QuotesDB.path;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private RecyclerView composesrecycler;
    private Query quotesdb;

    ArrayList<Quotes> quotesArrayList;
    private android.widget.TextView quote;
    private android.widget.TextView author;
    private android.support.v7.widget.Toolbar toolbar;
    private android.support.design.widget.CollapsingToolbarLayout collapsetoolbar;
    FirebaseUser user;
    Boolean novo;


    private android.widget.LinearLayout loading;

    public HomeFragment() {
        // Required empty public constructor
    }

    Pref preferences;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        preferences = new Pref(Objects.requireNonNull(getContext()));
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        android.widget.ImageView offlineimage = view.findViewById(R.id.offlineimage);
        this.loading = view.findViewById(R.id.loading);

        AdView adView = view.findViewById(R.id.adView);
        Glide.with(this).asBitmap()
                .load("https://cdn.dribbble.com/users/4852/screenshots/4949739/monstermind.jpg").into(offlineimage);
        MobileAds.initialize(getContext(),
                "ca-app-pub-3940256099942544/1033173712");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        novo = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getBoolean("novo");

        collapsetoolbar = view.findViewById(R.id.collapsetoolbar);
        toolbar = view.findViewById(R.id.toolbar);
        author = view.findViewById(R.id.author);
        quote = view.findViewById(R.id.quote);

        composesrecycler = view.findViewById(R.id.composesrecycler);
        quotesdb = FirebaseDatabase.getInstance().getReference();
        collapsetoolbar.setCollapsedTitleTextColor(Color.WHITE);
        tutorial();


        //Carregar();

        if (preferences.nightmodestate()) {

            collapsetoolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimaryDark));
            collapsetoolbar.setCollapsedTitleTextColor(Color.WHITE); }

        if (novo) {
            tutorial();
        }
        Carregar();
        show();
        return view;


    }


    private void tutorial() {

        if (novo) {
             Spotlight();


        }
        novo = false;
    }




    private void Spotlight() {
        new SpotlightView.Builder(Objects.requireNonNull(getActivity()))
                .introAnimationDuration(400)
                .lineAndArcColor(Color.WHITE)
                .headingTvColor(Color.WHITE)
                .subHeadingTvColor(Color.WHITE)
                .enableRevealAnimation(true)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvText("Tela inicial")
                .subHeadingTvSize(16)
                .subHeadingTvText(getString(R.string.home_intro))
                .maskColor(getContext().getResources().getColor(R.color.lblack))
                .target(composesrecycler)
                .lineAnimDuration(400)
                .headingTvSize(28)
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .usageId("homescreen") //UNIQUE ID
                .show();
    }

    private void Carregar() {
        if (getActivity() == null || getContext() == null){
            return;
        }

        quotesdb.keepSynced(true);



        quotesdb = FirebaseDatabase.getInstance().getReference().child(path);
        if (this.isAdded()) {
        quotesdb.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quotesArrayList = new ArrayList<>();
                quotesArrayList.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes quotes = new Quotes();
                    Quotes q = d.getValue(Quotes.class);
                    if (q != null) {
                        quotes.setId(q.getId());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setUserID(q.getUserID());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                         quotes.setUsername(q.getUsername());
                        quotes.setUserphoto(q.getUserphoto());
                        quotes.setReport(q.isReport());
                        if (q.getFont() != null){

                                quotes.setFont(q.getFont());
                        }else{
                            quotes.setFont(null);
                        }


                        if (q.getTextcolor() == 0|| q.getBackgroundcolor() == 0){
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        }else{
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());}
                        quotesArrayList.add(quotes);

                        System.out.println("Quotes " + quotesArrayList.size());
                        System.out.println("Quote  " + quotes.getId());

                    }
                }
                Collections.reverse(quotesArrayList);
                composesrecycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), Tools.spancount, GridLayoutManager.VERTICAL, false);
                composesrecycler.setHasFixedSize(true);
                System.out.println(quotesArrayList.size());
                if (getContext() == null){
                    return;
                }
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotesArrayList, getActivity(),composesrecycler);
                composesrecycler.setAdapter(myadapter);
                composesrecycler.setLayoutManager(llm);
                if (getActivity() != null) {
                    ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
                    if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(getString(R.string.app_name));
                    }
                }

                Random r = new Random();
                int q = r.nextInt(quotesArrayList.size());

                quote.setText(quotesArrayList.get(q).getQuote());
                if (quotesArrayList.get(q).getFont() != null){
                    if (getActivity() == null){
                        quote.setText(R.string.atuazlizando);

                        return;
                    }
                    quote.setTypeface(Tools.fonts(getActivity()).get(quotesArrayList.get(q).getFont()));
                    author.setTypeface(Tools.fonts(getActivity()).get(quotesArrayList.get(q).getFont()));
                }else{
                    quote.setTypeface(Typeface.DEFAULT);
                }

                if (quotesArrayList.get(q).isBold()){
                    quote.setTypeface(quote.getTypeface(), Typeface.BOLD);
                    author.setTypeface(quote.getTypeface(),Typeface.BOLD);
                }else{
                    quote.setTypeface(quote.getTypeface(),Typeface.NORMAL);
                    author.setTypeface(quote.getTypeface(),Typeface.NORMAL);
                }

                if (quotesArrayList.get(q).isItalic()){
                    quote.setTypeface(quote.getTypeface(),Typeface.ITALIC);
                    author.setTypeface(quote.getTypeface(),Typeface.ITALIC);

                }else {
                    quote.setTypeface(quote.getTypeface(),Typeface.NORMAL);
                    author.setTypeface(quote.getTypeface(),Typeface.NORMAL);
                }

                if (quotesArrayList.get(q).isItalic() && quotesArrayList.get(q).isBold()){
                    quote.setTypeface(quote.getTypeface(),Typeface.BOLD_ITALIC);
                    author.setTypeface(quote.getTypeface(),Typeface.BOLD_ITALIC);
                }else{
                    quote.setTypeface(quote.getTypeface(),Typeface.NORMAL);
                    author.setTypeface(quote.getTypeface(),Typeface.NORMAL);
                }

                author.setText(quotesArrayList.get(q).getAuthor());
                quote.setTextColor(quotesArrayList.get(q).getTextcolor());
                author.setTextColor(quotesArrayList.get(q).getTextcolor());
                collapsetoolbar.setExpandedTitleColor(quotesArrayList.get(q).getTextcolor());
                collapsetoolbar.setBackgroundColor(quotesArrayList.get(q).getBackgroundcolor());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
        }


    }

    private void show() {

        CountDownTimer timer = new CountDownTimer(9000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (getContext() == null){
                    return;
                }
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_scale_down);
                loading.startAnimation(animation);
                loading.setVisibility(View.GONE);
            }
        };timer.start();
    }


}
