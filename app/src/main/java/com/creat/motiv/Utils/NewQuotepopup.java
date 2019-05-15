package com.creat.motiv.Utils;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.RecyclerColorAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;

public class NewQuotepopup {
    public NewQuotepopup(Activity activity) {
        this.activity = activity;
         user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private Activity activity;
    String categoria = "Nenhum";
    FirebaseUser user;
    QuotesDB quotesDB;
    private Dialog m_dialog;

    Pref preferences;
    boolean tuto = true;
    private RecyclerView colorlibrary;
    private EditText frase;
    private EditText author;
    private LinearLayout background;
    private TextView fontid;
    private TextView texcolorid;
    private TextView backcolorid;
     private android.support.design.widget.TabLayout categories;


     public void showup(){

         LayoutInflater inflater = LayoutInflater.from(activity);
         BottomSheetDialog myDialog = new BottomSheetDialog(activity);
         myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         myDialog.setContentView(R.layout.settings);
         myDialog.setCanceledOnTouchOutside(true);
         myDialog.setContentView(R.layout.newquotepopup);
         myDialog.show();
         categories = myDialog.findViewById(R.id.categories);
         ImageButton backcolorfab = myDialog.findViewById(R.id.backcolorfab);
         ImageButton textcolorfab = myDialog.findViewById(R.id.textcolorfab);
         this.backcolorid = myDialog.findViewById(R.id.backcolorid);
         this.texcolorid = myDialog.findViewById(R.id.texcolorid);
         this.fontid = myDialog.findViewById(R.id.fontid);
         this.background = myDialog.findViewById(R.id.background);
         this.author = myDialog.findViewById(R.id.author);
         this.frase = myDialog.findViewById(R.id.quote);
         TextView username = myDialog.findViewById(R.id.username);
         CircleImageView userpic = myDialog.findViewById(R.id.userpic);
         this.colorlibrary = myDialog.findViewById(R.id.colorlibrary);
         Button salvar = myDialog.findViewById(R.id.salvar);
         username.setText(user.getDisplayName());
         Glide.with(activity).load(user.getPhotoUrl()).into(userpic);



         //theme();
         try {
             colorgallery();
         } catch (ClassNotFoundException e) {
             e.printStackTrace();
         } catch (IllegalAccessException e) {
             e.printStackTrace();
         }

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




         categories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
             @Override
             public void onTabSelected(TabLayout.Tab tab) {
                 switch (tab.getPosition()) {
                     case 0:
                         categoria = "Amor";
                         break;
                     case 1:
                         categoria = "Música";
                         break;
                     case 2:
                         categoria = "Citação";

                         break;
                     case 3:
                         categoria = "Motivação";

                         break;
                     case 4:
                         categoria = "Nenhuma";
                         break;
                     default:
                         categoria = "Nenhuma";
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






     }


    public void configure(View view){
         user = FirebaseAuth.getInstance().getCurrentUser();





        try {
            colorgallery();
        } catch (ClassNotFoundException | IllegalAccessException e) {
            System.out.println("erro ao carregar cores" +  e.getCause());
        }

        Tutorial();



    }

    private void BackColorpicker() {
        final ColorPicker cp = new ColorPicker(activity);
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
        final ColorPicker cp = new ColorPicker(activity);
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


    private void Tutorial() {
        Boolean novo = Objects.requireNonNull(Objects.requireNonNull(activity).getIntent().getExtras()).getBoolean("novo");
        if (novo){
            Pref preferences = new Pref(Objects.requireNonNull(activity));
            if (!preferences.writetutorialstate()) {
                preferences.setWriteTutorial(true);
                Info.tutorial(activity.getString(R.string.new_quoteintro), activity);
            }

            //getData();
        }
        tuto = false;
    }


    public void salvar() {
        agree();
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
           QuotesDB quotesDB = new QuotesDB(quotes, Objects.requireNonNull(activity));
            quotesDB.Inserir();
        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(activity).setMessage("Email não verificado");
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



    private void agree() {
        if (activity == null) throw new AssertionError();
        preferences = new Pref(activity);
        m_dialog = new Dialog(activity);
        Animation in = AnimationUtils.loadAnimation(activity, R.anim.slide_in_top);
        LayoutInflater m_inflater = LayoutInflater.from(activity);
        View m_view = m_inflater.inflate(R.layout.politics, null);
        m_dialog.setContentView(m_view);
        Button agreebutton = m_view.findViewById(R.id.agreebutton);
        agreebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_dialog.dismiss();
                preferences.setAgree(true);

            }
        });
        m_view.startAnimation(in);
        if (!preferences.agreestate()) {
            Snacky.builder().setActivity(activity).warning()
                    .setText("Você precisa concordar com os termos de uso para usar o aplicativo!")
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            m_dialog.show();
                        }
                    }).show();
        }
        m_dialog.setCanceledOnTouchOutside(false);
        m_dialog.setCancelable(false);
    }


    private void colorgallery() throws ClassNotFoundException, IllegalAccessException {
        ArrayList<Integer> colors = new ArrayList<>();
        Field[] fields = Class.forName(Objects.requireNonNull(activity).getPackageName() +".R$color").getDeclaredFields();
        for(Field field : fields) {
            String colorName = field.getName();
            int colorId = field.getInt(null);
            int color = activity.getResources().getColor(colorId);
            System.out.println("color " + colorName + " " + color);
            colors.add(color);

        }

        System.out.println("Load " + colors.size() + " colors");
        Collections.reverse(colors);
        colorlibrary.setHasFixedSize(true);
        GridLayoutManager llm = new GridLayoutManager(activity, 3, GridLayoutManager.HORIZONTAL, false);
        RecyclerColorAdapter recyclerColorAdapter = new RecyclerColorAdapter(colors,activity,
                background,frase,author,activity,texcolorid,backcolorid);
        recyclerColorAdapter.notifyDataSetChanged();

        colorlibrary.setAdapter(recyclerColorAdapter);
        colorlibrary.setLayoutManager(llm);

    }



}
