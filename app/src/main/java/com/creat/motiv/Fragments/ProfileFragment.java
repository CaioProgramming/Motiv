package com.creat.motiv.Fragments;


import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Adapters.RecyclerPicAdapter;
import com.creat.motiv.Beans.Pics;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

import static android.view.Gravity.START;
import static com.creat.motiv.Database.QuotesDB.path;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    private android.widget.ImageView profilepic;
    private android.widget.TextView postnumber;
    private android.support.v7.widget.RecyclerView myquotesrecycler;
    private RealtimeBlurView blur;
    ArrayList<Quotes> myquotes;
    private Query quotesdb;
    FirebaseUser user;
    ValueEventListener databaseReference;
    QuotesDB quotesDB;
    private de.hdodenhof.circleimageview.CircleImageView userpictwo;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsetoolbar;
    private android.support.design.widget.AppBarLayout appBarLayout;
    private android.widget.LinearLayout linearLayout;
    private android.support.design.widget.FloatingActionButton floatingActionButton;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         blur = Objects.requireNonNull(getActivity()).findViewById(R.id.rootblur);
        user = FirebaseAuth.getInstance().getCurrentUser();
        quotesdb = FirebaseDatabase.getInstance().getReference();


        View view = inflater.inflate(R.layout.fragment_profile, container, false);
         appBarLayout = (AppBarLayout) view.findViewById(R.id.appBarLayout);
        this.collapsetoolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsetoolbar);
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        userpictwo = view.findViewById(R.id.userpictwo);
        postnumber = view.findViewById(R.id.postnumber);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        myquotesrecycler = view.findViewById(R.id.myquotes);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        profilepic = view.findViewById(R.id.profilepic);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEdit();
            }
        });


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    Animation out = AnimationUtils.loadAnimation(getContext(),R.anim.pop_out);
                    userpictwo.startAnimation(out);
                    postnumber.startAnimation(out);
                    userpictwo.setVisibility(View.INVISIBLE);
                    postnumber.setVisibility(View.INVISIBLE);

                    isShow = true;
                } else if(isShow) {
                    Animation in = AnimationUtils.loadAnimation(getContext(),R.anim.pop_in);
                    userpictwo.setVisibility(View.VISIBLE);
                    postnumber.setVisibility(View.VISIBLE);
                    postnumber.startAnimation(in);
                    userpictwo.startAnimation(in);//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        Carregar();

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(user.getDisplayName());
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        }

        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsetoolbar);
        collapsingToolbarLayout.setTitle(user.getDisplayName());
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleGravity(START);

        // Set collapsing tool bar image.


        userpictwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picalert();
            }
        });

        userinfo();
        return view;

    }

    private void userinfo() {
        Glide.with(this).load(user.getPhotoUrl()).into(profilepic);
        Glide.with(this).load(user.getPhotoUrl()).into(userpictwo);


    }

    private void userEdit() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
        bottomSheetDialog.setContentView(R.layout.profile_edit);
        final EditText username;
        AppCompatButton save;
        CircleImageView userpic;
        save = bottomSheetDialog.findViewById(R.id.save);
        username = bottomSheetDialog.findViewById(R.id.username);
        userpic = bottomSheetDialog.findViewById(R.id.userpic);
        if (username != null) {
            username.setHint(user.getDisplayName());
        }
        if (userpic != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(userpic);
        }

        if (save != null) {
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (username != null && !username.getText().toString().equals("")) {
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username.getText().toString()).build();
                        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).setText("Nome alterado, " + user.getDisplayName() + " que nome bonito hein!").
                                            success().show();
                                    bottomSheetDialog.dismiss();
                                } else {
                                    Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).setText("putz deu esse erro aqui " + Objects.requireNonNull(task.getException()).getMessage())
                                            .error().show();


                                }
                            }
                        });


                    } else {
                        Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).setText("Não pode salvar o nome vazio, isso aqui não é festa").error().show();
                    }

                }
            });
        }
        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                blur.setBlurRadius(0);
            }
        });
        blur.setBlurRadius(15);

    }


    private void Picalert() {
        final ArrayList<Pics> Picslist;
        final RealtimeBlurView blurView = Objects.requireNonNull(getActivity()).findViewById(R.id.rootblur);
        Picslist = new ArrayList<>();
        final BottomSheetDialog myDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
        myDialog.setTitle("Escolha uma foto");
        myDialog.setContentView(R.layout.profilepicselect);
        final RecyclerView picrecycler;
        picrecycler = myDialog.findViewById(R.id.picsrecycler);

        databaseReference = FirebaseDatabase.getInstance().getReference("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Pics pic = postSnapshot.getValue(Pics.class);
                    Pics p = new Pics();
                    if (pic != null) {
                        p.setUri(pic.getUri());
                    }
                    Picslist.add(p);
                    System.out.println(Picslist.size());

                }

                Objects.requireNonNull(picrecycler).setHasFixedSize(true);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
                RecyclerPicAdapter recyclerPicAdapter = new RecyclerPicAdapter(quotesDB, getContext(), Picslist,
                        blurView, getActivity(), myDialog);
                picrecycler.setAdapter(recyclerPicAdapter);
                picrecycler.setLayoutManager(llm);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myDialog.show();
        blurView.setBlurRadius(40);
        blurView.setOverlayColor(R.color.lwhite);

        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                blurView.setBlurRadius(0);
                blurView.setOverlayColor(Color.TRANSPARENT);
                userinfo();

            }
        });


    }




    private void Carregar() {
        myquotes = new ArrayList<>();
        myquotes.clear();


        quotesdb.keepSynced(true);


        quotesdb = FirebaseDatabase.getInstance().getReference().child(path);
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

                        quotes.setUserphoto(q.getUserphoto());
                        quotes.setUsername(q.getUsername());
                        quotes.setCategoria(q.getCategoria());
                        quotes.setData(q.getData());
                        quotes.setLikes(q.getLikes());

                        if (q.getTextcolor() == 0|| q.getBackgroundcolor() == 0){
                            quotes.setTextcolor(Color.BLACK);
                            quotes.setBackgroundcolor(Color.WHITE);
                        }else{
                            quotes.setTextcolor(q.getTextcolor());
                            quotes.setBackgroundcolor(q.getBackgroundcolor());}
                            if (q.getUserID().equals(user.getUid())){
                            quotes.setUsername(user.getDisplayName());
                            quotes.setUserphoto(String.valueOf(user.getPhotoUrl()));
                        myquotes.add(quotes);
                        System.out.println(i);

                        System.out.println("Quotes " + myquotes.size());}

                    }
                }

                Collections.reverse(myquotes);
                myquotesrecycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                myquotesrecycler.setHasFixedSize(true);
                System.out.println(myquotes.size());
                final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.transition);
                RecyclerAdapter myadapter = new RecyclerAdapter(quotesDB, getContext(), myquotes, blur, getActivity());
                myquotesrecycler.setAdapter(myadapter);
                myquotesrecycler.setLayoutManager(llm);
                myquotesrecycler.startAnimation(myanim2);
                final ValueAnimator animator = ValueAnimator.ofInt(0, myquotes.size());
                animator.setDuration(2500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        postnumber.setText(valueAnimator.getAnimatedValue().toString());
                    }
                });
                animator.start();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



}
