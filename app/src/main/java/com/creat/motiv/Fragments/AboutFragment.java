package com.creat.motiv.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.creat.motiv.Adapters.RecyclerArtistsAdapter;
import com.creat.motiv.Adapters.RecyclerCreatorsAdapter;
import com.creat.motiv.Adapters.RecyclerReferencesAdapter;
import com.creat.motiv.Beans.Developers;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.mateware.snacky.Snacky;

import static com.creat.motiv.Utils.Tools.iconssite;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    private android.widget.CheckBox creators;
    private android.support.v7.widget.RecyclerView creatorsrecycler;
    private android.widget.CheckBox designreferences;
    private android.support.v7.widget.RecyclerView designrecycler;
    private android.widget.CheckBox artistsreferences;
    private android.support.v7.widget.RecyclerView artistsrecycler;
    private android.widget.CheckBox support;
    private android.widget.TextView helpad;
    private Query quotesdb;
    private android.widget.LinearLayout creatorlayout;
    private android.widget.LinearLayout referenceslayout;
    private android.widget.LinearLayout artistslayout;
    private android.widget.LinearLayout supportlayout;

    public AboutFragment() {
        // Required empty public constructor
    }

    private InterstitialAd mInterstitialAd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        quotesdb = FirebaseDatabase.getInstance().getReference();
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        this.supportlayout = (LinearLayout) view.findViewById(R.id.supportlayout);
        this.artistslayout = (LinearLayout) view.findViewById(R.id.artistslayout);
        this.referenceslayout = (LinearLayout) view.findViewById(R.id.referenceslayout);
        this.creatorlayout = (LinearLayout) view.findViewById(R.id.creatorlayout);
        this.helpad = view.findViewById(R.id.helpad);
        this.support = view.findViewById(R.id.support);
        this.artistsrecycler = view.findViewById(R.id.artistsrecycler);
        this.artistsreferences = view.findViewById(R.id.artistsreferences);
        this.designrecycler = view.findViewById(R.id.designrecycler);
        this.designreferences = view.findViewById(R.id.designreferences);
        this.creatorsrecycler = view.findViewById(R.id.creatorsrecycler);
        this.creators = view.findViewById(R.id.creators);
        MobileAds.initialize(getActivity(),
                "ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                Snacky.builder().setActivity(getActivity()).setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                        .setText("Obrigado pela ajuda! Você é demais").setTextColor(Color.WHITE).build().show();
            }
        });


        CarregarAll();

        expandlisteners();
        Theme(view);

        return view;
    }

    private void expandlisteners() {
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (support.isChecked()) {
                    helpad.setVisibility(View.GONE);
                } else {
                    helpad.setVisibility(View.VISIBLE);
                }
            }
        });

        artistsreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expand(artistsrecycler, artistsreferences);
            }
        });

        creators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expand(creatorsrecycler, creators);
            }
        });


        designreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expand(designrecycler, designreferences);
            }
        });

        helpad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle("Ajude o motiv a crescer")

                        .setMessage("Veja esse anúncio para que nos ajude a melhorar mais nosso aplicativo")
                        .setPositiveButton("Sim, quero ajudar!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                loadAd();
                            }
                        });
                builder.show();


            }
        });
    }

    private void Theme(View view) {
        preferences = new Pref(getContext());
        int white = Color.WHITE;
        if (preferences.nightmodestate()) {
            view.setBackgroundResource(R.drawable.gradnight);
            artistsreferences.setTextColor(white);
            artistsreferences.setButtonTintList(ColorStateList.valueOf(white));
            artistslayout.setBackgroundColor(getResources().getColor(R.color.grey_900));


            creators.setTextColor(white);
            creators.setButtonTintList(ColorStateList.valueOf(white));
            creatorlayout.setBackgroundColor(getResources().getColor(R.color.grey_900));
            helpad.setTextColor(white);
            support.setTextColor(white);
            supportlayout.setBackgroundColor(getResources().getColor(R.color.grey_900));
            support.setButtonTintList(ColorStateList.valueOf(white));


            designreferences.setTextColor(white);
            referenceslayout.setBackgroundColor(getResources().getColor(R.color.grey_900));
            designreferences.setButtonTintList(ColorStateList.valueOf(white));


        }
    }

    private void loadAd() {
        Snacky.builder().setText("Eba! Obrigado pela ajuda, seu anúncio está carregando").
                setTextColor(getResources().getColor(R.color.white)).setActivity(getActivity()).
                setBackgroundColor(android.graphics.Color.BLACK).setDuration(5000).build().show();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Snacky.builder().setText("Parece que está demorando muito para carregar... ").
                    setTextColor(getResources().getColor(R.color.white)).setActivity(getActivity()).
                    setBackgroundColor(Color.LTGRAY).setDuration(5000).build().show();
        }

    }

    Pref preferences;


    private void expand(RecyclerView layout, CheckBox checkBox) {
        if (checkBox.isChecked()) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
        }


    }

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
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false);
                creatorsrecycler.setAdapter(recyclerCreatorsAdapter);
                creatorsrecycler.setHasFixedSize(true);
                creatorsrecycler.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CarregarReferences() {
        ArrayList<String> referencias = new ArrayList<>();
        Collections.addAll(referencias, iconssite);
        Collections.sort(referencias, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareToIgnoreCase(t1);
            }
        });
        RecyclerReferencesAdapter recyclerReferencesAdapter = new RecyclerReferencesAdapter(getContext(), referencias, getActivity());
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);

        designrecycler.setLayoutManager(llm);
        designrecycler.setHasFixedSize(true);
        designrecycler.setAdapter(recyclerReferencesAdapter);

    }

    private void CarregarArtistas() {
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);

        RecyclerArtistsAdapter recyclerArtistsAdapter = new RecyclerArtistsAdapter(getContext(), Tools.artists(), getActivity());
        artistsrecycler.setAdapter(recyclerArtistsAdapter);
        artistsrecycler.setLayoutManager(llm);

    }

    private void CarregarAll() {
        CarregarCreators();
        CarregarArtistas();
        CarregarReferences();
    }

}
