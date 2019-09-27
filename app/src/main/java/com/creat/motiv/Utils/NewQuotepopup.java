package com.creat.motiv.Utils;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.RecyclerColorAdapter;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.R;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
    private RealtimeBlurView blur;
    private ImageButton backcolorfab, texcolorfab;


    private Activity activity;
    private FirebaseUser user;
    private QuotesDB quotesDB;
    private Dialog m_dialog;

    private Pref preferences;
    boolean tuto = true;
    private RecyclerView colorlibrary;
    private EditText frase;
    private EditText author;
    private LinearLayout background;
    private TextView fontid;
    private TextView texcolorid;
    private TextView backcolorid;
    private int f;
    private boolean isfirst = true;
    private TextView font;
    public NewQuotepopup(Activity activity, RealtimeBlurView blurView) {
        this.activity = activity;
        user = FirebaseAuth.getInstance().getCurrentUser();
        this.blur = blurView;
    }

    public void showup() {
        final BottomSheetDialog myDialog = new BottomSheetDialog(activity);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(R.layout.newquotepopup);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        myDialog.show();
        font = myDialog.findViewById(R.id.font);
        backcolorfab = myDialog.findViewById(R.id.backcolorfab);
        texcolorfab = myDialog.findViewById(R.id.textcolorfab);
        backcolorid = myDialog.findViewById(R.id.backcolorid);
        texcolorid = myDialog.findViewById(R.id.texcolorid);
        fontid = myDialog.findViewById(R.id.fontid);
        background = myDialog.findViewById(R.id.background);
        author = myDialog.findViewById(R.id.author);
        frase = myDialog.findViewById(R.id.quote);


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
                myDialog.dismiss();
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
                        salvar();
                        return true;
                    }

                }
                return false; // pass on to other listeners.

            }
        });
        Tutorial();
        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Animation out = AnimationUtils.loadAnimation(activity, R.anim.fade_out);
                blur.startAnimation(out);
                blur.setVisibility(View.GONE);


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
                font.setTypeface(fonts.get(f));
                frase.setTypeface(fonts.get(f));
                author.setTypeface(fonts.get(f));
                isfirst = false;

            }
        });
        Animation in = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
        blur.setVisibility(View.VISIBLE);
        blur.startAnimation(in);
    }


    void showedit(final Quotes quote) {

        final BottomSheetDialog myDialog = new BottomSheetDialog(activity);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(R.layout.newquotepopup);
        this.font = myDialog.findViewById(R.id.font);
        myDialog.show();
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




        Tutorial();
        LoadQuote(username,userpic,quote);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizar(quote);
                myDialog.dismiss();
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
                font.setTypeface(fonts.get(f));
                frase.setTypeface(fonts.get(f));
                author.setTypeface(fonts.get(f));
                isfirst = false;

            }
        });

        myDialog.show();
        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Animation out = AnimationUtils.loadAnimation(activity, R.anim.fade_out);
                blur.startAnimation(out);
                blur.setVisibility(View.GONE);
                SwipeRefreshLayout refreshLayout = activity.findViewById(R.id.refresh);
                refreshLayout.setRefreshing(true);


            }
        });


    }


    private void atualizar(Quotes quote) {
        Integer fonti = f;

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
        SwipeRefreshLayout refreshLayout = activity.findViewById(R.id.refresh);
        refreshLayout.setRefreshing(true);



    }


    private void LoadQuote( final TextView username, final CircleImageView userpic, Quotes quotes) {
        //quoteID.setText(quotes.getId());
        frase.setText(quotes.getQuote());
        author.setText(quotes.getAuthor());
        username.setText(quotes.getUsername());
        background.setBackgroundColor(quotes.getBackgroundcolor());

        frase.setTextColor(quotes.getTextcolor());
        if (quotes.getFont() != null) {
            frase.setTypeface(Tools.fonts(activity).get(quotes.getFont()));
            author.setTypeface(Tools.fonts(activity).get(quotes.getFont()));
            font.setTypeface(Tools.fonts(activity).get(quotes.getFont()));
            f = quotes.getFont();
        } else {
            frase.setTypeface(Typeface.DEFAULT);
            author.setTypeface(Typeface.DEFAULT);

        }
        author.setTextColor(quotes.getTextcolor());
        texcolorid.setText(String.valueOf(quotes.getTextcolor()));
        backcolorid.setText(String.valueOf(quotes.getBackgroundcolor()));
        backcolorfab.setImageTintList(ColorStateList.valueOf(quotes.getBackgroundcolor()));
        texcolorfab.setImageTintList(ColorStateList.valueOf(quotes.getTextcolor()));

        fontid.setText(String.valueOf(quotes.getFont()));
        Glide.with(activity).load(quotes.getUserphoto()).into(userpic);

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
                backcolorfab.setImageTintList(ColorStateList.valueOf(color));

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
                texcolorfab.setImageTintList(ColorStateList.valueOf(color));
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
