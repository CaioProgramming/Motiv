package com.creat.motiv.Utils

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.CountDownTimer
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.ViewCompat
import com.creat.motiv.Model.Beans.Artists
import com.creat.motiv.R
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

    fun View.fadeIn(view: View, duration: Long): Completable {
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

    fun load(progressBar: ProgressBar?) {
        if (progressBar != null) {
            for (i in 0..3) {
                progressBar.progress = 25 * i
                val timer = object : CountDownTimer(1500, 100) {
                    override fun onTick(l: Long) {

                    }

                    override fun onFinish() {

                    }
                }.start()
            }
            val timer = object : CountDownTimer(1500, 100) {
                override fun onTick(l: Long) {

                }

                override fun onFinish() {
                    progressBar.visibility = View.GONE
                }
            }.start()
        }
    }

}
