package com.creat.motiv.Fragments;


import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.creat.motiv.Beans.Likes;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Alert;
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



/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private ArrayList<Quotes> likequotes, allquotes;
    private ArrayList<Quotes> myquotes = new ArrayList<>();
    private ArrayList<Likes> likesArrayList;
    private Pref preferences;
    private TextView posts, likes;
    private CircleImageView profilepic;
    private RecyclerView myquotesrecycler;
    private Query quotesdb;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private TextView username;
    private ProfileFragment pfragment = this;

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

        Tutorial();



        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alert pics = new Alert(Objects.requireNonNull(getActivity()));
                pics.Picalert(pfragment);
            }
        });

        Button edit = v.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alert a = new Alert(getActivity());
                a.settings();
            }
        });

        return v;

    }


    private void Tutorial() {
        boolean novo = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getBoolean("novo");

        if (novo){

                Pref preferences = new Pref(Objects.requireNonNull(getContext()));
                if (!preferences.profiletutorialstate()) {
                    preferences.setProfileTutorial(true);
                    Alert a = new Alert(getActivity());
                    a.Message(getActivity().getDrawable(R.drawable.ic_mobile_post_monochrome), getString(R.string.profile_intro));

                }


        }
    }



    private void userinfo() {

        username.setText(user.getDisplayName());

        Glide.with(this).load(user.getPhotoUrl()).error(getActivity().getDrawable(R.drawable.notfound)).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Alert a = new Alert(getActivity());
                a.Nopicture(pfragment);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
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


    public void reload() {
        Carregar();
        userinfo();
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
                Alert a = new Alert(getActivity());
                a.Message(a.erroricon, "Erro " + databaseError.getMessage());
            }
        });



    }

    private void like(final Quotes position) {
        if (getActivity() == null) {
            return;
        }
        //loading.setVisibility(View.VISIBLE);
        likesArrayList = new ArrayList<>();
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
        QuotesDB quotesDB = new QuotesDB(getActivity());
        quotesDB.CarregarUserQuotes(myquotes, myquotesrecycler, posts);
        CarregarLikes();


    }


    private void show() {
        Alert a = new Alert(Objects.requireNonNull(getActivity()));
        a.loading();

    }


}
