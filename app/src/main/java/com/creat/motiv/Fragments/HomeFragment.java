package com.creat.motiv.Fragments;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.Pref;
import com.creat.motiv.R;
import com.github.mmin18.widget.RealtimeBlurView;
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

    QuotesDB quotesDB;
    ArrayList<Quotes> quotesArrayList;
    private android.widget.TextView quote;
    private android.widget.TextView author;
    private android.support.v7.widget.Toolbar toolbar;
    private android.support.design.widget.CollapsingToolbarLayout collapsetoolbar;
    private android.support.design.widget.AppBarLayout appbarlayout;
    private AdView madView;
    FirebaseUser user;
    private SharedPreferences setings;
    private AdView adView;
    private android.support.design.widget.CoordinatorLayout home;
    public HomeFragment() {
        // Required empty public constructor
    }


    RealtimeBlurView blur;
    Pref preferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        user = FirebaseAuth.getInstance().getCurrentUser();
        preferences = new Pref(getContext());
        View view = inflater.inflate(R.layout.fragment_home, container, false);
         home = view.findViewById(R.id.home);
        setings = PreferenceManager.getDefaultSharedPreferences(getContext());

         madView = view.findViewById(R.id.adView);
        MobileAds.initialize(getContext(),
                "ca-app-pub-4979584089010597/3019321601");

        madView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        madView.loadAd(adRequest);

        Boolean novo = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getBoolean("novo");
        if(novo == null){
            novo = false;
        }
        if (novo) {
            new SpotlightView.Builder(getActivity())
                    .introAnimationDuration(400)
                    .enableRevealAnimation(true)
                    .performClick(true)
                    .fadeinTextDuration(400)
                    .headingTvColor(R.color.colorPrimary)
                    .headingTvSize(32)
                    .headingTvText("Tela inicial")
                    .subHeadingTvColor(Color.parseColor("#ffffff"))
                    .subHeadingTvSize(16)
                    .subHeadingTvText("Esta é sua tela inicial, onde poderá ver todas as postagens ja feitas no motiv")
                    .maskColor(Color.parseColor("#dc000000"))
                    .target(view)
                    .lineAnimDuration(400)
                    .lineAndArcColor(R.color.colorPrimaryDark)
                    .dismissOnTouch(true)
                    .dismissOnBackPress(true)
                    .usageId("homescreen") //UNIQUE ID
                    .show();
        }
        appbarlayout = view.findViewById(R.id.appbarlayout);
        collapsetoolbar = view.findViewById(R.id.collapsetoolbar);
        toolbar = view.findViewById(R.id.toolbar);
        author = view.findViewById(R.id.author);
        quote = view.findViewById(R.id.quote);
        blur = Objects.requireNonNull(getActivity()).findViewById(R.id.rootblur);

        composesrecycler = view.findViewById(R.id.composesrecycler);
        quotesdb = FirebaseDatabase.getInstance().getReference();

        Carregar();

        if (preferences.nightmodestate()) {
            home.setBackgroundResource(R.drawable.gradnight);
            collapsetoolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimaryDark));
            collapsetoolbar.setCollapsedTitleTextColor(Color.WHITE); }



        return view;


    }

    private void Carregar() {

        quotesdb.keepSynced(true);


        quotesdb = FirebaseDatabase.getInstance().getReference().child(path);
        quotesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quotesArrayList = new ArrayList<>();
                quotesArrayList.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes quotes = new Quotes();
                    Quotes q = d.getValue(Quotes.class);
                    if (q != null) {
                        quotes.setId(d.getKey());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setUserID(q.getUserID());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                        quotes.setLikes(q.getLikes());
                        quotes.setUsername(q.getUsername());
                        quotes.setUserphoto(q.getUserphoto());
                        if (q.getTextcolor() == 0|| q.getBackgroundcolor() == 0){
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        }else{
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());}
                        quotesArrayList.add(quotes);
                        if (q.getUserID().equals(user.getUid())){
                            quotes.setUsername(user.getDisplayName());
                            quotes.setUserphoto(String.valueOf(user.getPhotoUrl()));
                        }

                        System.out.println("Quotes " + quotesArrayList.size());

                    }
                }
                Collections.reverse(quotesArrayList);
                composesrecycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                composesrecycler.setHasFixedSize(true);
                System.out.println(quotesArrayList.size());
                RecyclerAdapter myadapter = new RecyclerAdapter(quotesDB, getContext(), quotesArrayList, blur, getActivity());
                composesrecycler.setAdapter(myadapter);
                composesrecycler.setLayoutManager(llm);


                ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
                if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));


                }

                Random r = new Random();
                int q = r.nextInt(quotesArrayList.size());

                quote.setText(quotesArrayList.get(q).getQuote());
                author.setText(quotesArrayList.get(q).getAuthor());
                quote.setTextColor(quotesArrayList.get(q).getTextcolor());
                author.setTextColor(quotesArrayList.get(q).getTextcolor());
                collapsetoolbar.setExpandedTitleColor(quotesArrayList.get(q).getTextcolor());
                collapsetoolbar.setBackgroundColor(quotesArrayList.get(q).getBackgroundcolor());


                quotesArrayList.remove(q);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Carregar();
    }
}
