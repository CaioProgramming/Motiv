package com.creat.motiv.model

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Color
import android.os.CountDownTimer
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.andrognito.flashbar.Flashbar
import com.creat.motiv.R
import com.creat.motiv.adapters.RecyclerAdapter
import com.creat.motiv.model.Beans.Likes
import com.creat.motiv.model.Beans.Quotes
import com.creat.motiv.presenter.ProfilePresenter
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.Tools
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList


class QuotesDB : ValueEventListener {


    override fun onCancelled(p0: DatabaseError) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {

        val quotesArrayList = ArrayList<Quotes>()
        quotesArrayList.clear()
        recyclerView?.removeAllViews()
        for (d in dataSnapshot.children) {
            var quotes: Quotes
            val q = d.getValue(Quotes::class.java)
            if (q != null) {
                quotes = q
                quotes.id = d.key
                if (q.textcolor == 0 || q.backgroundcolor == 0) {
                    quotes.textcolor = Color.BLACK
                    quotes.backgroundcolor = Color.WHITE
                }
                if (likecount != null) {
                    var likes = 0
                    quotesdb.child(quotes.id!!).child("likes").child(user.uid)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        likes++
                                        Log.i("Favoritos", "${likes} favoritos")
                                        likecount!!.text = "${likes} Favoritos"
                                    }
                                }

                            })
                }

                quotesArrayList.add(quotes)

                println("Quotes " + quotesArrayList.size)
                println("Quote  " + quotes.id)
               // Log.println(Log.INFO,"Quote","${quotes.id} likes ${quotes.likes}")


            }

        }
        print("Loaded ${quotesArrayList.size} quotes")
        Collections.reverse(quotesArrayList)
        recyclerView?.visibility = View.VISIBLE
        val llm = GridLayoutManager(activity, Tools.spancount, GridLayoutManager.VERTICAL, false)
        recyclerView?.setHasFixedSize(true)
        println(quotesArrayList.size)
        val myadapter = RecyclerAdapter(quotesArrayList, activity!!)
        recyclerView?.adapter = myadapter
        recyclerView?.layoutManager = llm
        refreshlayout?.isRefreshing = false
        usercount?.text = Html.fromHtml("<b>${quotesArrayList.size}</b>  \n Publicações")
       /* val alert = Alert(activity!!)
        alert.loading()*/



    }


    private var user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private var quotesdb = Tools.quotesreference
    private var activity: Activity? = null
    var refreshlayout: SwipeRefreshLayout? = null
    var usercount: TextView? = null
    var likecount: TextView? = null
    var recyclerView: RecyclerView? = null
    private var quotes: Quotes? = null

    constructor(activity: Activity, quotes: Quotes) {
        this.activity = activity
        this.quotes = quotes
    }

    constructor(activity: Activity) {
        this.activity = activity
    }


    fun carregar() {
        Log.println(Log.INFO, "Quotes", "Loading quotes")
        quotesdb.addListenerForSingleValueEvent(this)
    }









    fun pesquisar(pesquisa: String) {

        quotesdb.orderByChild("quote").startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        Log.println(Log.INFO, "PESQUISA", "Resultados " + dataSnapshot.childrenCount)

                        val quotesArrayList = ArrayList<Quotes>()
                        recyclerView!!.removeAllViews()
                        for (d in dataSnapshot.children) {
                            var quotes: Quotes
                            val q = d.getValue(Quotes::class.java)
                            if (q != null) {
                                quotes = q
                                quotes.id = d.key
                                if (q.textcolor == 0 || q.backgroundcolor == 0) {
                                    quotes.textcolor = Color.BLACK
                                    quotes.backgroundcolor = Color.WHITE
                                } else {
                                    quotes.textcolor = q.textcolor
                                    quotes.backgroundcolor = q.backgroundcolor
                                }

                                if (q.userID == user.uid) {
                                    quotes.username = user.displayName
                                    quotes.userphoto = user.photoUrl.toString()
                                }
                                quotesArrayList.add(quotes)


                            }
                        }


                        if (quotesArrayList.size > 0) {
                            recyclerView!!.visibility = View.VISIBLE
                            val llm = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
                            recyclerView!!.setHasFixedSize(true)
                            System.out.println(quotesArrayList.size)
                            val myadapter = RecyclerAdapter(quotesArrayList, activity!!)
                            myadapter.notifyDataSetChanged()
                            recyclerView!!.adapter = myadapter
                            recyclerView!!.layoutManager = llm
                            Log.println(Log.INFO, "PESQUISA", "Resultados para $pesquisa")
                            Log.println(Log.INFO, "PESQUISA", "Resultados " + quotesArrayList.size)

                        } else {
                            PesquisarAuthor(pesquisa)

                        }

                    }


                    override fun onCancelled(databaseError: DatabaseError) {
                        println("Erro " + databaseError.message)
                    }
                })


    }

    private fun PesquisarAuthor(pesquisa: String) {
        quotesdb.orderByChild("quote").startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        Log.println(Log.INFO, "PESQUISA", "Resultados " + dataSnapshot.childrenCount)

                        val quotesArrayList = ArrayList<Quotes>()
                        recyclerView!!.removeAllViews()
                        for (d in dataSnapshot.children) {
                            var quotes: Quotes
                            val q = d.getValue(Quotes::class.java)
                            if (q != null) {
                                quotes = q
                                quotes.id = d.key
                                if (q.textcolor == 0 || q.backgroundcolor == 0) {
                                    quotes.textcolor = Color.BLACK
                                    quotes.backgroundcolor = Color.WHITE
                                } else {
                                    quotes.textcolor = q.textcolor
                                    quotes.backgroundcolor = q.backgroundcolor
                                }

                                if (q.userID == user.uid) {
                                    quotes.username = user.displayName
                                    quotes.userphoto = user.photoUrl.toString()
                                }
                                quotesArrayList.add(quotes)


                            }
                        }


                        if (quotesArrayList.size > 0) {
                            recyclerView!!.visibility = View.VISIBLE
                            val llm = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
                            recyclerView!!.setHasFixedSize(true)
                            System.out.println(quotesArrayList.size)
                            val myadapter = RecyclerAdapter(quotesArrayList, activity!!)
                            myadapter.notifyDataSetChanged()
                            recyclerView!!.adapter = myadapter
                            recyclerView!!.layoutManager = llm
                            Log.println(Log.INFO, "PESQUISA", "Resultados para $pesquisa")
                            Log.println(Log.INFO, "PESQUISA", "Resultados " + quotesArrayList.size)

                        } else {
                            PesquisarUsuario(pesquisa)

                        }

                    }


                    override fun onCancelled(databaseError: DatabaseError) {
                        println("Erro " + databaseError.message)
                    }
                })

    }

    private fun PesquisarUsuario(pesquisa: String) {
        quotesdb.orderByChild("quote").startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        Log.println(Log.INFO, "PESQUISA", "Resultados " + dataSnapshot.childrenCount)

                        val quotesArrayList = ArrayList<Quotes>()
                        recyclerView!!.removeAllViews()
                        for (d in dataSnapshot.children) {
                            var quotes: Quotes
                            val q = d.getValue(Quotes::class.java)
                            if (q != null) {
                                quotes = q
                                quotes.id = d.key
                                if (q.textcolor == 0 || q.backgroundcolor == 0) {
                                    quotes.textcolor = Color.BLACK
                                    quotes.backgroundcolor = Color.WHITE
                                } else {
                                    quotes.textcolor = q.textcolor
                                    quotes.backgroundcolor = q.backgroundcolor
                                }

                                if (q.userID == user.uid) {
                                    quotes.username = user.displayName
                                    quotes.userphoto = user.photoUrl.toString()
                                }
                                quotesArrayList.add(quotes)


                            }
                        }


                        if (quotesArrayList.size > 0) {
                            recyclerView!!.visibility = View.VISIBLE
                            val llm = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
                            recyclerView!!.setHasFixedSize(true)
                            System.out.println(quotesArrayList.size)
                            val myadapter = RecyclerAdapter(quotesArrayList, activity!!)
                            myadapter.notifyDataSetChanged()
                            recyclerView!!.adapter = myadapter
                            recyclerView!!.layoutManager = llm
                            Log.println(Log.INFO, "PESQUISA", "Resultados para $pesquisa")
                            Log.println(Log.INFO, "PESQUISA", "Resultados " + quotesArrayList.size)

                        } else {
                            PesquisaAvancada(pesquisa)

                        }

                    }


                    override fun onCancelled(databaseError: DatabaseError) {
                        println("Erro " + databaseError.message)
                    }
                })


    }

    private fun PesquisaAvancada(pesquisa: String) {
        quotesdb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.println(Log.INFO, "PESQUISA", "Resultados " + dataSnapshot.childrenCount)

                val quotesArrayList = ArrayList<Quotes>()
                recyclerView!!.removeAllViews()
                for (d in dataSnapshot.children) {
                    var quotes: Quotes
                    val q = d.getValue(Quotes::class.java)
                    if (q != null) {
                        quotes = q
                        quotes.id = d.key
                        if (q.textcolor == 0 || q.backgroundcolor == 0) {
                            quotes.textcolor = Color.BLACK
                            quotes.backgroundcolor = Color.WHITE
                        } else {
                            quotes.textcolor = q.textcolor
                            quotes.backgroundcolor = q.backgroundcolor
                        }

                        if (q.userID == user.uid) {
                            quotes.username = user.displayName
                            quotes.userphoto = user.photoUrl.toString()
                        }
                        if (quotes.quote!!.contains(pesquisa) || quotes.author!!.contains(pesquisa) || quotes.username!!.contains(pesquisa)) {
                            quotesArrayList.add(quotes)
                        }
                    }
                }


                if (quotesArrayList.size > 0) {
                    recyclerView!!.visibility = View.VISIBLE
                    val llm = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
                    recyclerView!!.setHasFixedSize(true)
                    System.out.println(quotesArrayList.size)
                    val myadapter = RecyclerAdapter(quotesArrayList, activity!!)
                    myadapter.notifyDataSetChanged()
                    recyclerView!!.adapter = myadapter
                    recyclerView!!.layoutManager = llm
                    Log.println(Log.INFO, "PESQUISA", "Resultados para $pesquisa")
                    Log.println(Log.INFO, "PESQUISA", "Resultados " + quotesArrayList.size)

                }

            }


            override fun onCancelled(databaseError: DatabaseError) {
                println("Erro " + databaseError.message)
            }
        })


    }



    fun inserir() {
        var flashbar = Flashbar.Builder(activity!!)
                .gravity(Flashbar.Gravity.BOTTOM)
                .title("Salvando!")
                .message("Sua frase está sendo enviada...")
                .backgroundColorRes(R.color.colorPrimaryDark)
                .showProgress(Flashbar.ProgressPosition.RIGHT)
                .progressTintRes(R.color.colorAccent)
                .build()



        val alert = Alert(activity!!)

        if (quotes!!.quote!!.isEmpty()) {
            flashbar.dismiss()
            Alert.builder(activity!!).snackmessage(R.drawable.ic_error,Tools.emptyquote())
            return
        } else {
            quotesdb.push().setValue(this.quotes).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Alert.builder(activity!!).snackmessage(R.drawable.ic_success, "Sua frase foi compartilhada!")
                } else {
                    Alert.builder(activity!!).snackmessage(R.drawable.ic_error, "Erro ao publicar! \n" + task.exception!!.message)

                }
                flashbar.dismiss()

            }
        }
    }

    fun editar() {
        val user = FirebaseAuth.getInstance().currentUser
        if (quotes!!.quote!!.isEmpty()) {
            Alert.builder(activity!!).snackmessage(null,Tools.emptyquote())

        } else {
            val progressDialog = ProgressDialog(activity)
            progressDialog.setTitle("Salvando")
            progressDialog.show()
            if (user != null) {
                quotes!!.userphoto = user.photoUrl.toString()
                quotes!!.username = user.displayName.toString()
                quotes!!.userID = user.uid
            }
            println("Edited quote id " + this.quotes!!.id)
            quotesdb.child(quotes!!.id!!).setValue(this.quotes).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.setTitle("Frase editada com sucesso!")
                    val timer = object : CountDownTimer(3000, 100) {
                        override fun onTick(l: Long) {

                        }

                        override fun onFinish() {
                            progressDialog.dismiss()
                        }
                    }
                    timer.start()

                } else {
                    Alert.builder(activity!!).snackmessage(null,"Erro ${task.exception!!.message} "  )
                }
            }
        }
    }

    fun denunciar() {
        this.quotes!!.isReport = true
        val quotesdb = Tools.quotesreference
        quotesdb.child(quotes!!.id!!).child("report").setValue(this.quotes!!.isReport).addOnCompleteListener { task ->
            val a = Alert(activity!!)


            if (task.isSuccessful) {
                a.snackmessage( R.drawable.ic_success,"Frase denunciada com sucesso")
            } else {
                a.snackmessage(R.drawable.ic_error,"Erro ao processar denuncia ${task.exception!!.message})")
            }
        }

    }

    fun like() {
        val user = FirebaseAuth.getInstance().currentUser
        quotesdb = Tools.quotesreference
        if (this.quotes == null || user == null) {
            Alert.builder(activity!!).snackmessage(R.drawable.ic_error,"Frase não encontrada")

            return

        }
        val likes = Likes(user.uid, user.displayName!!, user.photoUrl.toString())
        quotesdb.child(quotes!!.id!!).child("likes").child(user.uid).setValue(likes).addOnCompleteListener {
            if (it.isSuccessful) {
                Alert.builder(activity!!).snackmessage(R.drawable.ic_heart,"Frase curtida com sucesso")
            }else{
                Alert.builder(activity!!).snackmessage(R.drawable.ic_error,"Erro ao curtir frase ${it.exception!!.localizedMessage}")

            }

        }
    }

    fun deslike() {
        val user = FirebaseAuth.getInstance().currentUser

        quotesdb = Tools.quotesreference
        if (this.quotes == null || user == null) {

            Alert.builder(activity!!).snackmessage(null,"Frase não encontrada")
            return

        }
        quotesdb.child(quotes!!.id!!).child("likes").child(user.uid).removeValue().addOnCompleteListener {
            Alert.builder(activity!!).snackmessage(null,"Frase descurtida com sucesso")

        }
    }


    fun removerposts(id: String) {

        quotesdb.child(id).removeValue()

    }

    fun apagarconta(id: String) {


        val quotesdb = FirebaseDatabase.getInstance().reference

        quotesdb.child(id).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Alert.builder(activity!!).snackmessage(null,"Conta removida")

                FirebaseAuth.getInstance().signOut()
                if (FirebaseAuth.getInstance().currentUser == null) {
                    Alert.builder(activity!!).snackmessage(null,"Você saiu do aplicativo")
                    val handler = Handler()
                    handler.postDelayed(object : Runnable {
                        override fun run() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    },1000)


                }
            } else {
                activity?.let { Alert.builder(it).snackmessage(null,"Erro ${Objects.requireNonNull<Exception>(task.exception).message}" ) }
            }
        }


    }


    private fun apagarlikes(id: String) {


        val user = FirebaseAuth.getInstance().currentUser
        val quotesdb = FirebaseDatabase.getInstance().reference

        assert(user != null)
        quotesdb.child("likes").child(user!!.uid).removeValue().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                activity?.let { Alert.builder(it).snackmessage(R.drawable.ic_error,"Erro  ${task.exception!!.message}") }
            }
        }


        user.delete()
        FirebaseAuth.getInstance().signOut()

    }


    fun carregarlikes(profilePresenter: ProfilePresenter) {
        quotesdb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val quotesArrayList = ArrayList<Quotes>()
                quotesArrayList.clear()
                recyclerView?.removeAllViews()
                for (d in dataSnapshot.children) {
                    var quotes: Quotes
                    val q = d.getValue(Quotes::class.java)
                    if (q != null) {
                        quotes = q
                        quotes.id = q.id
                        if (q.textcolor == 0 || q.backgroundcolor == 0) {
                            quotes.textcolor = Color.BLACK
                            quotes.backgroundcolor = Color.WHITE
                        }
                        if (likecount != null) {
                            quotesdb.child(quotes.id!!).child("likes").child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        quotesArrayList.add(quotes)

                                        print("Loaded ${quotesArrayList.size} favtorite quotes")
                                        Collections.reverse(quotesArrayList)
                                        recyclerView?.visibility = View.VISIBLE
                                        val llm = GridLayoutManager(activity, Tools.spancount, GridLayoutManager.VERTICAL, false)
                                        recyclerView?.setHasFixedSize(true)
                                        println(quotesArrayList.size)
                                        val myadapter = RecyclerAdapter(quotesArrayList, activity!!)
                                        recyclerView?.adapter = myadapter
                                        recyclerView?.layoutManager = llm
                                        refreshlayout?.isRefreshing = false
                                        likecount?.text = "${quotesArrayList.size} favoritos"
                                        val alert = Alert(activity!!)
                                        alert.loading()

                                    }
                                }

                            })

                        }
                    }

                }

            }
        })

    }


}
