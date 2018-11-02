package com.creat.motiv.Utils;

import android.content.Context;
import android.graphics.Typeface;

import com.creat.motiv.Beans.Artists;
import com.creat.motiv.Beans.Category;
import com.creat.motiv.Beans.Fonts;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Tools {
    public  static int sixhourinterval = 21600000;
    public static String iconssite[] ={ "https://flaticon.com","https://dribbble.com","https://material.io"};

    public static String artists[] ={
            "Alfrey Davilla",
            "Aleksandar Savic",
            "Alex Dixon",
            "Thunder Rockets",
            "Elena Maykhrych",
            "Ioana Şopov",
            "Agnese Lo",
            "Holden Kao",
            "Gigi Meburishvili",
            "Jess Dove",
            "Judith",
            "Nick Slater",
            "Mohamed Chahin",
            "Alexa Erkaeva",
            "Ilya Polutis",
            "Jordan Jenkins ",
            "Davs Doodles",
            "Brenttton","Smashicons"};

    public static String artistslinks[]
            ={"https://dribbble.com/vaneltia",
            "https://dribbble.com/almigor",
             "https://dribbble.com/alexdixon",
            "https://dribbble.com/thunderockets",
            "https://dribbble.com/elenmay",
            "https://dribbble.com/agneselodesign",
            "https://dribbble.com/ioanasopov",
            "https://dribbble.com/holdenkaodesign",
            "https://dribbble.com/GigiM",
            "https://dribbble.com/judag",
            "https://dribbble.com/slaterdesign",
            "https://dribbble.com/MohChahin",
            "https://dribbble.com/erkaeva",
            "https://dribbble.com/Polutis",
            "https://dribbble.com/jkane",
            "https://dribbble.com/davsdoodles",
            "Anano Miminoshvili",
            "https://dribbble.com/Breton-cn","https://www.flaticon.com/authors/smashicons"

    };

    public static String adId = "ca-app-pub-4979584089010597/3019321601";


    public static ArrayList<Artists> artists(){
        ArrayList<Artists> artistslist = new ArrayList<>();
        for (int i = 0; i <artists.length;i++){
            Artists a = new Artists(artists[i],artistslinks[i]);
            artistslist.add(a);
        }
        return artistslist;
    }



    public static Category categories[] ={new Category("Música","https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/music.gif?alt=media&token=906b5df8-c2dc-4f93-8540-9161073554f9"),
            new Category("Citação","https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/citation.gif?alt=media&token=675413a5-76cc-4112-b63b-d49c7cf890ea"),
            new Category("Amor","https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/love.gif?alt=media&token=21f16353-df2b-4843-9969-b837f8edb688"),
            new Category("Motivação","https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/motivation.gif?alt=media&token=dee84d87-5946-4a4c-8d7a-9e71a170aaa2")

    };


    public static String offlinefrases[] = {"Ish, sem internet você não consegue fazer nada",
            "Parece que você está desconectado. Aproveita e cria uma conexão com alguém",
            "Bom... Parece que você tá sem internet, então acho que você vai ter que aceitar esse gif"
    };
    public static String empyquotes[] = {"Você não vai escrever nada? Tá achando que é festa?",
            "O vazio da sua existência não necessariamente precisa ser o vazio do bloco de texto, escreva algo!",
            "Você não quer ver o feed e ver um texto vazio né? Então por favor escreve algo aí",
            "Eu sei que as vezes você se sente vazio, " +
                    "mas esse bloco de texto te ajuda a mostrar que ainda tem alguma coisa aí, então escreve por favor"
    };
    public static ArrayList<Fonts> fonts(Context context){
        Fonts fontsarchieves[] = {
            new Fonts(Typeface.createFromAsset(context.getAssets(),"fonts/Arvo-Regular_201.ttf"),"Arvo"),
            new Fonts(Typeface.createFromAsset(context.getAssets(),"fonts/Cornerstone.ttf"),"Cornerstone"),
             new Fonts( Typeface.createFromAsset(context.getAssets(),"fonts/times.ttf"),"Times")  ,
             new Fonts(  Typeface.createFromAsset(context.getAssets(),"fonts/MightypeScript.otf"),"Mightype")

        };


        return new ArrayList<>(Arrays.asList(fontsarchieves));
    }

    public  static String emptyquote(){
        int q;
        Random x =new Random( );
        q = x.nextInt(empyquotes.length);
        String quote = empyquotes[q];
        return quote;
    }



    public static String offlinemessage(){
        int q;
        Random x =new Random();
        q = x.nextInt(offlinefrases.length);
        String offline = offlinefrases[q];
        return offline;
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


}
