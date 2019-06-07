package com.creat.motiv.Fragments;


import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.RecyclerAdapter;
import com.creat.motiv.Adapters.RecyclerPicAdapter;
import com.creat.motiv.Beans.Likes;
import com.creat.motiv.Beans.Pics;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Info;
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
    private CircleImageView profilepic;
    private android.support.v7.widget.RecyclerView myquotesrecycler;
    private Query quotesdb;
    private Toolbar toolbar;
     private RelativeLayout loading;
    View v;
    private android.support.design.widget.CollapsingToolbarLayout collapsetoolbar;
    private android.support.design.widget.AppBarLayout appbarlayout;
    private android.support.design.widget.TabLayout profiletab;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    Fragment fragment = this;
    private com.github.clans.fab.FloatingActionButton camerafab;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        if (getContext() == null){
            return null;
        }
        preferences = new Pref(Objects.requireNonNull(getContext()));
        user = FirebaseAuth.getInstance().getCurrentUser();
        quotesdb = FirebaseDatabase.getInstance().getReference();

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_profile, container, false);
        }
        this.profiletab = v.findViewById(R.id.profiletab);
        this.appbarlayout = v.findViewById(R.id.appbarlayout);
        this.collapsetoolbar = v.findViewById(R.id.collapsetoolbar);
        this.camerafab = v.findViewById(R.id.camerafab);
        this.loading = v.findViewById(R.id.loading);
         toolbar = v.findViewById(R.id.toolbar);
        myquotesrecycler = v.findViewById(R.id.myquotesrecycler);
        profilepic = v.findViewById(R.id.profilepic);


        camerafab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picalert();
            }
        });











        Tutorial(v);
        //Carregar();
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            toolbar.setTitle(" ");

        } else {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        // Set collapsing tool bar image.


        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picalert();
            }
        });

        Carregar();
        CarregarLikes();
        userinfo();
        setupTabIcons();
        countinganimation();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.pop_in, R.anim.fab_slide_out_to_left)
                        .replace(R.id.frame, new HomeFragment())
                        .commit();

                BottomNavigationView navigationView = getActivity().findViewById(R.id.navigation);
                navigationView.setSelectedItemId(R.id.navigation_home);
            }
        });
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Cabin-Regular.ttf");
        collapsetoolbar.setExpandedTitleTypeface(tf);

        collapsetoolbar.setCollapsedTitleTypeface(tf);


        profiletab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Carregar();
                        break;
                    case 1:
                        Carregar();
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Carregar();
                        break;
                    case 1:
                        Carregar();
                        break;
                    default:
                        break;

                }
            }
        });


        return v;

    }


    private void setupTabIcons() {
        profiletab.setSelectedTabIndicatorColor(Color.TRANSPARENT);

        for (int i = 0; i < profiletab.getTabCount(); i++) {
            Objects.requireNonNull(profiletab.getTabAt(i)).setIcon(R.drawable.dot);
        }

    }
    private void removepostsalert() {
        if (getContext() == null){
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext())).setTitle("Apagar posts?");
        builder.setMessage("Tem certeza disso  Quer perder tudo? Não tem como voltar atrás depois...");
        builder.setNegativeButton("Cliquei errado calma aí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Isso aí apaga todos os meus posts!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int y = 0; y < myquotes.size(); y++) {
                    QuotesDB quotesDB = new QuotesDB();
                    quotesDB.Removerposts(getActivity(), myquotes.get(y).getId());
                }
                builder.setMessage("Parabéns apagou tudo");
            }
        }).show();
    }

    private void removeaccountalert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext())).setTitle("Apagar a conta?");
        builder.setMessage("Nossa... Apagar a conta? TEM CERTEZA?," +
                " você não pode desfazer essa ação e perderá tudo que já fez aqui");
        builder.setPositiveButton("Sim, cansei do motiv", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int y = 0; y < myquotes.size(); y++) {
                    QuotesDB quotesDB = new QuotesDB();
                    quotesDB.Apagarconta(getActivity(), myquotes.get(y).getId());
                }

                if (likequotes.size() > 0) {
                    for (int y = 0; y < likequotes.size(); y++) {
                        QuotesDB quotesDB = new QuotesDB();
                        quotesDB.Apagarlikes(getActivity(), likequotes.get(y).getId());
                    }
                }
                builder.setMessage("Pronto, você apagou sua conta, pode sair já, não é o que você queria?");
                CountDownTimer timer = new CountDownTimer(5000, 100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        user.delete();
                        FirebaseAuth.getInstance().signOut();

                        Objects.requireNonNull(getActivity()).finish();
                    }
                };
                timer.start();
            }
        });
        builder.setNegativeButton("Só quis dar um susto", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }


    private void Tutorial(View view) {
        Boolean novo = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getBoolean("novo");

        if (novo){
            if (myquotes.size() > 0) {
                if (!preferences.profiletutorialstate()) {
                    preferences.setProfileTutorial(true);
                    Info.tutorial("Você de novo" + user.getDisplayName(), getActivity());
                }

            } else {
                Pref preferences = new Pref(getContext());
                if (!preferences.profiletutorialstate()) {
                    preferences.setProfileTutorial(true);
                    Info.tutorial(getString(R.string.profile_intro), getActivity());
                }
            }

        }
    }



    private void userinfo() {

        toolbar.setTitle(user.getDisplayName());

        if (user.getPhotoUrl() == null) {
            Glide.with(this).asBitmap().load("https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/fantastic_planet_001.jpg?alt=media&token=03f77356-b17a-45f4-ac31-baf0bc9f87f5").into(profilepic);

        } else {
            Glide.with(this).asBitmap().load(user.getPhotoUrl()).into(profilepic);
        }


    }

    private void countinganimation() {



        int finalcount = likequotes.size();
        ValueAnimator animator2 = ValueAnimator.ofInt(0, finalcount);
        animator2.setDuration(2500);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                profiletab.getTabAt(1).setText(valueAnimator.
                        getAnimatedValue().toString() + " favoritos");
            }
        });
        animator2.start();
    }


    private void Picalert() {
        final ArrayList<Pics> Picslist;

        final RealtimeBlurView blurView = Objects.requireNonNull(getActivity()).findViewById(R.id.rootblur);
        Picslist = new ArrayList<>();
        Picslist.add(null);
        final BottomSheetDialog myDialog = new BottomSheetDialog(getActivity(), R.style.Dialog_No_Border);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(R.layout.profilepicselect_);
        myDialog.show();
        final LinearLayout back = myDialog.findViewById(R.id.back);
        final TextView title = myDialog.findViewById(R.id.title);
        final TextView message = myDialog.findViewById(R.id.message);
        final ProgressBar pb = myDialog.findViewById(R.id.pb);
        final RecyclerView picrecycler;
        final Button remove;

        remove = myDialog.findViewById(R.id.removepic);
        assert remove != null;
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

        databaseReference = FirebaseDatabase.getInstance().getReference("images").orderByKey().addValueEventListener(new ValueEventListener() {
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
                        blurView, getActivity(), myDialog, myquotes, message, remove, pb, picrecycler, title, back);
                picrecycler.setAdapter(recyclerPicAdapter);
                picrecycler.setLayoutManager(llm);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myDialog.show();
        Animation in = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
        blurView.setVisibility(View.VISIBLE);
        blurView.startAnimation(in);


        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                blurView.startAnimation(out);
                blurView.setVisibility(View.GONE);
                userinfo();


            }
        });


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
                //Snacky.builder().setActivity(getActivity()).error().setText("Erro " + databaseError.getMessage()).show();
            }
        });

///        userinfo();
        //show();


    }

    private void like(final Quotes position) {
        if (getActivity() == null) {
            return;
        }
        //loading.setVisibility(View.VISIBLE);
        likesArrayList = new ArrayList<>();
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
                ValueAnimator animator = ValueAnimator.ofInt(0, likequotes.size());
                animator.setDuration(2500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {

                        profiletab.getTabAt(1).setText(valueAnimator.getAnimatedValue().toString() + " publicações");
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
        loading.setVisibility(View.VISIBLE);
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
                ValueAnimator animator = ValueAnimator.ofInt(0, likequotes.size());
                animator.setDuration(2500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {

                        profiletab.getTabAt(1).setText(valueAnimator.getAnimatedValue().toString() + " publicações");
                    }
                });
                animator.start();
                recycler(myquotes);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

///        userinfo();

        show();
    }

    private void recycler(ArrayList<Quotes> quotes) {
        // Collections.reverse(quotes);
        myquotesrecycler.setVisibility(View.VISIBLE);
        GridLayoutManager llm = new GridLayoutManager(getActivity(), Tools.spancount, GridLayoutManager.VERTICAL, false);
        myquotesrecycler.setHasFixedSize(true);
        System.out.println(quotes);
        final Animation myanim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_scale_up);
        RecyclerAdapter myadapter = new RecyclerAdapter(getContext(), quotes, getActivity(), myquotesrecycler);
        myquotesrecycler.setAdapter(myadapter);
        myquotesrecycler.setLayoutManager(llm);
        myquotesrecycler.startAnimation(myanim2);
    }

    private void show() {
        CountDownTimer timer = new CountDownTimer(2000, 100) {
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
                if (myquotes == null) {
                    Carregar();
                }
                if (likequotes == null) {
                    CarregarLikes();
                }
                userinfo();

            }
        };
        timer.start();
    }


}
