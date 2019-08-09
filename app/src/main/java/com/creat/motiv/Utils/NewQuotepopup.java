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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.RecyclerColorAdapter;
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

public class NewQuotepopup {
    private RealtimeBlurView realtimeBlurView;
    ImageButton backcolorfab, texcolorfab;

    private RadioButton love;
    private RadioButton citation;
    private RadioButton motivation;
    private RadioButton music;

    private Activity activity;
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
    private RadioGroup categories;
    private Spinner fonts;
    private int f;
    private boolean isfirst = true;
    private RadioGroup categoriesgroup;
    private ImageButton font;
    private ImageButton textcolorfab;
    private LinearLayout options;
    private CircleImageView userpic;
    private TextView username;
    private ImageButton reported;
    private LinearLayout quotedata;
    private EditText quote;
    private Button salvar;
    public NewQuotepopup(Activity activity, RealtimeBlurView blurView) {
        this.activity = activity;
        user = FirebaseAuth.getInstance().getCurrentUser();
        this.realtimeBlurView = blurView;
    }

    public void showup() {
        BottomSheetDialog myDialog = new BottomSheetDialog(activity, R.style.Dialog_No_Border);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(R.layout.newquotepopup);
        myDialog.show();


        categories = myDialog.findViewById(R.id.categories);
        font = myDialog.findViewById(R.id.font);
        backcolorfab = myDialog.findViewById(R.id.backcolorfab);
        texcolorfab = myDialog.findViewById(R.id.textcolorfab);
        backcolorid = myDialog.findViewById(R.id.backcolorid);
        texcolorid = myDialog.findViewById(R.id.texcolorid);
        fontid = myDialog.findViewById(R.id.fontid);
        background = myDialog.findViewById(R.id.background);
        author = myDialog.findViewById(R.id.author);
        frase = myDialog.findViewById(R.id.quote);
        categories = myDialog.findViewById(R.id.categoriesgroup);


        TextView username = myDialog.findViewById(R.id.username);
        CircleImageView userpic = myDialog.findViewById(R.id.userpic);
        this.colorlibrary = myDialog.findViewById(R.id.colorlibrary);
        Button salvar = myDialog.findViewById(R.id.salvar);
        username.setText(user.getDisplayName());
        Glide.with(activity).load(user.getPhotoUrl()).into(userpic);
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
        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                realtimeBlurView.setVisibility(View.GONE);
            }
        });

        font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isfirst) {
                    f++;
                }
                ArrayList<Typeface> fonts = Tools.fonts(activity);

                if (f == fonts.size()) {
                    f = 0;
                }

                frase.setTypeface(fonts.get(f));
                author.setTypeface(fonts.get(f));
                isfirst = false;

            }
        });
    }


//    private void radios() {
//        love.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                categoria = "Amor";
//                popup.setBackgroundResource(R.drawable.bottom_line_love);
//            }
//        });
//
//
//        motivation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                categoria = "Motivação";
//                popup.setBackgroundResource(R.drawable.bottom_line_motivation);
//            }
//        });
//
//
//        music.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                categoria = "Musica";
//                popup.setBackgroundResource(R.drawable.bottom_line_music);
//            }
//        });
//
//        citation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                categoria = "Citação";
//                popup.setBackgroundResource(R.drawable.bottom_line_citation);
//
//            }
//        });
//
//
//    }


    public void showedit(String id) {

        BottomSheetDialog myDialog = new BottomSheetDialog(activity, R.style.Dialog_No_Border);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(R.layout.newquotepopup);
        this.font = myDialog.findViewById(R.id.font);
        myDialog.show();

        love = myDialog.findViewById(R.id.love);
        citation = myDialog.findViewById(R.id.citation);
        motivation = myDialog.findViewById(R.id.motivation);
        music = myDialog.findViewById(R.id.music);
        this.backcolorfab = myDialog.findViewById(R.id.backcolorfab);
        this.texcolorfab = myDialog.findViewById(R.id.textcolorfab);
        this.backcolorid = myDialog.findViewById(R.id.backcolorid);
        this.texcolorid = myDialog.findViewById(R.id.texcolorid);
        this.fontid = myDialog.findViewById(R.id.fontid);
        this.background = myDialog.findViewById(R.id.background);
        this.author = myDialog.findViewById(R.id.author);
        this.frase = myDialog.findViewById(R.id.quote);
        final TextView username = myDialog.findViewById(R.id.username);
        final CircleImageView userpic = myDialog.findViewById(R.id.userpic);
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

        texcolorfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Textocolorpicker();
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
                            fonts.setSelection(quotes.getFont());
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
                                music.setSelected(true);
                                break;
                            case "Citação":
                                popup.setBackgroundResource(R.drawable.bottom_line_citation);
                                citation.setSelected(true);
                                break;
                            case "Amor":
                                popup.setBackgroundResource(R.drawable.bottom_line_love);
                                love.setSelected(true);

                                break;
                            case "Motivação":
                                popup.setBackgroundResource(R.drawable.bottom_line_motivation);
                                motivation.setSelected(true);
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
        if (novo) {
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
        Integer fonti = f;
        if (fontid.getText() != "") {
            fonti = Integer.parseInt(fontid.getText().toString());

        }
        if (texcolorid.getText() == "") {
            texcolorid.setText(String.valueOf(Color.BLACK));
        }
        if (backcolorid.getText() == "") {
            backcolorid.setText(String.valueOf(Color.WHITE));
        }


        Quotes quotes = new Quotes(null,
                frase.getText().toString(),
                author.getText().toString(),
                dia, null,
                user.getUid(),
                user.getDisplayName(),
                String.valueOf(user.getPhotoUrl()),
                Integer.parseInt(backcolorid.getText().toString()),
                Integer.parseInt(texcolorid.getText().toString()),
                false,
                false,
                fonti,
                false);
        System.out.println("font " + quotes.getFont() + "backcolor e textcolor " + quotes.getBackgroundcolor() + " " + quotes.getTextcolor());

        if (author.getText().toString().equals("")) {
            quotes.setAuthor(user.getDisplayName());
        }
        if (user.isEmailVerified()) {
            QuotesDB quotesDB = new QuotesDB(quotes, Objects.requireNonNull(activity));
            quotesDB.Inserir();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Theme_AppCompat_Dialog).setMessage("Email não verificado");
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
        Field[] fields = Class.forName(Objects.requireNonNull(activity).getPackageName() + ".R$color").getDeclaredFields();
        for (Field field : fields) {
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
        RecyclerColorAdapter recyclerColorAdapter = new RecyclerColorAdapter(colors, activity,
                background, frase, author, activity, texcolorid, backcolorid, texcolorfab, backcolorfab);
        recyclerColorAdapter.notifyDataSetChanged();

        colorlibrary.setAdapter(recyclerColorAdapter);
        colorlibrary.setLayoutManager(llm);

    }






}
