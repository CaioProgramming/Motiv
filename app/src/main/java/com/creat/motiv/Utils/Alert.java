package com.creat.motiv.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Adapters.LikeAdapter;
import com.creat.motiv.Adapters.RecyclerCreatorsAdapter;
import com.creat.motiv.Adapters.RecyclerPicAdapter;
import com.creat.motiv.Adapters.RecyclerReferencesAdapter;
import com.creat.motiv.Beans.Developers;
import com.creat.motiv.Beans.Likes;
import com.creat.motiv.Beans.Pics;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.Database.QuotesDB;
import com.creat.motiv.Database.UserDB;
import com.creat.motiv.Fragments.ProfileFragment;
import com.creat.motiv.R;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import de.mateware.snacky.Snacky;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.creat.motiv.Utils.Tools.iconssite;

public class Alert implements Dialog.OnShowListener, Dialog.OnDismissListener {
    public Drawable erroricon, succesicon;
    private RealtimeBlurView blur;
    private Activity activity;
    private int dialogNoBorder = R.style.Dialog_No_Border;
    private int bottomdialogNoBorder = R.style.Bottom_Dialog_No_Border;


    public Alert(Activity activity) {
        this.activity = activity;
        this.blur = activity.findViewById(R.id.rootblur);
        this.erroricon = activity.getDrawable(R.drawable.ic_error);
        this.succesicon = activity.getDrawable(R.drawable.ic_success);
    }

    public void quoteoptions(boolean isfromuser, final Quotes quote) {
        final BottomSheetDialog myDialog = new BottomSheetDialog(activity, bottomdialogNoBorder);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(R.layout.bottom_options);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        myDialog.setOnShowListener(this);
        myDialog.setOnDismissListener(this);
        TextView copy, edit, share, delete, report;
        copy = myDialog.findViewById(R.id.copy);
        edit = myDialog.findViewById(R.id.edit);
        share = myDialog.findViewById(R.id.share);
        delete = myDialog.findViewById(R.id.delete);
        report = myDialog.findViewById(R.id.report);

        if (!isfromuser) {
            edit.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }

        if (copy != null) {
            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.dismiss();
                    ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("frase", quote.getQuote());
                    clipboard.setPrimaryClip(clip);
                    Snacky.builder().setActivity(activity).setBackgroundColor(Color.WHITE).
                            setText("Frase " + quote.getQuote() +
                                    "copiado para área de transferência")
                            .setTextColor(Color.WHITE).build().show();

                }
            });
        }

        if (edit != null) {
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.dismiss();
                    NewQuotepopup newQuotepopup = new NewQuotepopup(activity, blur);
                    newQuotepopup.showedit(quote);
                }
            });
        }

        if (share != null) {
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.dismiss();
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/pain");
                    share.putExtra(Intent.EXTRA_SUBJECT, "Motiv");
                    share.putExtra(Intent.EXTRA_TEXT, quote.getQuote() + " -" + quote.getAuthor());
                    activity.startActivity(Intent.createChooser(share, "Escolha onde quer compartilhar"));
                }
            });
        }

        if (delete != null) {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.dismiss();
                    DatabaseReference raiz;
                    raiz = Tools.quotesreference;
                    raiz.child(quote.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Animation out = AnimationUtils.loadAnimation(activity, R.anim.pop_out);
                            } else {
                                Snacky.builder().setActivity(activity).error().setText("Erro " + task.getException().getMessage()).show();

                            }

                        }
                    });
                }
            });
        }

        if (report != null) {
            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.dismiss();
                    Report(quote);

                }
            });
        }
        myDialog.show();
        Animation in = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
        blur.setVisibility(View.VISIBLE);
        blur.startAnimation(in);


    }


    private void Report(final Quotes quote){
        final Dialog myDialog = new Dialog(activity, dialogNoBorder);
        myDialog.setOnShowListener(this);
        myDialog.setOnDismissListener(this);
        myDialog.setContentView(R.layout.message_dialog);
        myDialog.show();
        ImageView icon = myDialog.findViewById(R.id.icon);
        TextView message = myDialog.findViewById(R.id.message);
        Glide.with(activity).load(activity.getDrawable(R.drawable.flamencodeleteconfirmation)).into(icon);
        message.setText("Opa,opa,opa, uma denúncia? Tem certeza que está frase tem algo inapropriado para a comunidade?");
        Button mButton = myDialog.findViewById(R.id.button);
        mButton.setText("Denunciar");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                QuotesDB quotesDB = new QuotesDB(activity,quote);
                quotesDB.Denunciar();

            }
        });
    }
    private void Ad(){
        final Dialog myDialog = new Dialog(activity, dialogNoBorder);
        myDialog.setOnShowListener(this);
        myDialog.setOnDismissListener(this);
        myDialog.setContentView(R.layout.message_dialog);
        myDialog.show();
        ImageView icon = myDialog.findViewById(R.id.icon);
        TextView message = myDialog.findViewById(R.id.message);
        Glide.with(activity).load(activity.getDrawable(R.drawable.pluto)).into(icon);
        message.setText("Gostaria de nos ajudar vendo um anúncio em vídeo?");
        Button mButton = myDialog.findViewById(R.id.button);
        mButton.setText("Assistir");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                ShowAd();

            }
        });
    }




    public void about() {

        final Dialog myDialog = new Dialog(activity, R.style.AppTheme);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        myDialog.setContentView(R.layout.about_layout);
        final TextView adtext = myDialog.findViewById(R.id.adtext);
        final TextView creators = myDialog.findViewById(R.id.creators);
        final RecyclerView creatorsrecycler = myDialog.findViewById(R.id.creatorsrecycler);
        final RecyclerView designrecycler = myDialog.findViewById(R.id.designrecycler);


        myDialog.show();
        Animation in = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
        blur.setVisibility(View.VISIBLE);
        blur.startAnimation(in);


        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Animation out = AnimationUtils.loadAnimation(activity, R.anim.fade_out);
                blur.startAnimation(out);
                blur.setVisibility(View.GONE);


            }
        });

        adtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                Ad();
            }
        });


        CarregarCreators(creatorsrecycler, new StringBuilder(), creators);
        CarregarReferences(designrecycler);

    }


    public void Picalert(final ProfileFragment pfragment) {
        final ArrayList<Pics> Picslist;

        final RealtimeBlurView blurView = Objects.requireNonNull(activity).findViewById(R.id.rootblur);
        Picslist = new ArrayList<>();
        final BottomSheetDialog myDialog = new BottomSheetDialog(activity, bottomdialogNoBorder);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(R.layout.profilepicselect_);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        final RecyclerView picrecycler;
        CardView layout = myDialog.findViewById(R.id.card);

        layout.setCardBackgroundColor(Color.TRANSPARENT);


        picrecycler = myDialog.findViewById(R.id.picsrecycler);
        DatabaseReference databaseReference = Tools.iconsreference;
        databaseReference.addValueEventListener(new ValueEventListener() {
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
                GridLayoutManager llm = new GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false);
                RecyclerPicAdapter recyclerPicAdapter = new RecyclerPicAdapter(Picslist, activity, pfragment);
                picrecycler.setAdapter(recyclerPicAdapter);
                picrecycler.setLayoutManager(llm);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myDialog.show();
        Animation in = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
        blurView.setVisibility(View.VISIBLE);
        blurView.startAnimation(in);


        myDialog.setOnDismissListener(this);


    }

    public void Likelist(ArrayList<Likes> likes) {
        BottomSheetDialog myDialog = new BottomSheetDialog(activity, bottomdialogNoBorder);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(R.layout.profilepicselect_);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        myDialog.setOnShowListener(this);
        myDialog.setOnDismissListener(this);
        TextView title = myDialog.findViewById(R.id.title);
        RecyclerView likesrecycler = myDialog.findViewById(R.id.picsrecycler);
        ImageView icon = myDialog.findViewById(R.id.icon);
        Glide.with(activity).load(activity.getDrawable(R.drawable.flamenco_done)).into(icon);
        title.setText("Curtidas");
        LikeAdapter likeAdapter = new LikeAdapter(likes, activity);
        GridLayoutManager llm = new GridLayoutManager(activity, 1, LinearLayoutManager.VERTICAL, false);
        likesrecycler.setAdapter(likeAdapter);
        likesrecycler.setLayoutManager(llm);

        myDialog.show();

    }


    public void changename() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Dialog myDialog = new Dialog(activity, dialogNoBorder);
        myDialog.setOnShowListener(this);
        myDialog.setContentView(R.layout.changename);
        myDialog.show();
        final EditText mUsername = myDialog.findViewById(R.id.username);
        mUsername.setHint(user.getDisplayName());
        final Button mButton = myDialog.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButton.setEnabled(false);
                UserDB userDB = new UserDB(activity);
                userDB.changeusername(mUsername.getText().toString());
            }
        });
    }


    private void ShowAd() {
        final RewardedVideoAd rewardedVideoAd;
        final ProgressDialog progressDialog = new ProgressDialog(activity, dialogNoBorder);
        progressDialog.setOnShowListener(this);
        progressDialog.setOnDismissListener(this);

        MobileAds.initialize(activity,
                "ca-app-pub-4979584089010597~4181793255");

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                progressDialog.dismiss();
                rewardedVideoAd.show();
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {


            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Message(erroricon,"Ocorreu um erro carregando o vídeo \uD83D\uDE22 ");
            }

            @Override
            public void onRewardedVideoCompleted() {
                Message(activity.getDrawable(R.drawable.flame_success),"Obrigado pela ajuda, você é demais \uD83D\uDE0D!");

            }
        });
        loadRewardedVideoAd(rewardedVideoAd);

    }

    private void loadRewardedVideoAd(RewardedVideoAd rewardedVideoAd) {
        rewardedVideoAd.loadAd("ca-app-pub-4979584089010597/9410101997",
                new AdRequest.Builder().build());

    }


    private void CarregarCreators(final RecyclerView creatorsrecycler, final StringBuilder devtext, final TextView devs) {
        final ArrayList<Developers> developersArrayList = new ArrayList<>();
        Query aboutdb = FirebaseDatabase.getInstance().getReference().child("Developers");
        aboutdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Developers developers = new Developers();
                    Developers dv = d.getValue(Developers.class);

                    if (dv != null) {
                        developers.setNome(dv.getNome());
                        developers.setBackgif(dv.getBackgif());
                        developers.setPhotouri(dv.getPhotouri());
                        developers.setLinkedin(dv.getLinkedin());
                        developers.setCargo(dv.getCargo());

                        System.out.println("Developer " + developers.getNome() + "   " + " cargo " + developers.getCargo() +
                                "  " + "photo " + developers.getPhotouri() + " linkedin " + developers.getLinkedin() +
                                " backgif " + developers.getBackgif());
                        developersArrayList.add(dv);
                        if (i == 0) {
                            devtext.append("<b>" + developers.getNome() + "</b>");
                        } else {
                            devtext.append(" e <b>" + developers.getNome() + "</b>");

                        }
                        i++;


                    }

                }

                devs.setText(Html.fromHtml(String.valueOf(devtext)));
                RecyclerCreatorsAdapter recyclerCreatorsAdapter = new RecyclerCreatorsAdapter(developersArrayList, activity);
                GridLayoutManager layoutManager = new GridLayoutManager(activity, 1, LinearLayoutManager.HORIZONTAL
                        , false);
                creatorsrecycler.setAdapter(recyclerCreatorsAdapter);
                creatorsrecycler.setHasFixedSize(true);
                creatorsrecycler.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CarregarReferences(RecyclerView designrecycler) {
        ArrayList<String> referencias = new ArrayList<>();
        Collections.addAll(referencias, iconssite);
        Collections.sort(referencias, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareToIgnoreCase(t1);
            }
        });
        RecyclerReferencesAdapter recyclerReferencesAdapter = new RecyclerReferencesAdapter(referencias, activity);
        GridLayoutManager llm = new GridLayoutManager(activity, 1, LinearLayoutManager.HORIZONTAL, false);

        designrecycler.setLayoutManager(llm);
        designrecycler.setHasFixedSize(true);
        designrecycler.setAdapter(recyclerReferencesAdapter);

    }


    public void loading() {
        final ProgressDialog myDialog = new ProgressDialog(activity, R.style.Dialog_No_Border);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setOnShowListener(this);
        myDialog.setOnDismissListener(this);
        myDialog.show();


        CountDownTimer timer = new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                myDialog.dismiss();

            }
        };
        timer.start();

    }

    public void Message(Drawable dicon, String messages) {
        final Dialog myDialog = new Dialog(activity, dialogNoBorder);
        myDialog.setOnShowListener(this);
        myDialog.setContentView(R.layout.message_dialog);
        myDialog.show();
        ImageView icon = myDialog.findViewById(R.id.icon);
        TextView message = myDialog.findViewById(R.id.message);
        Glide.with(activity).load(dicon).into(icon);
        message.setText(messages);
        Button mButton = myDialog.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });


    }


    @Override
    public void onShow(DialogInterface dialogInterface) {
        if (blur != null) {
            Animation in = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
            blur.setVisibility(View.VISIBLE);
            blur.startAnimation(in);
        }

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (blur != null) {
            Animation out = AnimationUtils.loadAnimation(activity, R.anim.fade_out);
            blur.startAnimation(out);
            blur.setVisibility(View.GONE);
        }
    }


}

