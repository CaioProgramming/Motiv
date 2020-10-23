package com.creat.motiv.view.adapters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.utils.Pref
import com.creat.motiv.utils.Tools
import com.creat.motiv.view.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.MessageFormat


class ViewPagerAdapter(private val context: Context, private val activity: Activity) : PagerAdapter() {
    internal var m_dialog: Dialog? = null
    internal var uri: String? = null
    private var quotesdb: Query? = null
    private var count: Long = 0

    private val slide_images = intArrayOf(R.mipmap.ic_launcher, R.drawable.undraw_missed_chances_k3cq, R.drawable.undraw_in_sync_xwsa, R.drawable.security, R.drawable.undraw_wishes_icyp, R.drawable.undraw_creativity_wqmm, R.drawable.undraw_outer_space_3v6n)

    private val slide_titles = arrayOf("Bem-vindo ao Motiv", "Motiv", "Sempre conectado", "Segurança", "Seu mundo", "Imagine...", "Hora de começar!")

    private val slide_text = arrayOf(

            "A sua rede social para os amantes da poesia,você é livre para expressar-se com suas palavras!", "O Motiv é sincronizado em tempo real,sempre que um novo usuário posta algo,você pode visualizar no mesmo momento.", "Seus dados estão seguros e não são compartilhados, fique tranquilo é um ambiente seguro.", "Compartilhe tudo o que imaginar, esse espaço é seu!", "Use a imaginação e criatividade sem medo com a ferramenta de edição.", "Já são inúmeras  frases compartilhadas no mundo!", "Agora que sabe onde se meteu " + user!!.displayName + ",é hora de explorar comunidade do motiv, veja o que os usuários estão compartilhando, compartilhe,explore!")

    private var preferences: Pref? = null

    init {

        Carregar()


    }

    private fun Carregar() {

        val quotesdb = Tools.quotesreference
        quotesdb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                count = dataSnapshot.childrenCount

            }

            override fun onCancelled(databaseError: DatabaseError) {
                println(databaseError.message)
            }
        })

    }


    override fun getCount(): Int {
        return slide_titles.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view: View? = null
        if (layoutInflater != null) {
            view = layoutInflater.inflate(R.layout.newuser, container, false)
        }
        assert(view != null)
        val politics = view!!.findViewById<TextView>(R.id.politics)
        val text = view.findViewById<TextView>(R.id.text)
        val textView2 = view.findViewById<TextView>(R.id.textView2)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val start = view.findViewById<Button>(R.id.start)
        politics.setOnClickListener { Comecar() }
        start.setOnClickListener { Comecar() }
        val myanim = AnimationUtils.loadAnimation(context, R.anim.fade_in)

        view.startAnimation(myanim)
        if (position > 0) {
            Glide.with(context).load(slide_images[position]).into(imageView)

        }
        if (position < 6) {
            politics.visibility = View.GONE
            start.visibility = View.GONE
        } else {
            start.visibility = View.VISIBLE
            politics.visibility = View.VISIBLE

        }


        text.text = slide_text[position]
        textView2.text = slide_titles[position]
        if (position == 6) {
            text.text = MessageFormat.format("Já são {0} frases postadas no motiv", count)


            val i = Intent(context, MainActivity::class.java)

            preferences = Pref(context)

            start.setOnClickListener {
                i.putExtra("novo", true)
                context.startActivity(i)
                activity.finish()
            }


        }
        container.addView(view)

        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }


    private fun Comecar() {
        val i = Intent(context, MainActivity::class.java)

        quotesdb = FirebaseDatabase.getInstance().reference
        quotesdb!!.keepSynced(false)
        preferences!!.setAgree(true)
        i.putExtra("novo", true)
        context.startActivity(i)
        activity.finish()

    }


    companion object {
        private val user = FirebaseAuth.getInstance().currentUser
    }

}
