package com.creat.motiv.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Vibrator
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.creat.motiv.model.Beans.Likes
import com.creat.motiv.model.Beans.Quotes
import com.creat.motiv.model.Beans.User
import com.creat.motiv.model.QuotesDB
import com.creat.motiv.model.UserDB
import com.creat.motiv.R
import com.creat.motiv.databinding.QuotescardBinding
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.ColorUtils
import com.creat.motiv.utils.Tools
import com.creat.motiv.utils.Tools.fadeIn
import com.creat.motiv.view.activities.UserActivity
import com.creat.motiv.view.fragments.ProfileFragment
import com.devs.readmoreoption.ReadMoreOption
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


class RecyclerAdapter(private val mData: ArrayList<Quotes>?, private val activity: Activity) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val quotescardBinding = QuotescardBinding.inflate(LayoutInflater.from(activity),null,false)
        return MyViewHolder(quotescardBinding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val quote = mData!![holder.adapterPosition]
        loadLikes(holder, quote)
        holder.quotescardBinding.like.setOnClickListener { Like(holder.adapterPosition, holder) }
        holder.quotescardBinding.quote.text = quote.quote
        holder.quotescardBinding.author.text = quote.author
        println("Quote " + mData[position].quote + " selected font: " + mData[position].font)
        if (quote.font != null) {
            holder.quotescardBinding.quote.typeface = Tools.fonts(activity)[mData[position].font!!]
            holder.quotescardBinding.author.typeface = Tools.fonts(activity)[mData[position].font!!]


        } else {
            holder.quotescardBinding.quote.typeface = Typeface.DEFAULT
            holder.quotescardBinding.author.typeface = Typeface.DEFAULT
        }
        val postdia = Tools.convertDate(quote.data!!)
        val now = Calendar.getInstance().time

        print("Date comparision ${now.compareTo(postdia)}")


        val fmt = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())


        val dayCount = ((now.time - postdia.time) / 1000 / 60 / 60 / 24).toInt()
        when {
            dayCount < 1 -> {
                holder.quotescardBinding.dia.text = "Hoje"
            }
            dayCount == 1 -> {
                holder.quotescardBinding.dia.text = "Ontem"
            }
            else -> {
                holder.quotescardBinding.dia.text = fmt.format(postdia)
            }
        }

        val user = FirebaseAuth.getInstance().currentUser

        if (quote.userID != user!!.uid) {
            var u = User()
            u.uid = quote.userID!!
            var uid = u.uid
            u.name = quote.username!!
            u.picurl = quote.userphoto!!
            val userDB = UserDB()
            u = userDB.getUser(uid)!!

            Glide.with(activity).load(u.picurl).error(R.drawable.notfound).into(holder.quotescardBinding.userpic)
            holder.quotescardBinding.username.text = u.name
            holder.quotescardBinding.userpic.setOnClickListener { showuserprofile(u, holder) }
            holder.quotescardBinding.username.setOnClickListener { showuserprofile(u, holder) }


        } else {
            holder.quotescardBinding.username.text = user.displayName
            val navigation = activity.findViewById<BottomNavigationView>(R.id.navigation)
            /*holder.quotescardBinding.username.setOnClickListener { pager.setCurrentItem(2, true) }
            holder.quotescardBinding.userpic.setOnClickListener { pager.setCurrentItem(2, true) }*/
            Glide.with(activity).load(user.photoUrl).error(R.drawable.notfound).into(holder.quotescardBinding.userpic)


        }

        if (mData[position].backgroundcolor != 0) {
            holder.quotescardBinding.background.setCardBackgroundColor(quote.backgroundcolor!!)

        }
        if (mData[position].textcolor != 0) {
            holder.quotescardBinding.quote.setTextColor(quote.textcolor!!)
            holder.quotescardBinding.author.setTextColor(quote.textcolor!!)
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

        readMoreOption.addReadMoreTo(holder.quotescardBinding.quote, quote.quote)

        holder.quotescardBinding.background.setOnClickListener {
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

        holder.quotescardBinding.share.setOnClickListener {
            val shareintent = Intent(Intent.ACTION_SEND)
            shareintent.type = "text/pain"
            shareintent.putExtra(Intent.EXTRA_SUBJECT, "Motiv")
            shareintent .putExtra(Intent.EXTRA_TEXT, quote.quote + " -" + quote.author)
            activity.startActivity(Intent.createChooser(shareintent, "Escolha onde quer compartilhar"))
        }

        val time: Long = 1000

        fadeIn(holder.quotescardBinding.quotedata, time).andThen{
            val handler = Handler()
            handler.postDelayed({
                holder.quotescardBinding.topshimmer.hideShimmer()
            },1500)

        }.subscribe()



    }


    private fun showuserprofile(u: User, holder: MyViewHolder) {
        /*Alert a = new Alert(activity);
        a.message(activity.getDrawable(R.drawable.ic_magic_wand), "Estamos trabalhando nisso ok...");*/
        val i = Intent(activity, UserActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("User",u)
        i.putExtra("user",bundle)
        i.putExtra("uid", u.uid)
        i.putExtra("uname", u.name)
        i.putExtra("upic", u.picurl)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                holder.quotescardBinding.userpic as View, "profilepic")
        activity.startActivity(i, options.toBundle())

    }


    private fun Like(position: Int, holder: MyViewHolder) {
        val quotesDB = QuotesDB(activity, mData!![position])
        Log.println(Log.INFO, "Quotes", "like event on quote \n ${mData[position].id}  ${mData[position].quote} ")
        if (!holder.quotescardBinding.like.isChecked) {
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
                        holder.quotescardBinding.like.isChecked = l.userid == userdb!!.uid
                    }

                }
                if (likesArrayList.size > 0) {
                    val liketext = StringBuilder()
                    val user = likesArrayList[0].userid
                    var username = likesArrayList[0].username
                    if (userdb!!.uid == user) {
                        username = "Você"
                        holder.quotescardBinding.like.isChecked = true
                    }
                    liketext.append("Curtido por ")
                    if (likesArrayList.size > 1) {
                        liketext.append("<b>").append(username).append("</b>").append(" e <b>outras pessoas</b>")
                    } else {
                        liketext.append("<b>").append(username).append("</b>")

                    }
                    holder.quotescardBinding.likecount.text = Html.fromHtml(liketext.toString())

                    holder.quotescardBinding.likecount.setOnClickListener {
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


    class MyViewHolder(val quotescardBinding: QuotescardBinding) : RecyclerView.ViewHolder(quotescardBinding.root) {

    }
}