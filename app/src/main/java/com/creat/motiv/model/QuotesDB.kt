package com.creat.motiv.model
import android.app.Activity
import android.graphics.Color
import android.os.Handler
import android.util.Log
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.creat.motiv.model.Beans.Likes
import com.creat.motiv.model.Beans.Quotes
import com.creat.motiv.utils.Alert
import com.creat.motiv.utils.ColorUtils
import com.creat.motiv.utils.ColorUtils.ERROR
import com.creat.motiv.utils.ColorUtils.INFO
import com.creat.motiv.utils.ColorUtils.SUCCESS
import com.creat.motiv.utils.ColorUtils.WARNING
import com.creat.motiv.utils.Tools
import com.creat.motiv.view.adapters.RecyclerAdapter
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
        for (d in dataSnapshot.children) {
            var quotes: Quotes
            val q = d.getValue(Quotes::class.java)
            if (q != null) {
                quotes = q
                quotes.id = d.key
                if (q.textcolor == 0) {
                    quotes.textcolor = activity?.titleColor

                } else if (q.backgroundcolor == 0) {
                    quotes.backgroundcolor = Color.TRANSPARENT
                }
                quotesArrayList.add(quotes)
                println("Quotes " + quotesArrayList.size)
                println("Quote  " + quotes.id)
               // Log.println(Log.INFO,"Quote","${quotes.id} likes ${quotes.likes}")


            }

        }
        print("Loaded ${quotesArrayList.size} quotes")
        quotesArrayList.reverse()
        updateAdapter(quotesArrayList)
        refreshlayout?.isRefreshing = false
    }


    private var user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private var quotesdb = Tools.quotesreference
    private var activity: Activity? = null
    var refreshlayout: SwipeRefreshLayout? = null
    var recyclerAdapter: RecyclerAdapter? = null
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
        quotesdb.addValueEventListener(this)
    }



    private fun updateAdapter(quotesArrayList: ArrayList<Quotes>) {
        recyclerAdapter?.quotesList = quotesArrayList
        Tools.delayAction(Runnable { recyclerAdapter?.notifyDataSetChanged() }, 1500)
    }






    fun pesquisar(pesquisa: String) {

        quotesdb.orderByChild("quote").startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        Log.println(Log.INFO, "PESQUISA", "Resultados " + dataSnapshot.childrenCount)
                        val quotesArrayList = ArrayList<Quotes>()
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
                            updateAdapter(quotesArrayList)
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
                            updateAdapter(quotesArrayList)
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
                            updateAdapter(quotesArrayList)
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
                    updateAdapter(quotesArrayList)
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




        val alert = Alert(activity!!)

        if (quotes!!.quote!!.isEmpty()) {
            alert.snackmessage(ERROR, Tools.emptyquote())
            return
        } else {
            quotesdb.push().setValue(this.quotes).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    alert.snackmessage(SUCCESS, "Sua frase foi compartilhada!")
                } else {
                    alert.snackmessage(ERROR, "Erro ao publicar! \n" + task.exception!!.message)

                }
            }
        }
    }

    fun editar() {
        val user = FirebaseAuth.getInstance().currentUser
        if (quotes!!.quote!!.isEmpty()) {
            Alert.builder(activity!!).snackmessage(WARNING, Tools.emptyquote())

        } else {
            Alert.builder(activity!!).snackmessage(ColorUtils.INFO, "Atualizando frase...")
            if (user != null) {
                quotes!!.userphoto = user.photoUrl.toString()
                quotes!!.username = user.displayName.toString()
                quotes!!.userID = user.uid
            }
            println("Edited quote id " + this.quotes!!.id)
            quotesdb.child(quotes!!.id!!).setValue(this.quotes).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Alert.builder(activity!!).snackmessage(SUCCESS, "Atualizando frase...")

                    val handler = Handler()
                    handler.postDelayed({ activity?.finish() }, 2000)
                } else {
                    Alert.builder(activity!!).snackmessage(ERROR, "Erro ${task.exception!!.message} ")
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
                a.snackmessage(SUCCESS, "Frase denunciada com sucesso")
            } else {
                a.snackmessage(ERROR, "Erro ao processar denuncia ${task.exception!!.message})")
            }
        }

    }

    fun like() {
        val user = FirebaseAuth.getInstance().currentUser
        quotesdb = Tools.quotesreference
        if (this.quotes == null || user == null) {
            Alert.builder(activity!!).snackmessage(ERROR, "Frase não encontrada")

            return

        }
        val likes = Likes(user.uid, user.displayName!!, user.photoUrl.toString())
        quotesdb.child(quotes!!.id!!).child("likes").child(user.uid).setValue(likes).addOnCompleteListener {
            val alert = Alert(activity!!)
            if (it.isSuccessful) {
                alert.likemessage("Frase curtida com sucesso")
            }else{
                alert.snackmessage(ERROR, "Erro ao curtir frase ${it.exception!!.localizedMessage}")

            }

        }
    }

    fun deslike() {
        val user = FirebaseAuth.getInstance().currentUser

        quotesdb = Tools.quotesreference
        if (this.quotes == null || user == null) {

            Alert.builder(activity!!).snackmessage(WARNING, "Frase não encontrada")
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
                    Alert.builder(activity!!).snackmessage(INFO, "Você saiu do aplicativo")
                    val handler = Handler()
                    handler.postDelayed(object : Runnable {
                        override fun run() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    },1000)


                }
            } else {
                activity?.let { Alert.builder(it).snackmessage(ERROR, "Erro ${Objects.requireNonNull<Exception>(task.exception).message}") }
            }
        }


    }


    private fun apagarlikes(id: String) {


        val user = FirebaseAuth.getInstance().currentUser
        val quotesdb = FirebaseDatabase.getInstance().reference

        assert(user != null)
        quotesdb.child("likes").child(user!!.uid).removeValue().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                activity?.let { Alert.builder(it).snackmessage(ERROR, "Erro  ${task.exception!!.message}") }
            }
        }


        user.delete()
        FirebaseAuth.getInstance().signOut()

    }




}
