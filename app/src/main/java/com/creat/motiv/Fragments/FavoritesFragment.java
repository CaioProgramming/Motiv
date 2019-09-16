package com.creat.motiv.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Beans.Likes;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import de.mateware.snacky.Snacky;

import static com.creat.motiv.Database.QuotesDB.path;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {


    String arg = "quote";
    ArrayList<Quotes> quotesArrayList;
    FirebaseUser user;
    Boolean novo;
    Query quotesdb;
    Pref preferences;
    private RecyclerView myquotesrecycler;

    private ArrayList<Quotes> allquotes;
    private ArrayList<Quotes> likequotes;
    private ArrayList<Likes> likesArrayList = new ArrayList<>();

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        user = FirebaseAuth.getInstance().getCurrentUser();
        preferences = new Pref(Objects.requireNonNull(getContext()));
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);


        v.findViewById(R.id.home);

        myquotesrecycler = v.findViewById(R.id.composesrecycler);




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        return v;


    }


    @Override
    public void onResume() {
        CarregarLikes();
        super.onResume();
    }

    private void CarregarLikes() {
        if (getActivity() == null) {
            return;
        }
        allquotes = new ArrayList<>();
        likequotes = new ArrayList<>();


        Query quotesdb2;
        quotesdb2 = FirebaseDatabase.getInstance().getReference().child(path);
        quotesdb2.keepSynced(false);
        quotesdb2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likequotes.clear();
                allquotes.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Quotes quotes = new Quotes();
                    Quotes q = d.getValue(Quotes.class);
                    if (q != null) {
                        quotes.setId(d.getKey());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setUserID(q.getUserID());

                        quotes.setUserphoto(q.getUserphoto());
                        quotes.setUsername(q.getUsername());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                        quotes.setReport(q.isReport());
                        if (q.getFont() != null) {
                            quotes.setFont(q.getFont());
                        } else {
                            quotes.setFont(null);
                        }
                        if (q.getTextcolor() == 0 || q.getBackgroundcolor() == 0) {
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        } else {
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());
                        }
                        quotes.setUsername(q.getUsername());
                        quotes.setUserphoto(q.getUserphoto());
                        allquotes.add(quotes);
                        like(quotes);
                        System.out.println("Quotes " + likequotes.size());

                    }
                }


                //Collections.reverse(likequotes);
                //recycler(likequotes);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snacky.builder().setActivity(getActivity()).error().setText("Erro " + databaseError.getMessage()).show();
            }
        });


    }

    private void like(final Quotes position) {
        if (getActivity() == null) {
            return;
        }
        DatabaseReference likedb = FirebaseDatabase.getInstance().getReference();
        likedb.child(path).child(position.getId()).child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likesArrayList.clear();

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Likes l = d.getValue(Likes.class);
                    Likes likes = null;
                    if (l != null) {
                        likes = new Likes(l.getUserid(), l.getUsername(), l.getUserpic());
                    }
                    likesArrayList.add(likes);
                    if (l != null && l.getUserid().equals(user.getUid())) {
                        likequotes.add(position);
                    }

                }
                Collections.reverse(likequotes);
                recycler(likequotes);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void recycler(ArrayList<Quotes> quotes) {
        // Collections.reverse(quotes);
        myquotesrecycler.setVisibility(View.VISIBLE);
        GridLayoutManager llm = new GridLayoutManager(getActivity(), Tools.spancount, GridLayoutManager.VERTICAL, false);
        myquotesrecycler.setHasFixedSize(true);
        System.out.println(quotes);
        final Animation myanim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_scale_up);
        RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotes, getActivity());
        myquotesrecycler.setAdapter(myadapter);
        myquotesrecycler.setLayoutManager(llm);
        myquotesrecycler.startAnimation(myanim2);
    }


}