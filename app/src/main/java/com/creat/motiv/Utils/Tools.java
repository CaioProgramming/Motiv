package com.creat.motiv.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.creat.motiv.Beans.Artists;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Tools {
    public  static int sixhourinterval = 21600000;
    public static String iconssite[] ={ "https://flaticon.com","https://dribbble.com","https://material.io"};

    public static Artists artist[] ={
        new Artists("M. EDNOKO Marinov","https://dribbble.com/ednoko"),
        new Artists("Cynthia Tizcareno","https://dribbble.com/cynthiatiz"),
        new Artists("Rounded Rectangle","https://dribbble.com/yxarcher"),
        new Artists("Zayn","https://dribbble.com/zaynkylo"),
        new Artists("Nick Slater","https://dribbble.com/slaterdesign"),
        new Artists("Yashika ","https://dribbble.com/amazingdesigner"),
        new Artists("Marta Zubieta","https://dribbble.com/martazubieta"),
        new Artists("Diana Stoyanova","https://dribbble.com/dianaxstoyanova"),
        new Artists("Maryia","https://dribbble.com/kulinskaya"),
        new Artists("Tyler Pate","https://dribbble.com/Ty_poe"),
        new Artists("Chris Fernandez","https://dribbble.com/c_illustrates"),
        new Artists("Andrew Rose","https://dribbble.com/andrewfelix"),
        new Artists("Andrew Rose","https://dribbble.com/andrewfelix"),
        new Artists("https://dribbble.com/andrewfelix","https://dribbble.com/NewhouseDesigns"),
        new Artists("Andrew Rose","https://dribbble.com/andrewfelix"),
        new Artists("Ssilbi NG","https://dribbble.com/ssilbing"),
        new Artists("Jetpacks and Rollerskates","https://dribbble.com/blakestevenson"),
        new Artists("volcanic ash","https://dribbble.com/volcanicash"),
        new Artists("Ksenia Vega","https://dribbble.com/LittleVega"),
        new Artists("Evgeny Polukhin ","https://dribbble.com/Pioneer"),
        new Artists("Rick Calzi","https://dribbble.com/rickcalzi"),
        new Artists("Antonija Golić","https://dribbble.com/SilverAG"),
        new Artists("Li Aladin","https://dribbble.com/Li_Aladin"),
        new Artists("Aleksandar Savic","https://dribbble.com/almigor"),
        new Artists("Jonathan Sullivan","https://dribbble.com/Sk8Gnarley"),
        new Artists("Peter Laudermilch","https://dribbble.com/peterlaudermilch"),
        new Artists("Burnt Toast Creative","https://dribbble.com/BurntToast"),
        new Artists("Thunder Rockets","https://dribbble.com/thunderockets"),
        new Artists("Nick Slater","https://dribbble.com/slaterdesign"),
        new Artists("Alfrey Davilla | vaneltia","https://dribbble.com/vaneltia"),
        new Artists("Brenttton","https://dribbble.com/Breton-cn"),
        new Artists("Mohamed Chahin","https://dribbble.com/MohChahin"),
        new Artists("Alex Dixon","https://dribbble.com/alexdixon"),
        new Artists("Alexa Erkaeva","https://dribbble.com/erkaeva"),
        new Artists("Ilya Polutis","https://dribbble.com/Polutis"),
        new Artists("Jordan Jenkins ","https://dribbble.com/jkane"),
        new Artists("Anano Miminoshvili","https://dribbble.com/Anano"),
        new Artists("Davs Doodles","https://dribbble.com/davsdoodles"),
        new Artists("Kong_Family","https://dribbble.com/godesignme"),
        new Artists("Andrii Nhuien","https://dribbble.com/bassadz"),
        new Artists("Judith","https://dribbble.com/judag"),
        new Artists("Fahiz","https://dribbble.com/Fahiz"),
        new Artists("Gigi Meburishvili","https://dribbble.com/GigiM"),

    };



    public static int spancount = 1;

    public static ArrayList<Artists> artists(){
        ArrayList<Artists> artistslist = new ArrayList<>();
        artistslist.addAll(Arrays.asList(artist));
        Collections.sort(artistslist, new Comparator<Artists>() {
            @Override
            public int compare(Artists artists, Artists t1) {
                return artists.getNome().compareToIgnoreCase(t1.getNome());
            }
        });
        return artistslist;
    }



    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        if (darkness < 0.5) {
            return false; // It's a light color
        } else {
            return true; // It's a dark color
        }
    }




    public static String offlinefrases[] = {"Ish, sem internet você não consegue fazer nada",
            "Parece que você está desconectado. Aproveita e cria uma conexão com alguém",
            "Bom... Parece que você tá sem internet, então acho que você vai ter que aceitar esse gif"
    };


    public static String  frases[] = {"Tudo o que um sonho precisa para ser realizado é alguém que acredite que ele possa ser realizado.",
            "Imagine uma nova história para sua vida e acredite nela.",
            "Ser feliz sem motivo é a mais autêntica forma de felicidade.",
            "Não existe um caminho para a felicidade. A felicidade é o caminho.",
            "A amizade desenvolve a felicidade e reduz o sofrimento, duplicando a nossa alegria e dividindo a nossa dor.",
            "Não espere por uma crise para descobrir o que é importante em sua vida.",
            "Saber encontrar a alegria na alegria dos outros, é o segredo da felicidade.",
            "Perder tempo em aprender coisas que não interessam, priva-nos de descobrir coisas interessantes.",
            "Pessimismo leva à fraqueza, otimismo ao poder."

    };


    public static String empyquotes[] = {"Você não vai escrever nada? Tá achando que é festa?",
            "O vazio da sua existência não necessariamente precisa ser o vazio do bloco de texto, escreva algo!",
            "Você não quer ver o feed e ver um texto vazio né? Então por favor escreve algo aí",
            "Eu sei que as vezes você se sente vazio, " +
                    "mas esse bloco de texto te ajuda a mostrar que ainda tem alguma coisa aí, então escreve por favor"
    };
    public static ArrayList<Typeface> fonts(Context context){
        Typeface fontsarchieves[] = {
                Typeface.createFromAsset(context.getAssets(),"fonts/Arvo-Regular_201.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Audrey-Normal.otf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Cornerstone.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/times.ttf") ,
                Typeface.createFromAsset(context.getAssets(),"fonts/MightypeScript.otf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/AmaticSC-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Amiko-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/BlackHanSans-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Cabin-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Cinzel-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/CinzelDecorative-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Farsan-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/FingerPaint-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/FredokaOne-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Inconsolata-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Lalezar-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Lobster-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Mogra-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Nunito-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/NunitoSans-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Pacifico-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Quicksand-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Rakkas-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Ranga-Regular.ttf"),
                Typeface.createFromAsset(context.getAssets(),"fonts/Rasa-Regular.ttf"),





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
