package com.creat.motiv.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creat.motiv.Adapters.RecyclerArtistsAdapter;
import com.creat.motiv.Adapters.RecyclerReferencesAdapter;
import com.creat.motiv.MainActivity;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Tools;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import de.mateware.snacky.Snacky;

import static com.creat.motiv.Utils.Tools.iconssite;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    private RecyclerView creatorsrecycler;
    private RecyclerView designrecycler;
    private RecyclerView artistsrecycler;
    private Query quotesdb;
    private Toolbar toolbar;
    private android.widget.TextView creatorstitle;
    private android.widget.TextView referencestitle;
    private android.widget.TextView artiststitle;
    private android.widget.TextView suportitle;
    ProgressDialog progressDialog;
    private android.widget.ProgressBar loading;
    private CollapsingToolbarLayout collapsetoolbar;
    private AppBarLayout appbarlayout;
    private android.widget.TextView helpad;
    public AboutFragment() {
        // Required empty public constructor
    }

    private RewardedVideoAd rewardedVideoAd;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        quotesdb = FirebaseDatabase.getInstance().getReference();
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        this.helpad = view.findViewById(R.id.helpad);
        this.appbarlayout = view.findViewById(R.id.appbarlayout);
        this.collapsetoolbar = view.findViewById(R.id.collapsetoolbar);
        this.loading = view.findViewById(R.id.loading);
        this.suportitle = view.findViewById(R.id.suportitle);
        this.artiststitle = view.findViewById(R.id.artiststitle);
        this.referencestitle = view.findViewById(R.id.referencestitle);
        this.creatorstitle = view.findViewById(R.id.creatorstitle);
        TextView helpad = view.findViewById(R.id.helpad);
        this.artistsrecycler = view.findViewById(R.id.artistsrecycler);
        this.designrecycler = view.findViewById(R.id.designrecycler);
        this.creatorsrecycler = view.findViewById(R.id.creatorsrecycler);
        this.toolbar = view.findViewById(R.id.toolbar);
        MobileAds.initialize(getActivity(),
                "ca-app-pub-4979584089010597~4181793255");
        helpad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle("Ajude o motiv a crescer")

                        .setMessage("Veja esse anúncio para que nos ajude a melhorar mais nosso aplicativo")
                        .setPositiveButton("Sim, quero ajudar!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                loadRewardedVideoAd();
                                progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("Carregando");
                                progressDialog.show();

                            }
                        });
                builder.show();


            }
        });
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                progressDialog.dismiss();
                rewardedVideoAd.show();
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).warning().setText("Saindo do aplicativo fica díficil...").show();

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                    Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).error().setText("Deu erro carregando o anúncio, foi mal!").show();
            }

            @Override
            public void onRewardedVideoCompleted() {
                Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).setBackgroundColor(getResources().getColor(R.color.colorPrimary)).setTextColor(Color.WHITE)
                        .setIcon(R.drawable.ic_saturn_and_other_planets).setText("Obrigado pela ajuda, você é demais!").setDuration(10000).build().show();

            }
        });

        CarregarAll();
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.home = true;
                /*getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, new HomeFragment())
                        .commit();*/


            }
        });
        collapsetoolbar.setExpandedTitleTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Nunito-Regular.ttf"));
        collapsetoolbar.setCollapsedTitleTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Nunito-Regular.ttf"));
        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsetoolbar.setTitle("Sobre");
                    collapsetoolbar.setCollapsedTitleTextColor(ColorStateList.valueOf(Color.WHITE));
                    isShow = true;
                } else if (isShow) {
                    collapsetoolbar.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });


        return view;
    }




     private void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd("ca-app-pub-4979584089010597/9410101997",
                new AdRequest.Builder().build());

    }





/*
    private void CarregarCreators() {
        final ArrayList<Developers> developersArrayList = new ArrayList<>();
        quotesdb = FirebaseDatabase.getInstance().getReference().child("Developers");
        quotesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Developers developers = new Developers();
                    Developers dv = d.getValue(Developers.class);

                    if (dv != null) {
                        developers.setNome(dv.getNome());
                        developers.setBackgif(dv.getBackgif());
                        developers.setPhotouri(dv.getPhotouri());
                        developers.setLinkedin(dv.getLinkedin());
                        developers.setCargo(dv.getCargo());

                        System.out.println("Developer " + developers.getNome() + "   " + " cargo " + developers.getCargo() +
                                "  " + "photo " + developers.getPhotouri() + " linkedin " + developers.getLinkedin() +
                                " backgif " + developers.getBackgif()

                        );
                        developersArrayList.add(developers);
                    }

                }
                Collections.shuffle(developersArrayList);
                RecyclerCreatorsAdapter recyclerCreatorsAdapter = new RecyclerCreatorsAdapter(getContext(), developersArrayList, getActivity());
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL
                        , false);
                creatorsrecycler.setAdapter(recyclerCreatorsAdapter);
                creatorsrecycler.setHasFixedSize(true);
                creatorsrecycler.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
*/

    private void CarregarReferences() {
        ArrayList<String> referencias = new ArrayList<>();
        Collections.addAll(referencias, iconssite);
        Collections.sort(referencias, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareToIgnoreCase(t1);
            }
        });
        RecyclerReferencesAdapter recyclerReferencesAdapter = new RecyclerReferencesAdapter(referencias, getActivity());
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);

        designrecycler.setLayoutManager(llm);
        designrecycler.setHasFixedSize(true);
        designrecycler.setAdapter(recyclerReferencesAdapter);

    }

    private void CarregarArtistas() {
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);

        RecyclerArtistsAdapter recyclerArtistsAdapter = new RecyclerArtistsAdapter(getContext(), Tools.artists(), getActivity());
        artistsrecycler.setAdapter(recyclerArtistsAdapter);
        artistsrecycler.setLayoutManager(llm);

    }

    private void CarregarAll() {
        //CarregarCreators();
        CarregarArtistas();
        CarregarReferences();
    }

}
