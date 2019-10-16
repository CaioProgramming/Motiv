package com.creat.motiv.Model

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.creat.motiv.Model.Beans.Likes
import com.creat.motiv.Model.Beans.Quotes
import com.creat.motiv.R
import com.creat.motiv.Utils.Alert
import com.creat.motiv.Utils.Tools
import com.creat.motiv.View.activities.Splash
import com.creat.motiv.adapters.RecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.mateware.snacky.Snacky
import java.util.*
import kotlin.collections.ArrayList


class QuotesDB : ValueEventListener {


    override fun onCancelled(p0: DatabaseError) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (recyclerView == null) {
            val a = Alert(activity!!)
            a.Message(a.erroricon, "Recyclerview nao encontrada!")

        }
        val quotesArrayList = ArrayList<Quotes>()
        quotesArrayList.clear()
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
                quotesArrayList.add(quotes)

                println("Quotes " + quotesArrayList.size)
                println("Quote  " + quotes.id)


            }

        }
        Collections.reverse(quotesArrayList)
        recyclerView?.visibility = View.VISIBLE
        val llm = GridLayoutManager(activity, Tools.spancount, GridLayoutManager.VERTICAL, false)
        recyclerView?.setHasFixedSize(true)
        println(quotesArrayList.size)
        val myadapter = RecyclerAdapter(quotesArrayList, activity!!)
        recyclerView?.adapter = myadapter
        recyclerView?.layoutManager = llm
        refreshlayout?.isRefreshing = false
        usercount?.text = "${quotesArrayList.size} posts"
        if (likecount != null) {
            quotesdb.orderByChild("likes/userid").equalTo(user.uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            likecount!!.text = dataSnapshot.childrenCount.toString() //To change body of created functions use File | Settings | File Templates.
                        }

                    })
        }
        val alert = Alert(activity!!)
        alert.loading()
        destroy()


    }

    private fun destroy() {
        this.recyclerView = null
        this.usercount = null
        this.likecount = null
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


    fun Carregar() {
        quotesdb.addListenerForSingleValueEvent(this)
    }


    fun CarregarUserQuotes(userid: String) {
        quotesdb.orderByChild("userID").equalTo(userid).addListenerForSingleValueEvent(this)

    }

    fun CarregarLikes() {
        quotesdb.orderByChild("likes/userid").equalTo(user.uid).addListenerForSingleValueEvent(this)
    }


    fun Pesquisar(pesquisa: String) {

        quotesdb.orderByChild("quote").startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        Log.println(Log.INFO, "PESQUISA", "Resultados " + dataSnapshot.childrenCount)

                        val quotesArrayList = ArrayList<Quotes>()
                        recyclerView!!.removeAllViews()
                        for (d in dataSnapshot.children) {
                            var quotes = Quotes()
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
                            var quotes = Quotes()
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
                            var quotes = Quotes()
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
                    var quotes = Quotes()
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



    fun Inserir() {
        val alert = Alert(activity!!)
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Salvando")
        progressDialog.isIndeterminate = true
        val id = quotesdb.push().key
        this.quotes!!.id = id!!
        if (quotes!!.quote!!.isEmpty()) {
            alert.Message(activity!!.getDrawable(com.creat.motiv.R.drawable.ic_error), Tools.emptyquote())
            return
        } else {
            quotesdb.push().child(id).setValue(this.quotes).addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    alert.Message(activity!!.getDrawable(com.creat.motiv.R.drawable.ic_success), "Sua frase foi compartilhada!")
                } else {
                    alert.Message(activity!!.getDrawable(com.creat.motiv.R.drawable.ic_success), "Erro ao publicar! \n" + task.exception!!.message)

                }
            }
        }
        progressDialog.show()
    }

    fun Editar() {
        val user = FirebaseAuth.getInstance().currentUser
        if (quotes!!.quote!!.isEmpty()) {

            Snacky.builder().setActivity(activity!!).error().setText(Tools.emptyquote()).setDuration(5000).show()
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
                    Snacky.builder().setActivity(activity!!).error().setText("Erro " + Objects.requireNonNull<Exception>(task.exception).message).show()
                }
            }
        }
    }

    fun Denunciar() {
        this.quotes!!.isReport = true
        val quotesdb = Tools.quotesreference
        quotesdb.child(quotes!!.id!!).child("report").setValue(this.quotes!!.isReport).addOnCompleteListener { task ->
            val a = Alert(activity!!)


            if (task.isSuccessful) {
                a.Message(a.succesicon, "Frase denunciada com sucesso")
            } else {
                a.Message(a.erroricon, "Erro ao processar denuncia..." + task.exception!!.message)
            }
        }

    }

    fun like() {
        val user = FirebaseAuth.getInstance().currentUser
        quotesdb = Tools.quotesreference
        if (this.quotes == null || user == null) {
            Snacky.builder().setActivity(activity!!).error().setText("Objeto nulo!").show()
            return

        }
        val likes = Likes(user.uid, user.displayName!!, user.photoUrl.toString())
        quotesdb.child(quotes!!.id!!).child("likes").child(user.uid).setValue(likes).addOnCompleteListener {
            Snacky.builder().setActivity(activity!!).setText("Frase curtida")
                    .setBackgroundColor(Color.WHITE).setTextColor(Color.BLACK).setIcon(R.drawable.ic_favorite_black_24dp).build().show()
        }
    }

    fun deslike() {
        val user = FirebaseAuth.getInstance().currentUser

        quotesdb = Tools.quotesreference
        if (this.quotes == null || user == null) {
            Snacky.builder().setActivity(activity!!).error().setText("Objeto nulo!").show()
            return

        }
        quotesdb.child(quotes!!.id!!).child("likes").child(user.uid).removeValue().addOnCompleteListener {
            Snacky.builder().setActivity(activity!!).setText("Frase descurtida")
                    .setBackgroundColor(Color.WHITE).setTextColor(Color.RED).setIcon(R.drawable.ic_favorite_black_24dp).build().show()
        }
    }


    fun Removerposts(id: String) {
        val quotesdb = FirebaseDatabase.getInstance().reference

        quotesdb.child(id).removeValue()

    }

    fun Apagarconta(id: String) {


        val quotesdb = FirebaseDatabase.getInstance().reference

        quotesdb.child(id).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Snacky.builder().setActivity(activity).success().setText("Conta apagada").show()
                FirebaseAuth.getInstance().signOut()
                if (FirebaseAuth.getInstance().currentUser == null) {
                    val snackbar = Snacky.builder().setActivity(activity).build()
                    snackbar.setText("Você saiu do aplicativo")
                    snackbar.setDuration(5000).show()

                    if (!snackbar.isShown) {
                        val intent = Intent(activity, Splash::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        activity!!.startActivity(intent)
                    }
                }
            } else {
                Snacky.builder().setActivity(activity).error().setText("Erro " + Objects.requireNonNull<Exception>(task.exception).message).show()
            }
        }


    }

    private fun Apagarlikes(id: String) {


        val user = FirebaseAuth.getInstance().currentUser
        val quotesdb = FirebaseDatabase.getInstance().reference

        assert(user != null)
        quotesdb.child("likes").child(user!!.uid).removeValue().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Snacky.builder().setActivity(activity).error().setText("Erro " + Objects.requireNonNull<Exception>(task.exception).message).show()
            }
        }


        user.delete()
        FirebaseAuth.getInstance().signOut()

    }


    fun AlterarNome(id: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val quotesdb = FirebaseDatabase.getInstance().reference
        assert(user != null)
        quotesdb.child(id).child("username").setValue(user!!.displayName).addOnCompleteListener { println("quote" + id + "username changed to: " + user.displayName) }

    }


}
