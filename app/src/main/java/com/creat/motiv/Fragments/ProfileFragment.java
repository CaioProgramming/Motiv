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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Adapters.RecyclerPicAdapter;
import com.creat.motiv.Beans.Pics;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Beans.Tutorial;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Pref;
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
import com.wooplr.spotlight.SpotlightView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import de.mateware.snacky.Snacky;

import static android.view.Gravity.START;
import static com.creat.motiv.Database.QuotesDB.path;
import static com.creat.motiv.Database.QuotesDB.searcharg;


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
    CollapsingToolbarLayout collapsingToolbarLayout;
    private android.support.design.widget.AppBarLayout appBarLayout;
    private boolean tuto = true;
    ArrayList<Tutorial> tutorialArrayList = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    Pref preferences;
    private CollapsingToolbarLayout collapsetoolbar;
    private android.widget.LinearLayout linearLayout;
    private android.widget.LinearLayout postlayout;
    private FloatingActionButton floatingActionButtoncamera;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        preferences = new Pref(getContext());
         blur = Objects.requireNonNull(getActivity()).findViewById(R.id.rootblur);
        user = FirebaseAuth.getInstance().getCurrentUser();
        quotesdb = FirebaseDatabase.getInstance().getReference();


        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        this.floatingActionButtoncamera = (FloatingActionButton) view.findViewById(R.id.floatingActionButtoncamera);
        this.linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        this.postlayout = view.findViewById(R.id.postlayout);
        this.collapsetoolbar = view.findViewById(R.id.collapsetoolbar);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        toolbar = view.findViewById(R.id.toolbar);
        userpictwo = view.findViewById(R.id.userpictwo);
        postnumber = view.findViewById(R.id.postnumber);
        collapsingToolbarLayout = view.findViewById(R.id.collapsetoolbar);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        myquotesrecycler = view.findViewById(R.id.myquotes);
        profilepic = view.findViewById(R.id.profilepic);
        floatingActionButtoncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picalert();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEdit();
            }
        });
        Tutorial(view);
        nightmode(view);
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

                    userpictwo.setVisibility(View.GONE);


                    isShow = true;
                } else if(isShow) {
                    Animation in = AnimationUtils.loadAnimation(getContext(),R.anim.pop_in);
                    userpictwo.setVisibility(View.VISIBLE);

                    userpictwo.startAnimation(in);//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
        Carregar();
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(user.getDisplayName());
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);


        }

        Toolbarconfig(view);

        // Set collapsing tool bar image.

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Tutorial(view);
                return false;
            }
        });

        userpictwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picalert();
            }
        });
        userinfo();
        return view;

    }

    private void Toolbarconfig(View view) {
        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsetoolbar);
        collapsingToolbarLayout.setTitle(user.getDisplayName());
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleGravity(START);
    }

    private void Tutorial(View view) {
        Boolean novo = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getBoolean("novo");

        if (novo){
            Spotlight(view);
        }
        novo = false;
    }

    private void Spotlight(View view) {
        new SpotlightView.Builder(getActivity())
                .introAnimationDuration(400)
                .enableRevealAnimation(true)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvColor(R.color.colorPrimary)
                .headingTvSize(32)
                .headingTvText("Perfil")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(16)
                .subHeadingTvText("Aqui é o seu perfil, onde poderá ver todas suas postagens e se quiser apagá-las,alterar seu nome e foto de perfil")
                .maskColor(Color.parseColor("#dc000000"))
                .target(view)
                .lineAnimDuration(400)
                .lineAndArcColor(R.color.colorPrimaryDark)
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .usageId("createscreen") //UNIQUE ID
                .show();
    }

    private void nightmode(View view) {
        System.out.println(preferences.nightmodestate());
        if (preferences.nightmodestate()){
            view.setBackgroundResource(R.drawable.gradnight);
            collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.grey_800));
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
            floatingActionButton.setBackgroundColor(getResources().getColor(R.color.grey_800));
            postnumber.setTextColor(Color.WHITE);
             floatingActionButton.setBackgroundResource(R.color.grey_900);
              }
    }

    private void userinfo() {
        if (user.getPhotoUrl() == null){
            Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/fantastic_planet_001.jpg?alt=media&token=03f77356-b17a-45f4-ac31-baf0bc9f87f5").into(profilepic);
            Glide.with(this).asGif().load("https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/luna3.gif?alt=media&token=ad8ed0f1-f115-41dc-89ca-b4d424156ea4").into(userpictwo);
        } else {
        Glide.with(this).load(user.getPhotoUrl()).into(profilepic);
        Glide.with(this).load(user.getPhotoUrl()).into(userpictwo);
        }


    }

    private void userEdit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setTitle("Mudar o nome de usuário");
        final EditText username = new EditText(getContext());
        username.setHint(user.getDisplayName());
        builder.setView(username);
        builder.setPositiveButton("Sim, quero mudar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!Objects.equals(username.getText().toString(), "")) {
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username.getText().toString()).build();
                    user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                for (int i = 0; i < myquotes.size(); i++) {
                                    quotesDB = new QuotesDB();
                                    quotesDB.AlterarNome(getActivity(), myquotes.get(i).getId());

                                }
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

        builder.setNegativeButton("Não quero mais mudar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();


    }


    private void Picalert() {
        final ArrayList<Pics> Picslist;
        final RealtimeBlurView blurView = Objects.requireNonNull(getActivity()).findViewById(R.id.rootblur);
        Picslist = new ArrayList<>();
        final BottomSheetDialog myDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
        myDialog.setContentView(R.layout.profilepicselect_);
        final RecyclerView picrecycler;
        final TextView remove;
        remove = myDialog.findViewById(R.id.removepic);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(null).build();
                if (user != null) {
                    user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                for (int i = 0; i < myquotes.size(); i++) {
                                    quotesDB = new QuotesDB();
                                    quotesDB.AlterarFoto(getActivity(), myquotes.get(i).getId(), String.valueOf(user.getPhotoUrl()));
                                }
                                Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).success().setText("Foto de perfil alterada").show();
                                myDialog.dismiss();
                                myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.mi_fade_out);
                                        blurView.startAnimation(out);
                                        blurView.setOverlayColor(Color.TRANSPARENT);
                                        blurView.setBlurRadius(0);
                                    }
                                });

                            } else {
                                Snacky.builder().setActivity(getActivity()).success().setText("Erro " + task.getException()).show();
                            }


                        }
                    });


                }
            }
        });


        picrecycler = myDialog.findViewById(R.id.picsrecycler);

        databaseReference = FirebaseDatabase.getInstance().getReference("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picslist.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Pics pic = postSnapshot.getValue(Pics.class);
                    Pics p = new Pics();
                    if (pic != null) {
                        p.setUri(pic.getUri());
                    }
                    Picslist.add(p);
                    System.out.println("icons " + Picslist.size());


                }

                Objects.requireNonNull(picrecycler).setHasFixedSize(true);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
                RecyclerPicAdapter recyclerPicAdapter = new RecyclerPicAdapter(quotesDB, getContext(), Picslist,
                        blurView, getActivity(), myDialog,myquotes);
                picrecycler.setAdapter(recyclerPicAdapter);
                picrecycler.setLayoutManager(llm);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myDialog.show();
        Animation in = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
        blurView.setBlurRadius(40);
        blurView.setOverlayColor(R.color.lwhite);
        blurView.setVisibility(View.VISIBLE);
        blurView.startAnimation(in);


        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Animation out = AnimationUtils.loadAnimation(getContext(),R.anim.mi_fade_out);
                blurView.startAnimation(out);
                blurView.setOverlayColor(Color.TRANSPARENT);
                blurView.setBlurRadius(0);
                userinfo();

            }
        });


    }




    private void Carregar() {


        quotesdb.keepSynced(true);


        quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("userID")
        .startAt(user.getUid())
        .endAt(user.getUid() + searcharg);
        quotesdb.addValueEventListener(new ValueEventListener() {

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
                        quotes.setLikes(q.getLikes());
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
                            if (q.getUserID().equals(user.getUid())){
                            quotes.setUsername(user.getDisplayName());
                            quotes.setUserphoto(String.valueOf(user.getPhotoUrl()));
                        myquotes.add(quotes);

                        System.out.println("Quotes " + myquotes.size());}

                    }
                }

                Collections.reverse(myquotes);
                myquotesrecycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                myquotesrecycler.setHasFixedSize(true);
                System.out.println(myquotes.size());
                final Animation myanim2 = AnimationUtils.loadAnimation(getContext(), R.anim.transition);
                RecyclerAdapter myadapter = new RecyclerAdapter( getContext(), myquotes,  getActivity());
                myquotesrecycler.setAdapter(myadapter);
                myquotesrecycler.setLayoutManager(llm);
                myquotesrecycler.startAnimation(myanim2);
                final ValueAnimator animator = ValueAnimator.ofInt(0, myquotes.size());
                animator.setDuration(2500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        postnumber.setText(valueAnimator.getAnimatedValue().toString() + " publicações");
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
