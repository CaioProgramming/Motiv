package com.creat.motiv.Fragments;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
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
import com.creat.motiv.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

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
    private android.support.v7.widget.CardView card;
    public NewQuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         user = FirebaseAuth.getInstance().getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_newquote, container, false);

         username = view.findViewById(R.id.username);
         userpic = view.findViewById(R.id.userpic);
         username.setText(user.getDisplayName());
         Glide.with(this).load(user.getPhotoUrl()).into(userpic);
        preview = view.findViewById(R.id.card);
        background  = view.findViewById(R.id.background);
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





         textcolor.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 final ColorPicker cp = new ColorPicker(getActivity());
                 cp.show();
                 cp.enableAutoClose();
                 cp.setCallback(new ColorPickerCallback() {
                     @Override
                     public void onColorChosen(int color) {
                         Log.d("Pure Hex", Integer.toHexString(color));
                         int colorFrom = frase.getCurrentTextColor();
                         ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, color);
                         colorAnimation.setDuration(2000); // milliseconds
                         colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                             @Override
                             public void onAnimationUpdate(ValueAnimator animator) {
                                 frase.setTextColor((int) animator.getAnimatedValue());
                                 author.setTextColor((int) animator.getAnimatedValue());

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
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), category.getSolidColor(), R.color.md_deep_purple_300);
                colorAnimation.setDuration(2000); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        category.setBackgroundColor((int) animator.getAnimatedValue());

                    }

                });
                colorAnimation.start();
                categoria = "Musica";
            }
        });

        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), category.getSolidColor(), R.color.md_red_300);
                colorAnimation.setDuration(2000); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        category.setBackgroundColor((int) animator.getAnimatedValue());

                    }

                });
                colorAnimation.start();
                categoria = "Amor";
            }
        });

        citation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), category.getSolidColor(), R.color.md_grey_300);
                colorAnimation.setDuration(2000); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        category.setBackgroundColor((int) animator.getAnimatedValue());

                    }

                });
                colorAnimation.start();
                categoria = "Citação";
            }
        });

        motivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), category.getSolidColor(), R.color.md_orange_300);
                colorAnimation.setDuration(2000); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        category.setBackgroundColor((int) animator.getAnimatedValue());

                    }

                });
                colorAnimation.start();
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
