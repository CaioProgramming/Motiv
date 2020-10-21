package com.creat.motiv.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import com.creat.motiv.R
import com.creat.motiv.model.Beans.Artists
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Tools {

    internal var iconssite = arrayOf("https://flaticon.com", "https://dribbble.com", "https://material.io", "https://undraw.co", "https://mixkit.co", "https://icons8.com/ouch/")


    var path = "Quotes"
    var iconpath = "images"
    var userpath = "Users"
    var quotesreference = FirebaseDatabase.getInstance().reference.child(path)
    var iconsreference = FirebaseDatabase.getInstance().reference.child(iconpath)
    var userreference = FirebaseDatabase.getInstance().reference.child(userpath)


    var spancount = 1
    var searcharg = "\uf8ff"

    fun fadeIn(view: View, duration: Long): Completable {
        val animationSubject = CompletableSubject.create()
        return animationSubject.doOnSubscribe {
            ViewCompat.animate(view)
                    .setDuration(duration)
                    .alpha(1f)
                    .withEndAction {
                        animationSubject.onComplete()
                    }
        }


    }

    fun fadeOut(view: View, duration: Long): Completable {
        val animationSubject = CompletableSubject.create()
        return animationSubject.doOnSubscribe {
            ViewCompat.animate(view)
                    .setDuration(duration)
                    .alpha(0f)
                    .withEndAction {
                        animationSubject.onComplete()
                    }
        }


    }

    fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return Math.round(dp.toFloat() * density)
    }

    fun inversebackcolor(activity: Activity): Int {
        if (uimode(activity)) {
            return Color.BLACK
        }
        return Color.WHITE
    }

    fun inversetextcolor(activity: Activity): Int {
        if (!uimode(activity)) {
            return Color.BLACK
        }
        return Color.WHITE
    }


    fun setLightStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = activity.window.decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.decorView.systemUiVisibility = flags
            var navigationflag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                Log.println(Log.INFO, "Navigation Style", "Device is lower than android O")
            }
            activity.window.decorView.systemUiVisibility = navigationflag

        }
    }


    fun uimode(activity: Activity):Boolean {

        return when (activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    private val offlinefrases = arrayOf("Sem internet é? Acontece né vá aproveitar a vida", "Parece que você está desconectado. Quando você se reconectar eu mostro umas frases", "Bom... Parece que você tá sem internet, então acho que não tem oque fazer...")


    private val empyquotes = arrayOf("Você não vai escrever nada? Tá achando que é festa?", "O vazio da sua existência não necessariamente precisa ser o vazio do bloco de texto, escreva algo!", "Você não quer ver o feed e ver um texto vazio né? Então por favor escreve algo aí", "Eu sei que as vezes você se sente vazio, " + "mas esse bloco de texto te ajuda a mostrar que ainda tem alguma coisa aí, então escreve por favor")


    fun references(activity: Activity): ArrayList<Artists> {
        val colors = intArrayOf(activity.resources.getColor(R.color.green_500), activity.resources.getColor(R.color.pink_500), activity.resources.getColor(R.color.grey_600), activity.resources.getColor(R.color.blue_500), activity.resources.getColor(R.color.teal_500), activity.resources.getColor(R.color.lime_500))
        val artists = ArrayList<Artists>()
        for (i in iconssite.indices) {
            artists.add(Artists(iconssite[i].replace(".com", "").replace("https://", ""), iconssite[i], colors[i]))
        }



        return artists
    }

    fun delayAction(runnable: Runnable, delay: Long) {
        val handler = Handler()
        handler.postDelayed(runnable, delay)
    }


    fun fonts(context: Context): ArrayList<Typeface> {
        val fontsarchieves = arrayOf(Typeface.createFromAsset(context.assets, "fonts/Arvo-Regular_201.ttf"), Typeface.createFromAsset(context.assets, "fonts/Audrey-Normal.otf"), Typeface.createFromAsset(context.assets, "fonts/Cornerstone.ttf"), Typeface.createFromAsset(context.assets, "fonts/times.ttf"), Typeface.createFromAsset(context.assets, "fonts/MightypeScript.otf"), Typeface.createFromAsset(context.assets, "fonts/AmaticSC-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Amiko-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/BlackHanSans-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Cabin-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Cinzel-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/CinzelDecorative-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Farsan-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/FingerPaint-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/FredokaOne-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Inconsolata-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Lalezar-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Lobster-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Mogra-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Nunito-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/NunitoSans-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Pacifico-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Quicksand-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Rakkas-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Ranga-Regular.ttf"), Typeface.createFromAsset(context.assets, "fonts/Rasa-Regular.ttf"))


        return ArrayList(Arrays.asList(*fontsarchieves))
    }

    fun emptyquote(): String {
        val q: Int
        val x = Random()
        q = x.nextInt(empyquotes.size)
        return empyquotes[q]
    }


    fun offlinemessage(): String {
        val q: Int
        val x = Random()
        q = x.nextInt(offlinefrases.size)
        return offlinefrases[q]
    }

    val RC_SIGN_IN = 1
    val EDIT_QUOTE = 2


    fun convertDate(dia: String): Date {


        //2. Test - Convert Date to Calendar
        //3. Test - Convert Calendar to Date
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        var result = Date()

        try {

            result = df.parse(dia)
            println("post day $result")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        println(dia)


        return result

    }


}
