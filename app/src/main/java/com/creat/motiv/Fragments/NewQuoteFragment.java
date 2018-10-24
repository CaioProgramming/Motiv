package com.creat.motiv.Fragments;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.Pref;
import com.creat.motiv.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import com.wooplr.spotlight.SpotlightView;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import de.mateware.snacky.Snacky;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewQuoteFragment extends Fragment {


    private  EditText frase;
    private EditText author;
    private Button save;
    private RadioButton music;
    private  RadioButton citation;
    private  RadioButton motivation;
    private  RadioButton love;
     String categoria = "Nenhum";
     int textcolorstring;
     int backgoundcolor;
    FirebaseUser user;
    QuotesDB quotesDB;
    DatabaseReference raiz;
    private android.support.v7.widget.CardView preview;
    private Button textcolor;
    private Button backcolor;
    private LinearLayout options;
    private de.hdodenhof.circleimageview.CircleImageView userpic;
    private android.widget.TextView username;
    private LinearLayout category;
    private EditText quote;
    private LinearLayout background;
    private SharedPreferences setings;
    Pref preferences;
    private android.support.v7.widget.CardView card;
    public NewQuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        preferences = new Pref(getContext());
         user = FirebaseAuth.getInstance().getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_newquote, container, false);

         username = view.findViewById(R.id.username);
         userpic = view.findViewById(R.id.userpic);
         username.setText(user.getDisplayName());
         Glide.with(this).load(user.getPhotoUrl()).into(userpic);
        preview = view.findViewById(R.id.card);
        background  = view.findViewById(R.id.background);
        options = view.findViewById(R.id.options);
        Button backcolor = view.findViewById(R.id.backcolor);
        Button textcolor = view.findViewById(R.id.textcolor);
        final LinearLayout category = view.findViewById(R.id.category);
        love = view.findViewById(R.id.love);
         motivation = view.findViewById(R.id.motivation);
         citation = view.findViewById(R.id.citation);
         music = view.findViewById(R.id.music);
         save = view.findViewById(R.id.save);
          author = view.findViewById(R.id.author);
        frase = view.findViewById(R.id.quote);



        if (preferences.nightmodestate()) {
            options.setBackgroundResource(R.color.grey_800);
            love.setTextColor(Color.WHITE);
            motivation.setTextColor(Color.WHITE);
            citation.setTextColor(Color.WHITE);
            music.setTextColor(Color.WHITE);
            save.setTextColor(Color.WHITE);
            backcolor.setTextColor(Color.WHITE);
            textcolor.setTextColor(Color.WHITE);
            view.setBackgroundResource(R.color.grey_900); }

         textcolor.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
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

                         textcolorstring = color;


                     }
                 });

             }
         });

         backcolor.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
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
                         Animator anim = android.view.ViewAnimationUtils.createCircularReveal(background, cx, cy,
                                 0, radius);
                         background.setVisibility(View.VISIBLE);
                         anim.start();
                         backgoundcolor = color;
                     }
                 });
             }
         });


        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        category.setVisibility(View.INVISIBLE);
                        category.setBackgroundResource(R.color.purple_300);
                        int cx = category.getRight();
                        int cy = category.getTop();
                        int radius = Math.max(category.getWidth(), category.getHeight());
                        Animator anim = android.view.ViewAnimationUtils.createCircularReveal(category, cx, cy,
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
                int radius = Math.max(background.getWidth(), category.getHeight());
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(background, cx, cy,
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
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(category, cx, cy,
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
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(category, cx, cy,
                        0, radius);
                category.setVisibility(View.VISIBLE);
                anim.start();
                categoria = "Motivação";
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvar();
            }
        });


         frase.setOnKeyListener(new View.OnKeyListener() {
             @Override
             public boolean onKey(View view, int i, KeyEvent keyEvent) {
                 if (i == EditorInfo.IME_ACTION_NEXT){
                     if (!keyEvent.isShiftPressed()){
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



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
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
    }

    public void salvar(){
        if (!Objects.equals(frase.getText().toString(), "")) {
            Date datenow = Calendar.getInstance().getTime();
            String dia = datenow.toString();

            Quotes quotes = new Quotes(null, frase.getText().toString(),
                    author.getText().toString(),
                    dia, categoria,
                    user.getUid(),
                    user.getDisplayName(),
                    String.valueOf(user.getPhotoUrl()),
                    0, backgoundcolor,
                    textcolorstring);
            if (author.getText().toString().equals("")){
                quotes.setAuthor(user.getDisplayName());
            }
            quotesDB = new QuotesDB(quotes, Objects.requireNonNull(getActivity()));
            quotesDB.Inserir();


        }else{
            Snacky.builder().setActivity(Objects.requireNonNull(getActivity())).setText("Escreva algo!");
        }
    }







}
