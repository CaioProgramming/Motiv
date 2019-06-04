package com.creat.motiv.Utils;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.RecyclerColorAdapter;
import com.creat.motiv.Adapters.SpinnerAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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

import static com.creat.motiv.Database.QuotesDB.path;

public class NewQuotepopup implements DialogInterface.OnShowListener, DialogInterface.OnDismissListener {
    private RealtimeBlurView realtimeBlurView;

    public NewQuotepopup(Activity activity, RealtimeBlurView blurView) {
        this.activity = activity;
         user = FirebaseAuth.getInstance().getCurrentUser();
        this.realtimeBlurView = blurView;
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
    private LinearLayout popup;
    ImageButton backcolorfab,texcolorfab;

     private android.support.design.widget.TabLayout categories;
     private Spinner fonts;

     public void showup(){

         BottomSheetDialog myDialog = new BottomSheetDialog(activity, R.style.Dialog_No_Border);
         myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         myDialog.setCanceledOnTouchOutside(true);
         myDialog.setContentView(R.layout.newquotepopup);
         myDialog.show();
         myDialog.setOnShowListener(this);
         myDialog.setOnDismissListener(this);


         categories = myDialog.findViewById(R.id.categories);
         this.popup = myDialog.findViewById(R.id.popup);
         this.backcolorfab = myDialog.findViewById(R.id.backcolorfab);
         this.texcolorfab = myDialog.findViewById(R.id.textcolorfab);
         this.backcolorid = myDialog.findViewById(R.id.backcolorid);
         this.texcolorid = myDialog.findViewById(R.id.texcolorid);
         this.fontid = myDialog.findViewById(R.id.fontid);
         this.background = myDialog.findViewById(R.id.background);
         this.author = myDialog.findViewById(R.id.author);
         this.frase = myDialog.findViewById(R.id.quote);
         this.fonts = myDialog.findViewById(R.id.fonts);
         TextView username = myDialog.findViewById(R.id.username);
         CircleImageView userpic = myDialog.findViewById(R.id.userpic);
         this.colorlibrary = myDialog.findViewById(R.id.colorlibrary);
         Button salvar = myDialog.findViewById(R.id.salvar);
         username.setText(user.getDisplayName());
         Glide.with(activity).load(user.getPhotoUrl()).into(userpic);
         SpinnerAdapter spinnerAdapter = new SpinnerAdapter(activity,frase,author,fontid,fonts);
         fonts.setAdapter(spinnerAdapter);
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

         texcolorfab.setOnClickListener(new View.OnClickListener() {
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
                        popup.setBackgroundResource(R.drawable.bottom_line_love);

                         break;
                     case 1:
                         categoria = "Música";
                         popup.setBackgroundResource(R.drawable.bottom_line_music);

                         break;
                     case 2:
                         categoria = "Citação";
                         popup.setBackgroundResource(R.drawable.bottom_line_citation);

                         break;
                     case 3:
                         categoria = "Motivação";
                         popup.setBackgroundResource(R.drawable.bottom_line_motivation);

                         break;
                     case 4:
                         categoria = "Nenhuma";
                         popup.setBackgroundResource(R.drawable.bottom_line_none);

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
        Tutorial();
        myDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                activity.findViewById(R.id.blur);
            }
        });





     }

    public void showedit(String id) {

        BottomSheetDialog myDialog = new BottomSheetDialog(activity, R.style.Dialog_No_Border);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(R.layout.newquotepopup);
        myDialog.show();
        categories = myDialog.findViewById(R.id.categories);
        this.popup = myDialog.findViewById(R.id.popup);
        this.backcolorfab = myDialog.findViewById(R.id.backcolorfab);
        this.texcolorfab = myDialog.findViewById(R.id.textcolorfab);
        this.backcolorid = myDialog.findViewById(R.id.backcolorid);
        this.texcolorid = myDialog.findViewById(R.id.texcolorid);
        this.fontid = myDialog.findViewById(R.id.fontid);
        this.background = myDialog.findViewById(R.id.background);
        this.author = myDialog.findViewById(R.id.author);
        this.frase = myDialog.findViewById(R.id.quote);
        this.fonts = myDialog.findViewById(R.id.fonts);
        final TextView username = myDialog.findViewById(R.id.username);
        final CircleImageView userpic = myDialog.findViewById(R.id.userpic);
        this.colorlibrary = myDialog.findViewById(R.id.colorlibrary);
        Button salvar = myDialog.findViewById(R.id.salvar);
        username.setText(user.getDisplayName());
        Glide.with(activity).load(user.getPhotoUrl()).into(userpic);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(activity, frase, author, fontid, fonts);
        fonts.setAdapter(spinnerAdapter);
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

        texcolorfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Textocolorpicker();
            }
        });



        categories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        categoria = "Amor";
                        popup.setBackgroundResource(R.drawable.bottom_line_love);

                        break;
                    case 1:
                        categoria = "Música";
                        popup.setBackgroundResource(R.drawable.bottom_line_music);

                        break;
                    case 2:
                        categoria = "Citação";
                        popup.setBackgroundResource(R.drawable.bottom_line_citation);

                        break;
                    case 3:
                        categoria = "Motivação";
                        popup.setBackgroundResource(R.drawable.bottom_line_motivation);

                        break;
                    case 4:
                        categoria = "Nenhuma";
                        popup.setBackgroundResource(R.drawable.bottom_line_none);

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
        Tutorial();
        myDialog.setOnShowListener(this);
        LoadQuote(id, username, userpic, salvar);
        myDialog.show();


    }


    public void atualizar(Quotes quote) {
        Integer fonti = null;
        if (fontid.getText() != "") {
            fonti = Integer.parseInt(fontid.getText().toString());
        }
        if (author.getText().toString().equals("")) {
            quote.setAuthor(user.getDisplayName());
        }

        quote.setQuote(frase.getText().toString());
        quote.setAuthor(author.getText().toString());
        quote.setFont(fonti);
        quote.setBackgroundcolor(Integer.parseInt(backcolorid.getText().toString()));
        quote.setTextcolor(Integer.parseInt(texcolorid.getText().toString()));
        quotesDB = new QuotesDB(activity, quote);
        quotesDB.Editar();


    }


    private void LoadQuote(String id, final TextView username, final CircleImageView userpic, final Button salvar) {
        Query quotesdb = FirebaseDatabase.getInstance().getReference().child(path).orderByChild("id").startAt(id).endAt(id + "\uf8ff");


        quotesdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    final Quotes quotes = new Quotes();
                    Quotes q = d.getValue(Quotes.class);
                    if (q != null) {
                        quotes.setCategoria(q.getCategoria());
                        quotes.setUserphoto(q.getUserphoto());
                        quotes.setUsername(q.getUsername());
                        quotes.setUserID(q.getUserID());
                        quotes.setBackgroundcolor(q.getBackgroundcolor());
                        quotes.setAuthor(q.getAuthor());
                        quotes.setQuote(q.getQuote());
                        quotes.setData(q.getData());
                        quotes.setBold(q.isBold());
                        quotes.setItalic(q.isItalic());
                        quotes.setTextcolor(q.getTextcolor());
                        quotes.setBackgroundcolor(q.getBackgroundcolor());
                        quotes.setFont(q.getFont());
                        quotes.setReport(q.isReport());
                        //quoteID.setText(quotes.getId());
                        frase.setText(quotes.getQuote());
                        author.setText(quotes.getAuthor());
                        username.setText(quotes.getUsername());
                        background.setBackgroundColor(quotes.getBackgroundcolor());

                        frase.setTextColor(quotes.getTextcolor());
                        if (quotes.getFont() != null) {
                            frase.setTypeface(Tools.fonts(activity).get(quotes.getFont()));
                            author.setTypeface(Tools.fonts(activity).get(quotes.getFont()));
                        } else {
                            frase.setTypeface(Typeface.DEFAULT);
                            author.setTypeface(Typeface.DEFAULT);
                        }
                        author.setTextColor(quotes.getTextcolor());
                        texcolorid.setText(String.valueOf(quotes.getTextcolor()));
                        backcolorid.setText(String.valueOf(quotes.getBackgroundcolor()));
                        fontid.setText(String.valueOf(quotes.getFont()));
                        switch (quotes.getCategoria()) {
                            case "Musica":
                                popup.setBackgroundResource(R.drawable.bottom_line_music);

                                break;
                            case "Citação":
                                popup.setBackgroundResource(R.drawable.bottom_line_citation);
                                break;
                            case "Amor":
                                popup.setBackgroundResource(R.drawable.bottom_line_love);
                                break;
                            case "Motivação":
                                popup.setBackgroundResource(R.drawable.bottom_line_motivation);
                                break;
                            case "Nenhum":
                                popup.setBackgroundResource(R.drawable.bottom_line_none);

                                break;
                        }


                        Glide.with(activity).load(quotes.getUserphoto()).into(userpic);
                        salvar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                atualizar(quotes);
                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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


    private void salvar() {
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
                background,frase,author,activity,texcolorid,backcolorid,texcolorfab,backcolorfab);
        recyclerColorAdapter.notifyDataSetChanged();

        colorlibrary.setAdapter(recyclerColorAdapter);
        colorlibrary.setLayoutManager(llm);

    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        realtimeBlurView.setVisibility(View.GONE);

    }

    @Override
    public void onShow(DialogInterface dialog) {
        realtimeBlurView.setBlurRadius(20);

    }
}
