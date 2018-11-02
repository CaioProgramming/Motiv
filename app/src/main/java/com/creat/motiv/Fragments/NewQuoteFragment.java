package com.creat.motiv.Fragments;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.RecyclerColorAdapter;
import com.creat.motiv.Adapters.RecyclerFontAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Pref;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import com.wooplr.spotlight.SpotlightView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewQuoteFragment extends Fragment {


    private  EditText frase;
    private EditText author;
    private RadioButton music;
    private  RadioButton citation;
    private  RadioButton motivation;
    private  RadioButton love;
     String categoria = "Nenhum";
     int textcolorstring;
     int backgoundcolor;
    FirebaseUser user;
    QuotesDB quotesDB;
    private CardView preview;

    private LinearLayout options;
    private CircleImageView userpic;
    private TextView username;
    private LinearLayout category;
    private LinearLayout background;
    Pref preferences;
    private boolean italicb, boldb;
    boolean tuto = true;
    TextView fontid,texcolorid,backcolorid;
    private com.github.clans.fab.FloatingActionButton textcolorfab;
    private com.github.clans.fab.FloatingActionButton backcolorfab;
    private com.github.clans.fab.FloatingActionButton fontpickerfab;
    private com.github.clans.fab.FloatingActionButton italicfab;
    private com.github.clans.fab.FloatingActionButton boldfab;
    private com.github.clans.fab.FloatingActionButton salvar;
    private com.github.clans.fab.FloatingActionMenu materialdesignandroidfloatingactionmenu;
    private RecyclerView colorlibrary;
    public NewQuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        italicb = false;
        boldb = false;
        preferences = new Pref(getContext());
        user = FirebaseAuth.getInstance().getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_newquote, container, false);
         colorlibrary = view.findViewById(R.id.colorlibrary);
        materialdesignandroidfloatingactionmenu = view.findViewById(R.id.material_design_android_floating_action_menu);
        salvar = view.findViewById(R.id.salvar);
        boldfab = view.findViewById(R.id.boldfab);
        italicfab = view.findViewById(R.id.italicfab);
        fontpickerfab = view.findViewById(R.id.fontpickerfab);
        backcolorfab = view.findViewById(R.id.backcolorfab);
        textcolorfab = view.findViewById(R.id.textcolorfab);
        texcolorid = view.findViewById(R.id.texcolorid);
        backcolorid = view.findViewById(R.id.backcolorid);

        username = view.findViewById(R.id.username);
        userpic = view.findViewById(R.id.userpic);
        username.setText(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl()).into(userpic);
        preview = view.findViewById(R.id.card);
        background  = view.findViewById(R.id.background);
        options = view.findViewById(R.id.options);
        category = view.findViewById(R.id.category);
        love = view.findViewById(R.id.love);
        motivation = view.findViewById(R.id.motivation);
        citation = view.findViewById(R.id.citation);
        music = view.findViewById(R.id.music);
        author = view.findViewById(R.id.author);
        frase = view.findViewById(R.id.quote);
        fontid = view.findViewById(R.id.fontid);

        theme(view);
        CategorySeleect(category);

        backcolorfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackColorpicker();
            }
        });

        textcolorfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Textocolorpicker();
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvar();
            }
        });

        fontpickerfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fontpicker();
            }
        });


        boldfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boldtext();
            }
        });

        italicfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                italic();
            }
        });


        frase.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    if (!keyEvent.isShiftPressed()) {
                        Log.v("AndroidEnterKeyActivity", "Enter Key Pressed!");
                        switch (view.getId()) {
                            case 1:
                                author.requestFocus();
                                break;
                        }
                        return true;
                    }
                }
                return false;
            }
        });


        author.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH ||
                        i == EditorInfo.IME_ACTION_DONE ||
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                                keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    if (!keyEvent.isShiftPressed()) {
                        System.out.println("Enter key pressed");
                        switch (view.getId()) {
                            case 1:
                                salvar();
                                break;
                        }
                        return true;
                    }

                }
                return false; // pass on to other listeners.

            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Tutorial();
                return false;
            }
        });

        colorgallery();
        return view;
    }

    private void CategorySeleect(final LinearLayout category) {
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category.setVisibility(View.INVISIBLE);
                category.setBackgroundResource(R.color.purple_300);
                int cx = category.getRight();
                int cy = category.getTop();
                int radius = Math.max(category.getWidth(), category.getHeight());
                Animator anim = ViewAnimationUtils.createCircularReveal(category, cx, cy,
                        0, radius);
                category.setVisibility(View.VISIBLE);
                anim.start();

                categoria = "Musica";
            }
        });

        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category.setVisibility(View.INVISIBLE);
                category.setBackgroundResource(R.color.red_300);
                int cx = category.getRight();
                int cy = category.getTop();
                int radius = Math.max(category.getWidth(), category.getHeight());
                Animator anim = ViewAnimationUtils.createCircularReveal(category, cx, cy,
                        0, radius);
                category.setVisibility(View.VISIBLE);
                anim.start();

                categoria = "Amor";
            }
        });

        citation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category.setVisibility(View.INVISIBLE);
                category.setBackgroundResource(R.color.grey_300);
                int cx = background.getRight();
                int cy = background.getTop();
                int radius = Math.max(category.getWidth(), category.getHeight());
                Animator anim = ViewAnimationUtils.createCircularReveal(category, cx, cy,
                        0, radius);
                category.setVisibility(View.VISIBLE);
                anim.start();
                categoria = "Citação";
            }
        });

        motivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category.setVisibility(View.INVISIBLE);
                category.setBackgroundResource(R.color.orange_300);
                int cx = category.getRight();
                int cy = category.getTop();
                int radius = Math.max(category.getWidth(), category.getHeight());
                Animator anim = ViewAnimationUtils.createCircularReveal(category, cx, cy,
                        0, radius);
                category.setVisibility(View.VISIBLE);
                anim.start();
                categoria = "Motivação";
            }
        });
    }

    private void theme(View view) {
        if (preferences.nightmodestate()) {
            options.setBackgroundResource(R.color.grey_800);
            love.setTextColor(Color.WHITE);
            motivation.setTextColor(Color.WHITE);
            motivation.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
            love.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
            citation.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
            music.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
            citation.setTextColor(Color.WHITE);
            music.setTextColor(Color.WHITE);
            view.setBackgroundResource(R.drawable.gradnight);
        }
    }

    private void italic() {
        if (boldb) {
            frase.setTypeface(frase.getTypeface(), Typeface.BOLD_ITALIC);
            author.setTypeface(frase.getTypeface(), Typeface.BOLD_ITALIC);
        }
        if (italicb) {
            frase.setTypeface(frase.getTypeface(), Typeface.NORMAL);
            author.setTypeface(frase.getTypeface(), Typeface.NORMAL);
            italicb = false;
        } else {
            frase.setTypeface(frase.getTypeface(), Typeface.ITALIC);
            author.setTypeface(frase.getTypeface(), Typeface.ITALIC);
            italicb = true;
        }
    }

    private void boldtext() {
        if (italicb && boldb) {
            frase.setTypeface(frase.getTypeface(), Typeface.BOLD_ITALIC);
            author.setTypeface(frase.getTypeface(), Typeface.BOLD_ITALIC);
        }
        if (boldb) {
            frase.setTypeface(frase.getTypeface(), Typeface.NORMAL);
            author.setTypeface(frase.getTypeface(), Typeface.NORMAL);
            boldb = false;
        } else {
            frase.setTypeface(frase.getTypeface(), Typeface.BOLD);
            author.setTypeface(frase.getTypeface(), Typeface.BOLD);
            boldb = true;
        }
    }

    private void BackColorpicker() {
        final ColorPicker cp = new ColorPicker(getActivity());
        cp.show();
        cp.enableAutoClose();
        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(int color) {
                background.setVisibility(View.INVISIBLE);
                background.setBackgroundColor(color);
                int cx = background.getRight();
                int cy = background.getTop();
                int radius = Math.max(background.getWidth(), background.getHeight());
                Animator anim = ViewAnimationUtils.createCircularReveal(background, cx, cy,
                        0, radius);
                background.setVisibility(View.VISIBLE);
                anim.start();
                backcolorid.setText(String.valueOf(color));
             }
        });
    }

    private void Textocolorpicker() {
        final ColorPicker cp = new ColorPicker(getActivity());
        cp.show();
        cp.enableAutoClose();
        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(final int color) {
                Log.d("Pure Hex", Integer.toHexString(color));
                int colorFrom = frase.getCurrentTextColor();
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, color);
                colorAnimation.setDuration(2000); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        frase.setTextColor(color);
                        author.setTextColor(color);

                    }

                });
                colorAnimation.start();

                texcolorid.setText(String.valueOf(color));


            }
        });
    }

    private void Fontpicker() {
        BottomSheetDialog myDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
        final RealtimeBlurView blurView = Objects.requireNonNull(getActivity()).findViewById(R.id.rootblur);
        blurView.setBlurRadius(20);
        myDialog.setContentView(R.layout.profilepicselect);
        RecyclerView recyclerView = myDialog.findViewById(R.id.picsrecycler);
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        RecyclerFontAdapter recyclerFontAdapter = new RecyclerFontAdapter(getContext(), blurView, myDialog, frase, author, fontid);
        recyclerView.setAdapter(recyclerFontAdapter);
        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                blurView.setBlurRadius(0);
                blurView.setOverlayColor(Color.TRANSPARENT);

            }
        });
        myDialog.show();
    }

    private void Tutorial() {
        Boolean novo = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getBoolean("novo");
        if (novo){
            new SpotlightView.Builder(getActivity())
                    .introAnimationDuration(400)
                    .enableRevealAnimation(true)
                    .performClick(true)
                    .fadeinTextDuration(400)
                    .headingTvColor(R.color.colorPrimary)
                    .headingTvSize(32)
                    .headingTvText("Criação de frases")
                    .subHeadingTvColor(Color.parseColor("#ffffff"))
                    .subHeadingTvSize(16)
                    .subHeadingTvText("Aqui é o seu cantinho mágico, onde poderá criar suas frases! ")
                    .maskColor(Color.parseColor("#dc000000"))
                    .target(preview)
                    .lineAnimDuration(400)
                    .lineAndArcColor(Color.parseColor("#eb273f"))
                    .dismissOnTouch(true)
                    .dismissOnBackPress(true)
                    .enableDismissAfterShown(true)
                    .usageId("createscreen") //UNIQUE ID
                    .show();
        }
        tuto = false;
    }

    public void salvar(){

        Date datenow = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dia = df.format(datenow);
        System.out.println(dia);
        Integer fonti = null;
        if (fontid.getText() != "") {
            fonti = Integer.parseInt(fontid.getText().toString());

        }
        Quotes quotes = new Quotes(null, frase.getText().toString(),
                author.getText().toString(),
                dia, categoria,
                user.getUid(),
                user.getDisplayName(),
                String.valueOf(user.getPhotoUrl()),
                0, Integer.parseInt(texcolorid.getText().toString()),
                Integer.parseInt(backcolorid.getText().toString()), italicb, boldb, fonti);
        System.out.println("font" + quotes.getFont() + "backcolor e textcolor " + backgoundcolor + textcolorstring);

        if (author.getText().toString().equals("")){
            quotes.setAuthor(user.getDisplayName());
        }
        quotesDB = new QuotesDB(quotes, Objects.requireNonNull(getActivity()));
        quotesDB.Inserir();



    }

    public void colorgallery(){
        final ArrayList<com.creat.motiv.Beans.Color> ColorList;
        ColorList = new ArrayList<>();
        ValueEventListener databaseReference = FirebaseDatabase.getInstance().getReference("Colors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ColorList.clear();
                Integer i = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    i++;
                    com.creat.motiv.Beans.Color color = postSnapshot.getValue(com.creat.motiv.Beans.Color.class);
                    com.creat.motiv.Beans.Color c = new com.creat.motiv.Beans.Color();
                    System.out.println("Color Hex " + color.getValue());
                    c.setValue(color.getValue());
                    ColorList.add(color);
                    System.out.println(ColorList.size() + "   Colors");

                }
                System.out.println("Load " + ColorList.size() + " colors");
                Collections.shuffle(ColorList);
                colorlibrary.setHasFixedSize(true);
                GridLayoutManager llm = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
                RecyclerColorAdapter recyclerColorAdapter = new RecyclerColorAdapter(ColorList,getContext(),
                        background,frase,author,getActivity(),backcolorid,texcolorid);
                colorlibrary.setAdapter(recyclerColorAdapter);
                colorlibrary.setLayoutManager(llm);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Tutorial();
    }









}
