package com.creat.motiv.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Vibrator
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.creat.motiv.Model.Beans.Likes
import com.creat.motiv.Model.Beans.Quotes
import com.creat.motiv.Model.Beans.User
import com.creat.motiv.Model.QuotesDB
import com.creat.motiv.Model.UserDB
import com.creat.motiv.R
import com.creat.motiv.Utils.Alert
import com.creat.motiv.Utils.ColorUtils
import com.creat.motiv.Utils.Tools
import com.creat.motiv.View.activities.UserActivity
import com.devs.readmoreoption.ReadMoreOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import java.text.SimpleDateFormat
import java.util.*


class RecyclerAdapter(private val mData: ArrayList<Quotes>?, private val activity: Activity) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(activity)
        view = mInflater.inflate(R.layout.quotescard, parent, false)


        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val quote = mData!![holder.adapterPosition]
        loadLikes(holder, quote)
        holder.like.setOnClickListener { Like(holder.adapterPosition, holder) }
        holder.quote.text = quote.quote
        holder.author.text = quote.author
        println("Quote " + mData[position].quote + " selected font: " + mData[position].font)
        if (quote.font != null) {
            holder.quote.typeface = Tools.fonts(activity)[mData[position].font!!]
            holder.author.typeface = Tools.fonts(activity)[mData[position].font!!]


        } else {
            holder.quote.typeface = Typeface.DEFAULT
            holder.author.typeface = Typeface.DEFAULT
        }
        val postdia = Tools.convertDate(quote.data!!)
        val now = Calendar.getInstance().time

        val fmt = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())


        val dayCount = ((now.time - postdia.time) / 1000 / 60 / 60 / 24).toInt()
        if (dayCount < 1) {
            holder.dia.text = "Hoje"
        } else if (dayCount == 1) {
            holder.dia.text = "Ontem"
        } else if (dayCount < 7) {
            holder.dia.text = "Há $dayCount dias"
        } else if (dayCount == 7) {
            holder.dia.text = "Há " + dayCount / 7 + " semana"
        } else if (dayCount == 30) {
            holder.dia.text = "Há " + dayCount / 30 + " mês"
        } else {
            holder.dia.text = fmt.format(postdia)
        }

        val user = FirebaseAuth.getInstance().currentUser

        if (quote.userID != user!!.uid) {
            val u = User()
            u.uid = quote.userID!!
            u.name = quote.username!!
            u.picurl = quote.userphoto!!
            val userDB = UserDB(activity)
            userDB.LoadUser(holder.userpic, holder.username, u)
            Log.println(Log.INFO, "USER", "usuario " + u.name)
            Glide.with(activity).load(quote.userphoto).error(R.drawable.notfound).into(holder.userpic)
            holder.username.text = quote.username
            holder.userpic.setOnClickListener { showuserprofile(u) }
            holder.username.setOnClickListener { showuserprofile(u) }
            val time: Long = 500
            holder.userpic.fadeIn(holder.userpic, time)
                    .andThen(holder.username.fadeIn(holder.username, time))
                    .andThen(holder.dia.fadeIn(holder.dia, 500).andThen(holder.likes.fadeIn(holder.likes, time)))
                    .andThen(holder.back.fadeIn(holder.back, time))
                    .andThen(holder.quote.fadeIn(holder.quote, 500))
                    .andThen(holder.author.fadeIn(holder.author, 500))
                    .subscribe()


        } else {
            holder.username.text = user.displayName
            val pager = activity.findViewById<ViewPager>(R.id.pager)
            holder.username.setOnClickListener { pager.setCurrentItem(2, true) }
            holder.userpic.setOnClickListener { pager.setCurrentItem(2, true) }
            Glide.with(activity).load(user.photoUrl).error(R.drawable.notfound).into(holder.userpic)
            val time: Long = 500
            holder.userpic.fadeIn(holder.userpic, time * 2)
                    .andThen(holder.username.fadeIn(holder.username, time))
                    .andThen(holder.dia.fadeIn(holder.dia, 500).andThen(holder.likes.fadeIn(holder.likes, time)))
                    .andThen(holder.back.fadeIn(holder.back, time))
                    .andThen(holder.quote.fadeIn(holder.quote, 500))
                    .andThen(holder.author.fadeIn(holder.author, 500))
                    .subscribe()

        }

        if (mData[position].backgroundcolor != 0) {
            holder.back.setCardBackgroundColor(quote.backgroundcolor!!)

        }
        if (mData[position].textcolor != 0) {
            holder.quote.setTextColor(quote.textcolor!!)
            holder.author.setTextColor(quote.textcolor!!)
        }


        // OR using options to customize
        val color = ColorUtils.lighten(quote.textcolor!!, 0.3)

        val readMoreOption = ReadMoreOption.Builder(activity)
                .textLength(205, ReadMoreOption.TYPE_CHARACTER)
                .moreLabel(" Ver mais...")
                .lessLabel(" Ver menos")
                .moreLabelColor(color)
                .lessLabelColor(color)
                .expandAnimation(true)
                .build()

        readMoreOption.addReadMoreTo(holder.quote, quote.quote)

        holder.back.setOnClickListener {
            val vibrator = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                val mVibratePattern = longArrayOf(100, 150)

                vibrator.vibrate(mVibratePattern, -1) // for 500 ms
            }
            val u = FirebaseAuth.getInstance().currentUser
            val user = quote.userID == u!!.uid
            val alert = Alert(activity)
            alert.quoteoptions(user, mData[holder.adapterPosition])
        }


    }

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

    private fun showuserprofile(u: User) {
        /*Alert a = new Alert(activity);
        a.Message(activity.getDrawable(R.drawable.ic_magic_wand), "Estamos trabalhando nisso ok...");*/
        val i = Intent(activity, UserActivity::class.java)
        i.putExtra("uid", u.uid)
        i.putExtra("uname", u.name)
        i.putExtra("upic", u.picurl)
        activity.startActivity(i)

    }


    private fun Like(position: Int, holder: MyViewHolder) {
        val quotesDB = QuotesDB(activity, mData!![position])
        Log.println(Log.INFO, "Quotes", "like event on quote \n ${mData[position].id}  ${mData[position].quote} ")
        if (!holder.like.isChecked) {
            quotesDB.deslike()
        } else {
            quotesDB.like()
        }
    }

    private fun loadLikes(holder: MyViewHolder, quote: Quotes) {
        val likesArrayList = ArrayList<Likes>()
        val userdb = FirebaseAuth.getInstance().currentUser
        val quotedb = Tools.quotesreference
        quotedb.child(quote.id!!).child("likes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                likesArrayList.clear()
                for (d in dataSnapshot.children) {
                    val l = d.getValue(Likes::class.java)
                    if (l != null) {
                        Log.println(Log.DEBUG, "Likes", "who liked " + l.username)
                        likesArrayList.add(l)
                        holder.like.isChecked = l.userid == userdb!!.uid
                    }

                }
                if (likesArrayList.size > 0) {
                    val liketext = StringBuilder()
                    val user = likesArrayList[0].userid
                    var username = likesArrayList[0].username
                    if (userdb!!.uid == user) {
                        username = "Você"
                        holder.like.isChecked = true
                    }
                    liketext.append("Curtido por ")
                    if (likesArrayList.size > 1) {
                        liketext.append("<b>").append(username).append("</b>").append(" e <b>outras pessoas</b>")
                    } else {
                        liketext.append("<b>").append(username).append("</b>")

                    }
                    holder.likecount.text = Html.fromHtml(liketext.toString())

                    holder.likecount.setOnClickListener {
                        val alert = Alert(activity)
                        alert.Likelist(likesArrayList)
                    }


                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }


    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: LinearLayout
        var likes: LinearLayout
        var like: CheckBox
        var userpic: CircleImageView
        var username: TextView
        var dia: TextView
        var likecount: TextView
        var quote: TextView
        var author: TextView
        var back: CardView
        var quotedata: LinearLayout
        var quoteinfo: LinearLayout

        init {
            likes = view.findViewById(R.id.likes)
            likecount = view.findViewById(R.id.likecount)
            quotedata = view.findViewById(R.id.quotedata)
            quoteinfo = view.findViewById(R.id.quotetop)
            dia = view.findViewById(R.id.dia)
            like = view.findViewById(R.id.like)
            quote = view.findViewById(R.id.quote)
            author = view.findViewById(R.id.author)
            cardView = view.findViewById(R.id.card)
            back = view.findViewById(R.id.background)
            username = view.findViewById(R.id.username)
            userpic = view.findViewById(R.id.userpic)


        }
    }
}