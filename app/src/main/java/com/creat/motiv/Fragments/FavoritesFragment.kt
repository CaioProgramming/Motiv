package com.creat.motiv.Fragments


import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creat.motiv.Adapters.RecyclerAdapter
import com.creat.motiv.Beans.Likes
import com.creat.motiv.Beans.Quotes
import com.creat.motiv.R
import com.creat.motiv.Utils.Alert
import com.creat.motiv.Utils.Tools
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import de.mateware.snacky.Snacky
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class FavoritesFragment : Fragment() {


    private var myquotesrecycler: RecyclerView? = null
    private var loading: ProgressBar? = null
    private var favcount: TextView? = null
    private var allquotes: ArrayList<Quotes>? = null
    private var likequotes: ArrayList<Quotes>? = null
    private val likesArrayList = ArrayList<Likes>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val v = inflater.inflate(R.layout.fragment_favorites, container, false)
        myquotesrecycler = v.findViewById(R.id.composesrecycler)
        loading = v.findViewById(R.id.loading)
        favcount = v.findViewById(R.id.favtext)
        return v


    }

    private fun show() {
        val a = Alert(Objects.requireNonNull<FragmentActivity>(activity))
        a.loading()

    }


    override fun onResume() {
        CarregarLikes()
        show()
        super.onResume()
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
                    quotes.id = d.key!!
                    if (q.textcolor == 0 || q.backgroundcolor == 0) {
                        quotes.textcolor = Color.BLACK
                        quotes.backgroundcolor = Color.WHITE
                    } else {
                        quotes.textcolor = q.textcolor
                        quotes.backgroundcolor = q.backgroundcolor
                    }
                    quotes.username = q.username
                    quotes.userphoto = q.userphoto
                    allquotes!!.add(quotes)
                    like(quotes)
                    println("Quotes " + likequotes!!.size)

                }


                //Collections.reverse(likequotes);
                //recycler(likequotes);


            }


            override fun onCancelled(databaseError: DatabaseError) {
                Snacky.builder().setActivity(Objects.requireNonNull<FragmentActivity>(activity)).error().setText("Erro " + databaseError.message).show()
            }
        })


    }

    private fun like(position: Quotes) {
        val user = FirebaseAuth.getInstance().currentUser

        val likedb = Tools.quotesreference
        likedb.child(position.id!!).child("likes").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                likesArrayList.clear()

                for (d in dataSnapshot.children) {
                    val l = d.getValue(Likes::class.java)
                    var likes: Likes? = null
                    if (l != null) {
                        likes = Likes(l.userid, l.username, l.userpic)
                    }
                    likesArrayList.add(likes!!)
                    if (l != null && l.userid == Objects.requireNonNull<FirebaseUser>(user).uid) {
                        likequotes!!.add(position)
                    }

                }
                Collections.reverse(likequotes)
                recycler(likequotes)

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    private fun recycler(quotes: ArrayList<Quotes>?) {
        // Collections.reverse(quotes);
        myquotesrecycler!!.visibility = View.VISIBLE
        val llm = GridLayoutManager(activity, Tools.spancount, GridLayoutManager.VERTICAL, false)
        myquotesrecycler!!.setHasFixedSize(true)
        println(quotes)
        val myanim2 = AnimationUtils.loadAnimation(activity, R.anim.pop_in)
        val myadapter = RecyclerAdapter(quotes, activity!!)
        myquotesrecycler!!.adapter = myadapter
        myquotesrecycler!!.layoutManager = llm
        myquotesrecycler!!.startAnimation(myanim2)
        val animator = ValueAnimator.ofInt(0, likequotes!!.size)
        animator.duration = 2500
        animator.addUpdateListener { valueAnimator -> favcount!!.text = String.format("%s favoritos", valueAnimator.animatedValue.toString()) }
        animator.start()
    }


}// Required empty public constructor