package com.creat.motiv.Fragments;


import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.ProgressBar;

import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    private ProgressBar progress;
    private android.support.v4.widget.SwipeRefreshLayout swiperefresh;
    private android.widget.ScrollView scroll;
    private android.widget.TextView quote;
    private android.widget.TextView author;
    private android.support.v7.widget.Toolbar toolbar;
    private android.support.design.widget.CollapsingToolbarLayout collapsetoolbar;
    private android.support.design.widget.AppBarLayout appbarlayout;
    public HomeFragment() {
        // Required empty public constructor
    }


    RealtimeBlurView blur;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
         appbarlayout = view.findViewById(R.id.appbarlayout);
         collapsetoolbar = view.findViewById(R.id.collapsetoolbar);
         toolbar = view.findViewById(R.id.toolbar);
         author = view.findViewById(R.id.author);
         quote = view.findViewById(R.id.quote);
        blur = Objects.requireNonNull(getActivity()).findViewById(R.id.rootblur);

         scroll = view.findViewById(R.id.scroll);
        composesrecycler = view.findViewById(R.id.composesrecycler);
          quotesdb = FirebaseDatabase.getInstance().getReference();


        Carregar();






        return view;


    }

    private void Carregar() {
        quotesArrayList = new ArrayList<>();
        quotesArrayList.clear();

        quotesdb.keepSynced(true);


        quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("data");
        quotesdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes quotes = new Quotes();
                    i++;
                    Quotes q = d.getValue(Quotes.class);
                    if (q != null) {
                        quotes.setId(d.getKey());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setUserID(q.getUserID());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                        quotes.setLikes(q.getLikes());
                        if (q.getTextcolor() == 0|| q.getBackgroundcolor() == 0){
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        }else{
                        quotes.setTextcolor(q.getTextcolor());
                        quotes.setBackgroundcolor(q.getBackgroundcolor());}
                        quotesArrayList.add(quotes);
                        System.out.println(i);
                        if (i > 4){
                            Quotes adquote = new Quotes();
                            adquote.setQuote("Ad");
                            quotesArrayList.add(adquote);
                        }
                        if (i == 4){
                            i = 0;
                        }
                        System.out.println("Quotes " + quotesArrayList.size());

                    }
                }
                Collections.reverse(quotesArrayList);
                composesrecycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                composesrecycler.setHasFixedSize(true);
                System.out.println(quotesArrayList.size());
                final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.transition);
                RecyclerAdapter myadapter = new RecyclerAdapter(quotesDB,getContext(), quotesArrayList, blur);
                composesrecycler.setAdapter(myadapter);
                composesrecycler.setLayoutManager(llm);
                composesrecycler.startAnimation(myanim2);


                ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
                if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);



                }

                Random r = new Random();
                int q = r.nextInt(quotesArrayList.size());

                quote.setText(quotesArrayList.get(q).getQuote());
                author.setText(quotesArrayList.get(q).getAuthor());



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
