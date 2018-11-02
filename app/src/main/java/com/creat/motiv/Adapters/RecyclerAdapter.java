package com.creat.motiv.Adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.EditActivity;
import com.creat.motiv.R;
import com.creat.motiv.Utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.mateware.snacky.Snacky;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.creat.motiv.Database.QuotesDB.path;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>  {
    private Context mContext;
    private List<Quotes> mData;
    private Activity mActivity;



    public RecyclerAdapter( Context mContext, List<Quotes> mData,  Activity mActivity) {
        this.mContext = mContext;
        this.mData = mData;
        this.mActivity = mActivity;
     }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.quotescard,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator.hasVibrator()) {
                    long[] mVibratePattern = new long[]{100, 150};

                    vibrator.vibrate(mVibratePattern,-1); // for 500 ms
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (mData.get(position).getUserID().equals(user.getUid())){
                     Usermenu(holder, position);
                }else{
                PopupMenu popup = new PopupMenu(mContext, holder.menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.quotesoptions);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        switch (item.getItemId()){
                        case R.id.compartilhar:
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/pain");
                            share.putExtra(Intent.EXTRA_SUBJECT,"My app");
                            share.putExtra(Intent.EXTRA_TEXT,mData.get(position).getQuote() + " -" +mData.get(position).getAuthor());
                            mContext.startActivity(Intent.createChooser(share,"Escolha onde quer compartilhar"));
                            return true;
                        case R.id.denunciar:
                            Snacky.builder().setActivity(mActivity)
                                    .setText("Em desenvolvimento")
                                    .setBackgroundColor(Color.BLACK)
                                    .setTextColor(Color.WHITE).build().show();
                            case R.id.Copiar:
                                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("frase", mData.get(position).getQuote());
                                clipboard.setPrimaryClip(clip);
                                Snacky.builder().setActivity(mActivity).setBackgroundColor(Color.BLACK).
                                        setText("Frase " + mData.get(position).getQuote() +
                                                "copiado para área de transferência").build().show();

                                return true;
                        default:
                            return false;


                        }

                    }
                });

                popup.show();}
            }
        });
        Animation in = AnimationUtils.loadAnimation(mContext,R.anim.pop_in);
        holder.cardView.startAnimation(in);
        System.out.println("Quote " +mData.get(position).getQuote()+ " selected font: " + mData.get(position).getFont());
         if (mData.get(position).getFont() != null){
            holder.quote.setTypeface(Tools.fonts(mContext).get(mData.get(position).getFont()).getFont());
            holder.author.setTypeface(Tools.fonts(mContext).get(mData.get(position).getFont()).getFont());


        }else{
            holder.quote.setTypeface(Typeface.DEFAULT);
            holder.author.setTypeface(Typeface.DEFAULT);
         }
        Date postdia = Tools.convertDate(mData.get(position).getData() );
         Date now = Calendar.getInstance().getTime();

        Integer dayCount = (int) ((now.getTime() - postdia.getTime())/1000/60/60/24);
        if (dayCount <= 1){
            holder.dia.setText("Hoje");
        if (dayCount == 1){
            holder.dia.setText("Ontem");
        }
        }else{ holder.dia.setText("Há "   + dayCount + " dias");}
        System.out.println("Day counter " + dayCount);
        if (dayCount >= 7){
        if (dayCount % 7 == 0){
            dayCount = dayCount/7;
            if (dayCount == 1){
                holder.dia.setText("Há " + dayCount + " semana");
            }else{
                holder.dia.setText("Há " + dayCount + " semanas");
            }
        }
        }
        if (dayCount >= 30){
        if (dayCount % 30 == 0){
            dayCount = dayCount/30;
            if (dayCount == 1){
                holder.dia.setText("Há " + dayCount + " mês");
            }else{
                holder.dia.setText("Há " + dayCount + " meses");
            }
            }
        }

        if (mData.get(position).isBold()){
            holder.quote.setTypeface(holder.quote.getTypeface(), Typeface.BOLD);
            holder.author.setTypeface(holder.quote.getTypeface(), Typeface.BOLD);


        }
        if (mData.get(position).isItalic()){
            holder.quote.setTypeface(holder.quote.getTypeface(), Typeface.ITALIC);
            holder.author.setTypeface(holder.quote.getTypeface(), Typeface.ITALIC);
        }

        if (mData.get(position).isItalic() && mData.get(position).isBold()){
            holder.quote.setTypeface(holder.quote.getTypeface(), Typeface.BOLD_ITALIC);
            holder.author.setTypeface(holder.quote.getTypeface(), Typeface.BOLD_ITALIC);
        }


        if (mData.get(position).getUsername() != null || mData.get(position).getUserphoto() != null ){
            System.out.println(mData.get(position).getUserphoto());
            System.out.println(mData.get(position).getUsername());
            Glide.with(mContext).load(mData.get(position).getUserphoto()).into(holder.userpic);
            holder.username.setText(mData.get(position).getUsername());
        }else {
            holder.username.setVisibility(View.INVISIBLE);
            holder.userpic.setVisibility(View.INVISIBLE);

        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (mData.get(position).getUserID().equals(user.getUid())){
                holder.remove.setVisibility(View.VISIBLE);
            }else{
                holder.remove.setVisibility(View.INVISIBLE);
            }
        Animation faAnimation = AnimationUtils.loadAnimation(mContext,R.anim.fade_in);
            if (mData.get(position).getBackgroundcolor() != 0){
                holder.back.setBackgroundColor(mData.get(position).getBackgroundcolor());

            }
            if (mData.get(position).getTextcolor() != 0){
                holder.quote.setTextColor(mData.get(position).getTextcolor());
                holder.author.setTextColor(mData.get(position).getTextcolor());
            }
            Animation animationtext = AnimationUtils.loadAnimation(mContext,R.anim.fade_in);
            holder.quote.setText(mData.get(position).getQuote());
            holder.quote.startAnimation(animationtext);
            holder.author.startAnimation(animationtext);
            holder.author.setText(mData.get(position).getAuthor());
            holder.quote.startAnimation(faAnimation);
            holder.author.startAnimation(faAnimation);
        switch (mData.get(position).getCategoria()) {
            case "Musica":
                holder.category.setBackgroundResource(R.color.purple_300);

                break;
            case "Citação":
                holder.category.setBackgroundResource(R.color.grey_300);
                break;
            case "Amor":
                holder.category.setBackgroundResource(R.color.red_300);

                break;
            case "Motivação":
                holder.category.setBackgroundResource(R.color.orange_300);

                break;
            case "Nenhum":
                holder.category.setBackgroundResource(R.color.black);

                break;
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation bottom = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_bottom);
                Animation top = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_top);

                Animation bottomout = AnimationUtils.loadAnimation(mContext,R.anim.slide_out_bottom);
                Animation topout = AnimationUtils.loadAnimation(mContext,R.anim.slide_out_top);

                if (holder.quotedata.getVisibility() == View.GONE && holder.userdata.getVisibility() == View.GONE){
                    holder.quotedata.setVisibility(View.VISIBLE);
                    holder.userdata.setVisibility(View.VISIBLE);
                    holder.quotedata.startAnimation(bottom);
                    holder.userdata.startAnimation(top);
                }else{
                    holder.quotedata.startAnimation(bottomout);
                    holder.userdata.startAnimation(topout);
                    holder.quotedata.setVisibility(View.GONE);
                    holder.userdata.setVisibility(View.GONE);

                }
            }
        });



            holder.remove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Remover(position, holder);

                }
            });












    }

    private void Usermenu(@NonNull MyViewHolder holder, final int position) {
        PopupMenu popup = new PopupMenu(mContext,holder.menu);
        popup.inflate(R.menu.quotesoptions_user);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.compartilhar:
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/pain");
                        share.putExtra(Intent.EXTRA_SUBJECT,"My app");
                        share.putExtra(Intent.EXTRA_TEXT,mData.get(position).getQuote() + " -" +mData.get(position).getAuthor());
                        mContext.startActivity(Intent.createChooser(share,"Escolha onde quer compartilhar"));
                        return true;
                    case R.id.denunciar:
                        Snacky.builder().setActivity(mActivity)
                                .setText("Em desenvolvimento")
                                .setBackgroundColor(Color.BLACK)
                                .setTextColor(Color.WHITE).build().show();
                    case R.id.Copiar:
                        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("frase", mData.get(position).getQuote());
                        clipboard.setPrimaryClip(clip);
                        Snacky.builder().setActivity(mActivity).setBackgroundColor(Color.BLACK).
                                setText("Frase " + mData.get(position).getQuote() +
                                        "copiado para área de transferência").build().show();

                        return true;
                    default:
                        return false;

                    case R.id.editar:
                        Intent e = new Intent(mActivity, EditActivity.class);
                        e.putExtra("id",mData.get(position).getUserID());
                        e.putExtra("quote",mData.get(position).getQuote());
                        e.putExtra("author",mData.get(position).getAuthor());
                        e.putExtra("texcolor",mData.get(position).getTextcolor());
                        e.putExtra("backcolor",mData.get(position).getBackgroundcolor());
                        e.putExtra("font",mData.get(position).getFont());
                        e.putExtra("italic",mData.get(position).isBold());
                        e.putExtra("bold",mData.get(position).isItalic());
                        e.putExtra("data",mData.get(position).getData());
                        e.putExtra("likes",mData.get(position).getLikes());
                        e.putExtra("categoria",mData.get(position).getCategoria());
                        e.putExtra("userid",mData.get(position).getUserID());

                        mActivity.startActivity(e);

                        return true;


                }

            }
        });
        popup.show();
    }

    public boolean isColorDark(int color){
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }


    private void Remover(final int position, @NonNull final MyViewHolder holder) {
        DatabaseReference raiz;
        raiz = FirebaseDatabase.getInstance().getReference(path);
        raiz.child(mData.get(position).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                Animation out = AnimationUtils.loadAnimation(mContext, R.anim.pop_out);
                holder.cardView.startAnimation(out);
                holder.cardView.setVisibility(View.INVISIBLE);
                }else {
                    Snacky.builder().setActivity(mActivity).error().setText("Erro " + task.getException().getMessage()).show();

                }

            }
        });
    }


    @Override
    public int getItemCount() {
        if(mData.size() == 0){
            return 0;

        }else{
            return mData.size();}
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        CheckBox like;
        ImageButton remove,menu;
         ImageView userpic;
        TextView quote,author,username,dia;
        LinearLayout back;
        LinearLayout content,category;
        RelativeLayout userdata,quotedata;


        public MyViewHolder(View view) {
            super(view);
            userdata = view.findViewById(R.id.userdata);
            quotedata = view.findViewById(R.id.quotedata);
            menu = view.findViewById(R.id.menu);
            dia = view.findViewById(R.id.dia);
            like = view.findViewById(R.id.like);
            remove = view.findViewById(R.id.remove);
            quote = view.findViewById(R.id.quote);
            author = view.findViewById(R.id.author);
            cardView = view.findViewById(R.id.card);
            back = view.findViewById(R.id.background);
            content = view.findViewById(R.id.content);
            category = view.findViewById(R.id.category);
            username = view.findViewById(R.id.username);
            userpic = view.findViewById(R.id.userpic);





        }
    }
}
