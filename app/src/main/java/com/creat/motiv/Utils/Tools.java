package com.creat.motiv.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Tools {

    public static String userpicnotfound = "https://image.freepik.com/vetores-gratis/astronauta-em-pe-de-traje-espacial-na-prancha-de-surf-e-surf-em-estrelas-da-via-lactea_1441-3251.jpg";
    public  static int sixhourinterval = 21600000;
    public static String[] iconssite = {"https://flaticon.com", "https://dribbble.com", "https://material.io", "https://undraw.co"};


    public static String path = "Quotes";
    public static String iconpath = "Quotes";
    public static String userpath = "Users";
    public static DatabaseReference quotesreference = FirebaseDatabase.getInstance().getReference(path);
    public static DatabaseReference iconsreference = FirebaseDatabase.getInstance().getReference(iconpath);
    public static DatabaseReference userreference = FirebaseDatabase.getInstance().getReference(userpath);




    public static int spancount = 1;
    public static String searcharg = "\uf8ff";







    private static String[] offlinefrases = {"Sem internet é? Acontece né vá aproveitar a vida",
            "Parece que você está desconectado. Quando você se reconectar eu mostro umas frases",
            "Bom... Parece que você tá sem internet, então acho que não tem oque fazer..."
    };


    private static String[] empyquotes = {"Você não vai escrever nada? Tá achando que é festa?",
            "O vazio da sua existência não necessariamente precisa ser o vazio do bloco de texto, escreva algo!",
            "Você não quer ver o feed e ver um texto vazio né? Então por favor escreve algo aí",
            "Eu sei que as vezes você se sente vazio, " +
                    "mas esse bloco de texto te ajuda a mostrar que ainda tem alguma coisa aí, então escreve por favor"
    };





    public static ArrayList<Typeface> fonts(Context context){
        Typeface[] fontsarchieves = {
                Typeface.createFromAsset(context.getAssets(), "fonts/Arvo-Regular_201.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Audrey-Normal.otf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Cornerstone.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/times.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/MightypeScript.otf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/AmaticSC-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Amiko-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/BlackHanSans-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Cabin-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Cinzel-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/CinzelDecorative-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Farsan-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/FingerPaint-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/FredokaOne-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Inconsolata-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lalezar-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Lobster-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Mogra-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Nunito-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/NunitoSans-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Pacifico-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Rakkas-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Ranga-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(), "fonts/Rasa-Regular.ttf"),


        };


        return new ArrayList<>(Arrays.asList(fontsarchieves));
    }

    public  static String emptyquote(){
        int q;
        Random x =new Random( );
        q = x.nextInt(empyquotes.length);
        return empyquotes[q];
    }



    public static String offlinemessage(){
        int q;
        Random x =new Random();
        q = x.nextInt(offlinefrases.length);
        return offlinefrases[q];
    }

    public static Date convertDate(String dia) {


        //2. Test - Convert Date to Calendar
        //3. Test - Convert Calendar to Date
         DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault() );
        Date result = new Date();

        try {

              result = df.parse(dia);
            System.out.println("post day " + result.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(dia);


        return result;

    }

    public static void load(final ProgressBar progressBar) {
        if (progressBar != null) {
            for (int i = 0; i < 4; i++) {
                progressBar.setProgress(25 * i);
                CountDownTimer timer = new CountDownTimer(1500, 100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
            CountDownTimer timer = new CountDownTimer(1500, 100) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    progressBar.setVisibility(View.GONE);
                }
            }.start();
        }
    }

}
