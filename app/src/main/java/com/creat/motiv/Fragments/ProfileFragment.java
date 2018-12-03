package com.creat.motiv.Fragments;


import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Adapters.RecyclerPicAdapter;
import com.creat.motiv.Beans.Pics;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.creat.motiv.Utils.ColorUtils;
import com.creat.motiv.Utils.Pref;
import com.creat.motiv.Utils.Tools;
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

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

import static com.creat.motiv.Database.QuotesDB.path;
import static com.creat.motiv.Database.QuotesDB.searcharg;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    ArrayList<Quotes> myquotes;
    ValueEventListener databaseReference;
    QuotesDB quotesDB;
    Pref preferences;
    private CircleImageView profilepic;
    private android.widget.TextView postnumber;
    private android.support.v7.widget.RecyclerView myquotesrecycler;
    private Query quotesdb;
    private Toolbar toolbar;
    private android.widget.ImageView offlineimage;
    private RelativeLayout loading;
    private TextView username;
    private com.github.clans.fab.FloatingActionButton camerafab;
    private android.support.design.widget.CollapsingToolbarLayout collapsetoolbar;
    private android.support.design.widget.AppBarLayout appbarlayout;
    private TextView post;
    private android.support.design.widget.TabLayout profiletab;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private int[] tabIcons = {
            R.drawable.ic_person_white_24dp,
            R.drawable.ic_favorite_white_24dp,

    };
    private int[] tabIconsblack = {
            R.drawable.ic_person_black_24dp,
            R.drawable.ic_favorite2_black_24dp,

    };
    private android.widget.ImageButton settings;
    public ProfileFragment() {
        // Required empty public constructor
    }

    private void setupicons() {
        profiletab.removeAllTabs();
        for (int i = 0; i < tabIcons.length; i++) {
            profiletab.addTab(profiletab.newTab().setIcon(tabIcons[i]));
        }
        profiletab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (profiletab.getSelectedTabPosition()) {
                    case 0:
                        Carregar();
                        break;
                    case 1:
                        CarregarLikes();
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setupblackicons() {
        profiletab.removeAllTabs();
        for (int i = 0; i < tabIconsblack.length; i++) {
            profiletab.addTab(profiletab.newTab().setIcon(tabIconsblack[i]));
        }
        profiletab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (profiletab.getSelectedTabPosition()) {
                    case 0:
                        Carregar();
                        break;
                    case 1:
                        CarregarLikes();
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void Likes() {
        Snacky.builder().setText("Em breve...").setActivity(getActivity()).setBackgroundColor(Color.BLACK).setTextColor(Color.WHITE).build().show();
    }






    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        preferences = new Pref(getContext());
        user = FirebaseAuth.getInstance().getCurrentUser();
        quotesdb = FirebaseDatabase.getInstance().getReference();


        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        this.settings = view.findViewById(R.id.settings);
        this.profiletab = view.findViewById(R.id.profiletab);
        this.post = view.findViewById(R.id.post);
        this.appbarlayout = view.findViewById(R.id.appbarlayout);
        this.collapsetoolbar = view.findViewById(R.id.collapsetoolbar);
        this.camerafab = view.findViewById(R.id.camerafab);
        this.username = view.findViewById(R.id.username);
        this.loading = view.findViewById(R.id.loading);
        this.offlineimage = view.findViewById(R.id.offlineimage);
        toolbar = view.findViewById(R.id.toolbar);
        postnumber = view.findViewById(R.id.postnumber);
        myquotesrecycler = view.findViewById(R.id.myquotesrecycler);
        profilepic = view.findViewById(R.id.profilepic);


        camerafab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picalert();
            }
        });


        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setEnabled(true);

                username.requestFocus();
            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            private android.widget.LinearLayout deletecount;
            private Button removeacount;
            private android.widget.LinearLayout deleteposts;
            private Button removeallposts;
            private android.widget.LinearLayout namelayout;
            private android.widget.LinearLayout editname;
            private Button changename;
            private EditText username;

            @Override
            public void onClick(View view) {
                final RealtimeBlurView blurView = Objects.requireNonNull(getActivity()).findViewById(R.id.rootblur);
                blurView.setBlurRadius(50);
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                bottomSheetDialog.setContentView(R.layout.settings);
                this.deletecount = bottomSheetDialog.findViewById(R.id.deletecount);
                this.removeacount = bottomSheetDialog.findViewById(R.id.removeacount);
                this.deleteposts = bottomSheetDialog.findViewById(R.id.deleteposts);
                this.removeallposts = bottomSheetDialog.findViewById(R.id.removeallposts);
                this.namelayout = bottomSheetDialog.findViewById(R.id.namelayout);
                this.editname = bottomSheetDialog.findViewById(R.id.editname);
                this.changename = bottomSheetDialog.findViewById(R.id.changename);
                this.username = bottomSheetDialog.findViewById(R.id.username);

                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        blurView.setBlurRadius(0);
                        blurView.setOverlayColor(Color.TRANSPARENT);
                    }
                });
                username.setHint(user.getDisplayName());
                changename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("Salvando");
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username.getText().toString()).build();
                        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                for (int i = 0; i < myquotes.size(); i++) {
                                    QuotesDB quotesDB = new QuotesDB(getActivity(), null);
                                    quotesDB.AlterarNome(getActivity(), myquotes.get(i).getId());

                                }
                                progressDialog.setMessage("Salvo!");
                                progressDialog.dismiss();
                                bottomSheetDialog.dismiss();
                            }
                        });

                        removeacount.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetDialog.dismiss();
                                Snacky.builder().setActivity(getActivity())
                                        .setText("Tem certeza que quer apagar sua conta?")
                                        .setBackgroundColor(Color.RED)
                                        .setTextColor(Color.WHITE)
                                        .setActionTextColor(getContext().getResources().getColor(R.color.white))
                                        .setActionText("Sim")
                                        .setIcon(getContext().getDrawable(R.drawable.ic_warning_black_24dp))
                                        .setActionClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                                progressDialog.show();

                                                for (int i3 = 0; i3 < myquotes.size(); i3++) {
                                                    QuotesDB quotesDB = new QuotesDB(getActivity(), null);
                                                    quotesDB.Removerposts(getActivity(), myquotes.get(i3).getId());

                                                }
                                                progressDialog.dismiss();
                                            }
                                        }).build().show();

                            }
                        });

                        removeallposts.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetDialog.dismiss();
                                Snacky.builder().setActivity(getActivity())
                                        .setText("Tem certeza que quer remover todos seus posts?")
                                        .setBackgroundColor(Color.WHITE)
                                        .setTextColor(Color.BLACK)
                                        .setActionTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                                        .setActionText("Sim")
                                        .setActionClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                                progressDialog.show();

                                                for (int i3 = 0; i3 < myquotes.size(); i3++) {
                                                    QuotesDB quotesDB = new QuotesDB(getActivity(), null);
                                                    quotesDB.Removerposts(getActivity(), myquotes.get(i3).getId());

                                                }
                                                progressDialog.dismiss();
                                            }
                                        }).build().show();
                            }
                        });


                    }
                });
                bottomSheetDialog.show();
            }
        });


        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsetoolbar.setTitle(user.getDisplayName());
                    collapsetoolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    collapsetoolbar.setCollapsedTitleGravity(Gravity.CENTER);
                    isShow = true;
                } else if (isShow) {
                    collapsetoolbar.setTitle(" ");
                    //carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        username.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:

                            UserProfileChangeRequest profileChangeRequest;

                            profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username.getText().toString()).build();
                            if (user != null) {
                                user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            for (int i = 0; i < myquotes.size(); i++) {

                                                QuotesDB quotesDB = new QuotesDB();
                                                quotesDB.AlterarNome(getActivity(), myquotes.get(i).getId());
                                            }

                                        }
                                    }
                                });
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        Glide.with(getContext())
                .load("http://www.fubiz.net/wp-content/uploads/2016/07/funnysurreal-12.jpg").listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                offlineimage.setImageDrawable(resource);
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                offlineimage.startAnimation(animation);
                return true;
            }
        })
                .into(offlineimage);


        Tutorial(view);
        //Carregar();
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(user.getDisplayName());
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);


        }


        // Set collapsing tool bar image.

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Tutorial(view);
                return false;
            }
        });

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picalert();
            }
        });

        Carregar();
        userinfo();
        return view;

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


    private void userinfo() {

        username.setText(user.getDisplayName());
        if (user.getPhotoUrl() == null){
            Glide.with(this).asBitmap().load("https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/fantastic_planet_001.jpg?alt=media&token=03f77356-b17a-45f4-ac31-baf0bc9f87f5").listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    if (resource != null) {

                        int color = ColorUtils.getDominantColor(resource);

                        int[] colors = {color, ColorUtils.darken(color, 0.5f)};

                        //create a new gradient color
                        GradientDrawable gd = new GradientDrawable(
                                GradientDrawable.Orientation.TOP_BOTTOM, colors);

                        collapsetoolbar.setBackground(gd);
                        collapsetoolbar.setContentScrimColor(color);
                        profilepic.setImageBitmap(resource);

                        return true;
                    } else {
                        return false;
                    }
                }
            }).into(profilepic);

        } else {
            Glide.with(this).asBitmap().load(user.getPhotoUrl()).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    if (resource != null) {
                        int color = ColorUtils.getDominantColor(resource);


                        profilepic.setImageBitmap(resource);
                        if (Tools.isColorDark(color)) {
                            int[] colors = {color, ColorUtils.lighten(color, 0.8f)};

                            //create a new gradient color
                            collapsetoolbar.setContentScrimColor(ColorUtils.lighten(color, 0.8f));
                            GradientDrawable gd = new GradientDrawable(
                                    GradientDrawable.Orientation.TOP_BOTTOM, colors);
                            collapsetoolbar.setBackground(gd);
                            toolbar.setTitleTextColor(Color.WHITE);
                            toolbar.setTitleTextColor(Color.WHITE);
                            collapsetoolbar.setCollapsedTitleTextColor(Color.WHITE);
                            username.setTextColor(Color.WHITE);
                            postnumber.setTextColor(Color.WHITE);
                            post.setTextColor(Color.WHITE);

                            setupicons();
                            profiletab.setSelectedTabIndicatorColor(Color.WHITE);


                   /*         quotesdb.keepSynced(true);


                            quotesdb = FirebaseDatabase.getInstance().getReference().child(path)
                                    .child("likes")
                                    .orderByChild("userid")
                                    .startAt(user.getUid())
                                    .endAt(user.getUid() + searcharg);
                            quotesdb.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    profiletab.getTabAt(1).setText(dataSnapshot.getChildrenCount() + " Favoritos");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });*/


// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

// finally change the color
                        } else {
                            toolbar.setTitleTextColor(Color.BLACK);
                            toolbar.setTitleTextColor(Color.BLACK);
                            collapsetoolbar.setCollapsedTitleTextColor(Color.BLACK);
                            username.setTextColor(Color.BLACK);
                            postnumber.setTextColor(Color.BLACK);
                            post.setTextColor(Color.BLACK);
                            setupblackicons();
                            profiletab.setSelectedTabIndicatorColor(Color.BLACK);
                            int[] colors = {color, ColorUtils.lighten(color, 0.5f)};
                            collapsetoolbar.setContentScrimColor(ColorUtils.lighten(color, 0.5f));
                            //create a new gradient color
                            GradientDrawable gd = new GradientDrawable(
                                    GradientDrawable.Orientation.TOP_BOTTOM, colors);
                            collapsetoolbar.setBackground(gd);
                        }
                        return true;
                    } else {
                        return false;
                    }

                }
            }).into(profilepic);
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

                                    QuotesDB quotesDB = new QuotesDB();
                                    quotesDB.AlterarNome(getActivity(), myquotes.get(i).getId());
                                }


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




    private void CarregarLikes() {





        quotesdb.keepSynced(true);


        quotesdb = FirebaseDatabase.getInstance().getReference().child(path)
                .orderByChild("likes")
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
                myquotesrecycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), Tools.spancount, GridLayoutManager.VERTICAL, false);
                myquotesrecycler.setHasFixedSize(true);
                System.out.println(myquotes.size());
                final Animation myanim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_scale_up);
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), myquotes, getActivity(), myquotesrecycler);
                myquotesrecycler.setAdapter(myadapter);
                myquotesrecycler.setLayoutManager(llm);
                myquotesrecycler.startAnimation(myanim2);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

///        userinfo();
        show();
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
                myquotesrecycler.setVisibility(View.VISIBLE);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), Tools.spancount, GridLayoutManager.VERTICAL, false);
                myquotesrecycler.setHasFixedSize(true);
                System.out.println(myquotes.size());
                final Animation myanim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_scale_up);
                RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), myquotes, getActivity(), myquotesrecycler);
                myquotesrecycler.setAdapter(myadapter);
                myquotesrecycler.setLayoutManager(llm);
                myquotesrecycler.startAnimation(myanim2);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

///        userinfo();
        show();
    }

    private void show() {
        CountDownTimer timer = new CountDownTimer(5000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (getActivity() == null) {
                    return;
                }
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_scale_down);
                Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_scale_up);
                loading.startAnimation(animation);
                loading.setVisibility(View.INVISIBLE);
                profilepic.startAnimation(animation1);
                appbarlayout.setVisibility(View.VISIBLE);
                appbarlayout.startAnimation(animation2);
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
        }.start();
    }


}
