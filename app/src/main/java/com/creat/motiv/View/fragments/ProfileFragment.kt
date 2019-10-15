package com.creat.motiv.View.fragments


import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.creat.motiv.Model.Beans.Likes
import com.creat.motiv.Model.Beans.Quotes
import com.creat.motiv.R
import com.creat.motiv.Utils.Alert
import com.creat.motiv.Utils.Pref
import com.creat.motiv.Utils.Tools
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private var likequotes: ArrayList<Quotes>? = null
    private var allquotes: ArrayList<Quotes>? = null
    private val myquotes = ArrayList<Quotes>()
    private var likesArrayList: ArrayList<Likes>? = null
    private var preferences: Pref? = null
    private var posts: TextView? = null
    private var likes: TextView? = null
    private var profilepic: CircleImageView? = null
    private var myquotesrecycler: RecyclerView? = null
    private var quotesdb: Query? = null
    private var user = FirebaseAuth.getInstance().currentUser
    private var username: TextView? = null
    private val pfragment = this

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (context == null) {
            return null
        }
        preferences = Pref(Objects.requireNonNull<Context>(context))
        user = FirebaseAuth.getInstance().currentUser
        quotesdb = FirebaseDatabase.getInstance().reference


        val v = inflater.inflate(R.layout.fragment_profile, container, false)
        this.username = v.findViewById(R.id.username)

        this.likes = v.findViewById(R.id.likes)
        this.posts = v.findViewById(R.id.posts)
        myquotesrecycler = v.findViewById(R.id.myquotesrecycler)
        profilepic = v.findViewById(R.id.profilepic)

        Tutorial()



        profilepic!!.setOnClickListener {
            val pics = Alert(Objects.requireNonNull<FragmentActivity>(activity))
            pics.Picalert(pfragment)
        }

        val edit = v.findViewById<Button>(R.id.edit)
        edit.setOnClickListener {
            val a = Alert(activity!!)
            a.settings()
        }

        return v

    }


    private fun Tutorial() {
        val novo = Objects.requireNonNull<Bundle>(Objects.requireNonNull<FragmentActivity>(activity).intent.extras).getBoolean("novo")

        if (novo) {

            val preferences = Pref(Objects.requireNonNull<Context>(context))
            if (!preferences.profiletutorialstate()) {
                preferences.setProfileTutorial(true)
                val a = Alert(activity!!)
                a.Message(activity!!.getDrawable(R.drawable.ic_mobile_post_monochrome), getString(R.string.profile_intro))

            }


        }
    }


    private fun userinfo() {

        username!!.text = user!!.displayName

        Glide.with(this).load(user!!.photoUrl).error(activity!!.getDrawable(R.drawable.notfound)).addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                val a = Alert(activity!!)
                a.Nopicture(pfragment)
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                return false
            }
        }).into(profilepic!!)


    }


    private fun CarregarLikes() {
        if (activity == null) {
            return
        }
        allquotes = ArrayList()
        likequotes = ArrayList()


        val quotesdb2: Query
        quotesdb2 = Tools.quotesreference
        quotesdb2.keepSynced(false)
        quotesdb2.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                likequotes!!.clear()
                allquotes!!.clear()
                for (d in dataSnapshot.children) {

                    val q = d.getValue(Quotes::class.java)
                    val quotes = q!!
                    if (q.textcolor == 0 || q.backgroundcolor == 0) {
                        quotes.textcolor = Color.BLACK
                        quotes.backgroundcolor = Color.WHITE
                    }
                    allquotes!!.add(quotes)
                    like(quotes)
                    println("Quotes " + likequotes!!.size)
                }


                //Collections.reverse(likequotes);
                //recycler(likequotes);


            }


            override fun onCancelled(databaseError: DatabaseError) {
                val a = Alert(activity!!)
                a.Message(a.erroricon, "Erro " + databaseError.message)
            }
        })


    }

    private fun like(position: Quotes) {
        if (activity == null) {
            return
        }
        //loading.setVisibility(View.VISIBLE);
        likesArrayList = ArrayList()
        val likedb = Tools.quotesreference
        likedb.child(position.id!!).child("likes").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                likesArrayList!!.clear()

                for (d in dataSnapshot.children) {
                    val l = d.getValue(Likes::class.java)
                    var likes: Likes? = null
                    if (l != null) {
                        likes = Likes(l.userid, l.username, l.userpic)
                    }
                    likesArrayList!!.add(likes!!)
                    if (l != null && l.userid == user!!.uid) {
                        likequotes!!.add(position)
                    }

                }
                Collections.reverse(likequotes)

                val animator = ValueAnimator.ofInt(0, likequotes!!.size)
                animator.duration = 2500
                animator.addUpdateListener { valueAnimator -> likes!!.text = String.format("%s favoritos", valueAnimator.animatedValue.toString()) }
                animator.start()

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }






}// Required empty public constructor
