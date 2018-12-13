package com.creat.motiv.Fragments;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import com.wooplr.spotlight.SpotlightView;

import java.lang.reflect.Field;
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


     String categoria = "Nenhum";
    FirebaseUser user;
    QuotesDB quotesDB;

    Pref preferences;
    boolean tuto = true;
    private RecyclerView colorlibrary;
    private RadioButton love;
    private RadioButton citation;
    private RadioButton motivation;
    private RadioButton music;
    private LinearLayout options;
    private CircleImageView userpic;
    private TextView username;
    private ImageButton menu;
    private LinearLayout quotedata;
    private LinearLayout category;
    private EditText frase;
    private EditText author;
    private LinearLayout background;
    private TextView fontid;
    private TextView texcolorid;
    private TextView backcolorid;
    private CardView card;
    private com.github.clans.fab.FloatingActionButton categoryfab;
    private com.github.clans.fab.FloatingActionButton textcolorfab;
    private com.github.clans.fab.FloatingActionButton backcolorfab;
    private com.github.clans.fab.FloatingActionButton fontpickerfab;
    private com.github.clans.fab.FloatingActionButton salvar;
    private com.github.clans.fab.FloatingActionMenu materialdesignandroidfloatingactionmenu;
    private android.widget.RadioGroup categories;
    private EditText quote;

    public NewQuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = new Pref(Objects.requireNonNull(getContext()));
        user = FirebaseAuth.getInstance().getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_newquote, container, false);
        this.categories = view.findViewById(R.id.categories);
        this.materialdesignandroidfloatingactionmenu = view.findViewById(R.id.material_design_android_floating_action_menu);
        this.salvar = view.findViewById(R.id.salvar);
        this.fontpickerfab = view.findViewById(R.id.fontpickerfab);
        this.backcolorfab = view.findViewById(R.id.backcolorfab);
        this.textcolorfab = view.findViewById(R.id.textcolorfab);
        this.categoryfab = view.findViewById(R.id.categoryfab);
        this.card = view.findViewById(R.id.card);
        this.backcolorid = view.findViewById(R.id.backcolorid);
        this.texcolorid = view.findViewById(R.id.texcolorid);
        this.fontid = view.findViewById(R.id.fontid);
        this.background = view.findViewById(R.id.background);
        this.author = view.findViewById(R.id.author);
        this.frase = view.findViewById(R.id.quote);
        this.category = view.findViewById(R.id.category);
        this.quotedata = view.findViewById(R.id.quotedata);
        this.menu = view.findViewById(R.id.menu);
        this.username = view.findViewById(R.id.username);
        this.userpic = view.findViewById(R.id.userpic);
        this.options = view.findViewById(R.id.options);
        this.music = view.findViewById(R.id.music);
        this.motivation = view.findViewById(R.id.motivation);
        this.citation = view.findViewById(R.id.citation);
        this.love = view.findViewById(R.id.love);
        this.colorlibrary = view.findViewById(R.id.colorlibrary);

        username.setText(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl()).into(userpic);
        theme();
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

        categoryfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categories.clearCheck();
                categoria = "Nenhum";
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



        try {
            colorgallery();
        } catch (ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
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
                category.setBackgroundResource(R.color.black);
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

    private void theme() {
        if (preferences.nightmodestate()) {
            options.setBackgroundResource(R.color.grey_800);


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
        blurView.setVisibility(View.VISIBLE);
        blurView.setBlurRadius(50);
        myDialog.setContentView(R.layout.profilepicselect);
        RecyclerView recyclerView = myDialog.findViewById(R.id.picsrecycler);
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        assert recyclerView != null;
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
            Spotlight();
            //getData();
        }
        tuto = false;
    }

    private void Spotlight() {
        new SpotlightView.Builder(Objects.requireNonNull(getActivity()))
                .introAnimationDuration(400)
                .enableRevealAnimation(true)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvText("Criação de frases")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(14)
                .subHeadingTvText("Aqui é o seu cantinho mágico, onde poderá criar suas frases! ")
                .maskColor(Color.parseColor("#dc000000"))
                .target(card)
                .lineAnimDuration(400)
                .lineAndArcColor(R.color.white)
                .headingTvColor(R.color.white)
                .headingTvSize(28)
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId("createscreen") //UNIQUE ID
                .show();
    }

    public void salvar() {

        Date datenow = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dia = df.format(datenow);
        System.out.println(dia);
        Integer fonti = null;
        if (fontid.getText() != "") {
            fonti = Integer.parseInt(fontid.getText().toString());

        }
        if (texcolorid.getText() == ""){
            texcolorid.setText(String.valueOf(Color.BLACK));
        }
        if (backcolorid.getText() == ""){
            backcolorid.setText(String.valueOf(Color.WHITE)  );
        }

        Quotes quotes = new Quotes(null,
                frase.getText().toString(),
                author.getText().toString(),
                dia, categoria,
                user.getUid(),
                user.getDisplayName(),
                String.valueOf(user.getPhotoUrl()),
                Integer.parseInt(backcolorid.getText().toString()),
                Integer.parseInt( texcolorid.getText().toString()),
                false,
                false,
                fonti,
                false);
        System.out.println("font " + quotes.getFont() + "backcolor e textcolor " + quotes.getBackgroundcolor() + " "+ quotes.getTextcolor());

        if (author.getText().toString().equals("")){
            quotes.setAuthor(user.getDisplayName());
        }
        if (user.isEmailVerified()){
        quotesDB = new QuotesDB(quotes, Objects.requireNonNull(getActivity()));
        quotesDB.Inserir();
        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setMessage("Email não verificado");
            builder.setMessage("Seu email não foi verificado, então não vai poder compartilhar frases.");
            builder.setPositiveButton("Então me envia esse email meu consagrado", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    user.sendEmailVerification();
                }
            });
            builder.setNegativeButton("Beleza então, não vou postar nada", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.show();



        }



    }



    public void colorgallery() throws ClassNotFoundException, IllegalAccessException {
        ArrayList<Integer> colors = new ArrayList<>();
        Field[] fields = Class.forName(Objects.requireNonNull(getContext()).getPackageName() +".R$color").getDeclaredFields();
        for(Field field : fields) {
            String colorName = field.getName();
            int colorId = field.getInt(null);
            int color = getResources().getColor(colorId);
            System.out.println("color " + colorName + " " + color);
            colors.add(color);

        }

        System.out.println("Load " + colors.size() + " colors");
        Collections.reverse(colors);
        colorlibrary.setHasFixedSize(true);
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
        RecyclerColorAdapter recyclerColorAdapter = new RecyclerColorAdapter(colors,getContext(),
                background,frase,author,getActivity(),texcolorid,backcolorid);
        colorlibrary.setAdapter(recyclerColorAdapter);
        colorlibrary.setLayoutManager(llm);
    }

    @Override
    public void onStart() {
        super.onStart();
        Tutorial();
    }









}
