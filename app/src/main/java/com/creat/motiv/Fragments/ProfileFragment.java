package com.creat.motiv.Fragments;


import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Beans.Likes;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Alert;
import com.creat.motiv.Utils.Info;
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

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

import static com.creat.motiv.Database.QuotesDB.path;
import static com.creat.motiv.Database.QuotesDB.searcharg;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    ArrayList<Quotes> likequotes, allquotes;
    ArrayList<Quotes> myquotes = new ArrayList<>();
    ArrayList<Likes> likesArrayList;
    ValueEventListener databaseReference;
    QuotesDB quotesDB;
    Pref preferences;
    TextView posts, likes;
    private CircleImageView profilepic;
    private android.support.v7.widget.RecyclerView myquotesrecycler;
    private Query quotesdb;
    ProgressBar loading;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private TextView username;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getContext() == null){
            return null;
        }
        preferences = new Pref(Objects.requireNonNull(getContext()));
        user = FirebaseAuth.getInstance().getCurrentUser();
        quotesdb = FirebaseDatabase.getInstance().getReference();


        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        this.username = v.findViewById(R.id.username);

        this.likes = v.findViewById(R.id.likes);
        this.posts = v.findViewById(R.id.posts);
        myquotesrecycler = v.findViewById(R.id.myquotesrecycler);
        profilepic = v.findViewById(R.id.profilepic);
        loading = v.findViewById(R.id.loading);

        Tutorial();



        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alert pics = new Alert(getActivity());
                pics.Picalert(myquotes);
            }
        });

        CircleImageView rootpic = Objects.requireNonNull(getActivity()).findViewById(R.id.profilepic);


        return v;

    }


    private void Tutorial() {
        boolean novo = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getBoolean("novo");

        if (novo){
            if (myquotes.size() > 0) {
                if (!preferences.profiletutorialstate()) {
                    preferences.setProfileTutorial(true);
                    Info.tutorial("Você de novo" + user.getDisplayName(), getActivity());
                }

            } else {
                Pref preferences = new Pref(Objects.requireNonNull(getContext()));
                if (!preferences.profiletutorialstate()) {
                    preferences.setProfileTutorial(true);
                    Info.tutorial(getString(R.string.profile_intro), getActivity());
                }
            }

        }
    }



    private void userinfo() {

        username.setText(user.getDisplayName());

        Glide.with(this).load(user.getPhotoUrl()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Glide.with(Objects.requireNonNull(getActivity())).load(Tools.userpicnotfound).into(profilepic);
                Snacky.builder().setActivity(getActivity()).warning()
                        .setText("Não conseguimos encontrar sua foto de perfil no nosso servidor, vamos ficar te devendo")
                        .show();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                profilepic.setImageDrawable(resource);
                return false;
            }
        }).into(profilepic);


    }





    @Override
    public void onResume() {
        Carregar();
        userinfo();
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
                        if (q.getTextcolor() == 0|| q.getBackgroundcolor() == 0){
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        }else{
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());}
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
        //loading.setVisibility(View.VISIBLE);
        likesArrayList = new ArrayList<>();
        DatabaseReference likedb = FirebaseDatabase.getInstance().getReference();
        likedb.child(path).child(position.getId()).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
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

                ValueAnimator animator = ValueAnimator.ofInt(0, likequotes.size());
                animator.setDuration(2500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {

                        likes.setText(String.format("%s favoritos", valueAnimator.getAnimatedValue().toString()));
                    }
                });
                animator.start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void Carregar() {
        quotesdb.keepSynced(true);


        quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("userID")
                .startAt(user.getUid())
                .endAt(user.getUid() + searcharg);
        quotesdb.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myquotes = new ArrayList<>();
                myquotes.clear();
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
                        if (q.getTextcolor() == 0|| q.getBackgroundcolor() == 0){
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        }else{
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());}
                        if (q.getUserID().equals(user.getUid())) {
                            quotes.setUsername(q.getUsername());
                            quotes.setUserphoto(q.getUserphoto());
                            myquotes.add(quotes);

                            System.out.println("Quotes " + myquotes.size());
                        }

                    }
                }
                if (getActivity() == null) {
                    return;
                }
                Collections.reverse(myquotes);
                ValueAnimator animator = ValueAnimator.ofInt(0, myquotes.size());
                animator.setDuration(2500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {

                        posts.setText(valueAnimator.getAnimatedValue().toString() + " frases");
                    }
                });
                animator.start();
                recycler(myquotes);
                CarregarLikes();


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

    private void show() {
        Alert a = new Alert(getActivity());
        a.loading();

    }


}
