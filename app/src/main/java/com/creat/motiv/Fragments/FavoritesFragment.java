package com.creat.motiv.Fragments;


import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Beans.Likes;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Alert;
import com.creat.motiv.Utils.Tools;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import de.mateware.snacky.Snacky;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {


    private RecyclerView myquotesrecycler;
    private ProgressBar loading;
    private TextView favcount;
    private ArrayList<Quotes> allquotes;
    private ArrayList<Quotes> likequotes;
    private ArrayList<Likes> likesArrayList = new ArrayList<>();

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        myquotesrecycler = v.findViewById(R.id.composesrecycler);
        loading = v.findViewById(R.id.loading);
        favcount = v.findViewById(R.id.favtext);
        return v;


    }

    private void show() {
        Alert a = new Alert(Objects.requireNonNull(getActivity()));
        a.loading();

    }


    @Override
    public void onResume() {
        CarregarLikes();
        show();
        super.onResume();
    }

    private void CarregarLikes() {
        if (getActivity() == null) {
            return;
        }
        allquotes = new ArrayList<>();
        likequotes = new ArrayList<>();


        Query quotesdb2;
        quotesdb2 = Tools.quotesreference;
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
                Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).error().setText("Erro " + databaseError.getMessage()).show();
            }
        });


    }

    private void like(final Quotes position) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference likedb = Tools.quotesreference;
        likedb.child(position.getId()).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
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
                    if (l != null && l.getUserid().equals(Objects.requireNonNull(user).getUid())) {
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
        RecyclerAdapter myadapter = new RecyclerAdapter(quotes, getActivity());
        myquotesrecycler.setAdapter(myadapter);
        myquotesrecycler.setLayoutManager(llm);
        myquotesrecycler.startAnimation(myanim2);
        ValueAnimator animator = ValueAnimator.ofInt(0, likequotes.size());
        animator.setDuration(2500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                favcount.setText(String.format("%s favoritos", valueAnimator.getAnimatedValue().toString()));
            }
        });
        animator.start();
    }


}