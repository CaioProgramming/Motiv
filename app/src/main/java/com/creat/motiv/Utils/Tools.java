package com.creat.motiv.Utils;

import android.content.Context;
import android.graphics.Typeface;

import com.creat.motiv.Beans.Artists;
import com.creat.motiv.Beans.Category;

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
            "Anano Miminoshvili",
            "Brenttton",
            "Smashicons",
            "cheesano",
            "Burnt Toast Creative",
            "Peter Laudermilch ",
            "Jonathan Sullivan",
            "Li Aladin",
            "Antonija Golić",
            "Rick Calzi",
            "Evgeny Polukhin",
            "Andrii Nhuien",
            "Nastia Zubova",
            "Mario Nikolic",
            "Camille Magnan",
            "Cami",
            "Tushar Saini",
            "Vy Tat",
            "Prajwal Mangrulkar"

    };

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
            "https://dribbble.com/Anano",
            "https://dribbble.com/Breton-cn",
            "https://www.flaticon.com/authors/smashicons",
            "https://dribbble.com/cheesano",
            "https://dribbble.com/BurntToast",
            "https://dribbble.com/peterlaudermilch",
            "https://dribbble.com/Sk8Gnarley",
            "https://dribbble.com/Li_Aladin",
            "https://dribbble.com/SilverAG",
            "https://dribbble.com/rickcalzi",
            "https://dribbble.com/Pioneer",
            "https://dribbble.com/bassadz",
            "https://dribbble.com/mynameisnastia",
            "https://dribbble.com/mariomovement",
            "https://dribbble.com/camillemagnan",
            "https://dribbble.com/camidobrin",
            "https://dribbble.com/tusharsainii",
            "https://dribbble.com/mooandidesign",
            "https://dribbble.com/prajwalmangrulkar"

    };
    public static String loader = "https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/astrodribble.gif?alt=media&token=65f55a1b-0e3b-4d1f-a015-cd6c46b0b125";

    public static String adId = "ca-app-pub-4979584089010597/3019321601";


    public static ArrayList<Artists> artists(){
        System.out.println("artists " + artists.length + " with " + artistslinks.length + " links");
        ArrayList<Artists> artistslist = new ArrayList<>();
        for (int i = 0; i <artistslinks.length;i++){
            Artists a = new Artists(artists[i], artistslinks[i]);
            artistslist.add(a);
            System.out.println("artist " + artists[i] + " link " + artistslinks[i]);
        }
        return artistslist;
    }



    public static Category categories[] ={
            new Category("Música","https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/categories%2Flazy_wednesday_d_2x.png?alt=media&token=e22443c8-5ac0-4501-9597-a83d550f3daa"),
            new Category("Citação","https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/categories%2Fcopywritter.jpg?alt=media&token=2fb62421-55da-403f-b16a-16f8ec88384f"),
            new Category("Amor","https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/categories%2Fartboard_2x.png?alt=media&token=6b28e4c1-bfa1-4dc7-aa1b-858c3fd85bba"),
            new Category("Motivação","https://firebasestorage.googleapis.com/v0/b/motiv-d16d1.appspot.com/o/categories%2F3.jpg?alt=media&token=ed80018b-1101-4b58-8fe0-d2cebb2ddfec")

    };


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
