package com.creat.motiv.Utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.CountDownTimer
import android.text.Html
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creat.motiv.Model.Beans.Developers
import com.creat.motiv.Model.Beans.Likes
import com.creat.motiv.Model.Beans.Pics
import com.creat.motiv.Model.Beans.Quotes
import com.creat.motiv.Model.QuotesDB
import com.creat.motiv.Model.UserDB
import com.creat.motiv.R
import com.creat.motiv.Utils.Tools.searcharg
import com.creat.motiv.View.activities.Splash
import com.creat.motiv.adapters.LikeAdapter
import com.creat.motiv.adapters.RecyclerPicAdapter
import com.creat.motiv.adapters.RecyclerReferencesAdapter
import com.creat.motiv.presenter.ProfilePresenter
import com.github.mmin18.widget.RealtimeBlurView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.mateware.snacky.Snacky
import java.util.*

class Alert(private val activity: Activity) : DialogInterface.OnShowListener, DialogInterface.OnDismissListener {
    var erroricon: Drawable? = null
    var succesicon: Drawable? = null
    private val blur: RealtimeBlurView?
    private val dialogNoBorder = R.style.Dialog_No_Border
    private val bottomdialogNoBorder = R.style.Bottom_Dialog_No_Border


    init {
        this.blur = activity.findViewById(R.id.rootblur)
        this.erroricon = activity.getDrawable(R.drawable.ic_error)
        this.succesicon = activity.getDrawable(R.drawable.ic_success)
    }

    fun quoteoptions(isfromuser: Boolean, quote: Quotes) {
        val myDialog = BottomSheetDialog(activity, bottomdialogNoBorder)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setContentView(R.layout.bottom_options)
        myDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        val copy: TextView?
        val edit: TextView?
        val share: TextView?
        val delete: TextView?
        val report: TextView?
        copy = myDialog.findViewById(R.id.copy)
        edit = myDialog.findViewById(R.id.edit)
        share = myDialog.findViewById(R.id.share)
        delete = myDialog.findViewById(R.id.delete)
        report = myDialog.findViewById(R.id.report)

        if (!isfromuser) {
            edit!!.visibility = View.GONE
            delete!!.visibility = View.GONE
        }

        copy?.setOnClickListener {
            myDialog.dismiss()
            val clipboard = activity.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("frase", quote.quote)
            clipboard.primaryClip = clip

            Snacky.builder().setActivity(activity).setBackgroundColor(Color.WHITE).setText("Frase " + quote.quote +
                    "copiado para área de transferência")
                    .setTextColor(Color.BLACK).build().show()
        }

        edit?.setOnClickListener {
            myDialog.dismiss()
            val newQuotepopup = NewQuotepopup(activity)
            newQuotepopup.showedit(quote)
        }

        share?.setOnClickListener {
            myDialog.dismiss()
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/pain"
            share.putExtra(Intent.EXTRA_SUBJECT, "Motiv")
            share.putExtra(Intent.EXTRA_TEXT, quote.quote + " -" + quote.author)
            activity.startActivity(Intent.createChooser(share, "Escolha onde quer compartilhar"))
        }

        delete?.setOnClickListener {
            myDialog.dismiss()
            val raiz: DatabaseReference
            raiz = Tools.quotesreference
            raiz.child(quote.id!!).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val out = AnimationUtils.loadAnimation(activity, R.anim.pop_out)
                } else {
                    Snacky.builder().setActivity(activity).error().setText("Erro " + task.exception!!.message).show()

                }
            }
        }

        report?.setOnClickListener {
            myDialog.dismiss()
            Report(quote)
        }
        myDialog.show()
        val `in` = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
        blur?.visibility = View.VISIBLE
        blur?.startAnimation(`in`)


    }


    private fun Report(quote: Quotes) {
        val myDialog = Dialog(activity, dialogNoBorder)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.setContentView(R.layout.message_dialog)
        myDialog.show()
        val icon = myDialog.findViewById<ImageView>(R.id.icon)
        val message = myDialog.findViewById<TextView>(R.id.message)
        Glide.with(activity).load(activity.getDrawable(R.drawable.flamencodeleteconfirmation)).into(icon)
        message.text = "Opa,opa,opa, uma denúncia? Tem certeza que está frase tem algo inapropriado para a comunidade?"
        val mButton = myDialog.findViewById<Button>(R.id.button)
        mButton.text = "Denunciar"
        mButton.setOnClickListener {
            myDialog.dismiss()
            val quotesDB = QuotesDB(activity, quote)
            quotesDB.Denunciar()
        }
    }

    private fun Ad() {
        val myDialog = Dialog(activity, dialogNoBorder)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.setContentView(R.layout.message_dialog)
        myDialog.show()
        val icon = myDialog.findViewById<ImageView>(R.id.icon)
        val message = myDialog.findViewById<TextView>(R.id.message)
        Glide.with(activity).load(activity.getDrawable(R.drawable.pluto)).into(icon)
        message.text = "Gostaria de nos ajudar vendo um anúncio em vídeo?"
        val mButton = myDialog.findViewById<Button>(R.id.button)
        mButton.text = "Assistir"
        mButton.setOnClickListener {
            myDialog.dismiss()
            ShowAd()
        }
    }


    fun version(messages: String) {
        val myDialog = Dialog(activity, dialogNoBorder)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.setContentView(R.layout.version_alert)
        myDialog.show()
        val txtview = myDialog.findViewById<TextView>(R.id.message)
        val update = myDialog.findViewById<Button>(R.id.updatenow)
        val img = myDialog.findViewById<ImageView>(R.id.background)
        var close = myDialog.findViewById<ImageButton>(R.id.close)

        update.setOnClickListener {
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.creat.motiv")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            activity.startActivity(intent)
        }
        close?.setOnClickListener {
            myDialog.dismiss()
        }
        txtview.text = messages
        Glide.with(activity).load("https://unsplash.com/photos/NvesrDbsrL4").into(img)

    }

    fun Nopicture(profilepresenter: ProfilePresenter?) {
        val myDialog = Dialog(activity, dialogNoBorder)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.setContentView(R.layout.message_dialog)
        myDialog.show()
        val icon = myDialog.findViewById<ImageView>(R.id.icon)
        val message = myDialog.findViewById<TextView>(R.id.message)
        Glide.with(activity).load(erroricon).into(icon)
        message.text = "Parece que não encontramos seu ícone de perfil, gostaria de alterá-lo agora?"

        val mButton = myDialog.findViewById<Button>(R.id.button)
        mButton.text = "Alterar ícone"
        mButton.setOnClickListener {
            myDialog.dismiss()
            Picalert(profilepresenter)
        }
    }


    fun about() {

        val myDialog = Dialog(activity, R.style.AppTheme)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setContentView(R.layout.about_layout)
        val adtext = myDialog.findViewById<TextView>(R.id.adtext)
        val creators = myDialog.findViewById<TextView>(R.id.creators)
        val creatorsrecycler = myDialog.findViewById<RecyclerView>(R.id.creatorsrecycler)
        val designrecycler = myDialog.findViewById<RecyclerView>(R.id.designrecycler)


        myDialog.show()
        val `in` = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
        blur!!.visibility = View.VISIBLE
        blur.startAnimation(`in`)


        myDialog.setOnDismissListener {
            val out = AnimationUtils.loadAnimation(activity, R.anim.fade_out)
            blur.startAnimation(out)
            blur.visibility = View.GONE
        }

        adtext.setOnClickListener {
            myDialog.dismiss()
            Ad()
        }


        CarregarCreators(creators)
        CarregarReferences(designrecycler)

    }


    fun Picalert(profilePresenter: ProfilePresenter?) {
        val Picslist: ArrayList<Pics>

        val blurView = Objects.requireNonNull(activity).findViewById<RealtimeBlurView>(R.id.rootblur)
        Picslist = ArrayList()
        val myDialog = BottomSheetDialog(activity, bottomdialogNoBorder)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setContentView(R.layout.profilepicselect_)

        val picrecycler: RecyclerView?
        val layout = myDialog.findViewById<CardView>(R.id.card)

        layout!!.setCardBackgroundColor(Color.TRANSPARENT)


        picrecycler = myDialog.findViewById(R.id.picsrecycler)
        val databaseReference = Tools.iconsreference
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Picslist.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val pic = postSnapshot.getValue(Pics::class.java)
                    val p = Pics()
                    if (pic != null) {
                        p.uri = pic.uri
                    }
                    Picslist.add(p)
                    println("icons " + Picslist.size)


                }

                Objects.requireNonNull<RecyclerView>(picrecycler).setHasFixedSize(true)
                val llm = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
                val recyclerPicAdapter = RecyclerPicAdapter(Picslist, activity, profilePresenter, myDialog)
                picrecycler!!.adapter = recyclerPicAdapter
                picrecycler.layoutManager = llm
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        myDialog.setOnDismissListener(this)
        myDialog.setOnShowListener(this)

        myDialog.show()





    }

    fun Likelist(likes: ArrayList<Likes>) {
        val myDialog = BottomSheetDialog(activity, bottomdialogNoBorder)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setContentView(R.layout.profilepicselect_)
        myDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        val title = myDialog.findViewById<TextView>(R.id.title)
        val likesrecycler = myDialog.findViewById<RecyclerView>(R.id.picsrecycler)
        val likeAdapter = LikeAdapter(likes, activity)
        val llm = GridLayoutManager(activity, 1, LinearLayoutManager.VERTICAL, false)
        likesrecycler!!.adapter = likeAdapter
        likesrecycler.layoutManager = llm
        title!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_loving_message_red, 0, 0)
        title.text = "Curtidas"
        myDialog.show()

    }


    fun changename() {
        val user = FirebaseAuth.getInstance().currentUser
        val myDialog = Dialog(activity, dialogNoBorder)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.setContentView(R.layout.changename)
        myDialog.show()
        val mUsername = myDialog.findViewById<EditText>(R.id.username)
        mUsername.hint = user!!.displayName
        val mButton = myDialog.findViewById<Button>(R.id.button)
        mButton.setOnClickListener {
            mButton.isEnabled = false
            val userDB = UserDB(activity)
            userDB.changeusername(mUsername.text.toString())
        }
    }


    fun settings() {
        val myDialog = BottomSheetDialog(activity, bottomdialogNoBorder)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setContentView(R.layout.settings)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.show()
        val mChangename = myDialog.findViewById<TextView>(R.id.changename)
        mChangename!!.setOnClickListener {
            myDialog.dismiss()
            changename()
        }
        val mDeleteposts = myDialog.findViewById<TextView>(R.id.deleteposts)
        mDeleteposts!!.setOnClickListener { Removeposts() }
        val mDeleteaccount = myDialog.findViewById<TextView>(R.id.deleteaccount)
        mDeleteaccount!!.setOnClickListener { RemoverAccount() }
        val mExit = myDialog.findViewById<TextView>(R.id.exit)
        mExit!!.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val snackbar = Snacky.builder().setActivity(activity).setBackgroundColor(Color.WHITE).setTextColor(activity.resources.getColor(R.color.colorPrimaryDark)).build()
            snackbar.setText("Você saiu do aplicativo")
            snackbar.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE).show()
            val timer = object : CountDownTimer(3500, 100) {
                override fun onTick(l: Long) {

                }

                override fun onFinish() {
                    val intent = Intent(activity, Splash::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    activity.startActivity(intent)
                }
            }.start()
        }


    }

    private fun Removeposts() {
        val user = FirebaseAuth.getInstance().currentUser

        val quotesdb = Tools.quotesreference
        quotesdb.orderByChild("userID")
                .startAt(user!!.uid)
                .endAt(user.uid + searcharg)
        quotesdb.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val myquotes = ArrayList<Quotes>()
                myquotes.clear()
                for (d in dataSnapshot.children) {
                    val q = d.getValue(Quotes::class.java)
                    myquotes.add(q!!)
                }
                val quotesDB = QuotesDB(activity)
                val alertDialog = AlertDialog.Builder(activity)
                        .setTitle("Tem certeza?")
                        .setMessage("Suas " + myquotes.size + " frases serão removidas para sempre! S E M P R E")
                        .setNeutralButton("Tenho certeza sim, cliquei porque quis!") { dialogInterface, i ->
                            for (quotes in myquotes) {
                                quotesDB.Removerposts(quotes.id!!)
                            }
                        }
                        .setNegativeButton("Cliquei errado calma", null)
                alertDialog.show()


            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }

    private fun RemoverAccount() {
        val user = FirebaseAuth.getInstance().currentUser
        val quotesdb = Tools.quotesreference
        quotesdb.orderByChild("userID").startAt(user!!.uid)
                .endAt(user.uid + searcharg)
        quotesdb.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val myquotes = ArrayList<Quotes>()
                myquotes.clear()
                for (d in dataSnapshot.children) {
                    val q = d.getValue(Quotes::class.java)
                    myquotes.add(q!!)
                }
                val quotesDB = QuotesDB(activity)
                val alertDialog = AlertDialog.Builder(activity)
                        .setTitle("Tem certeza?")
                        .setMessage("Você e suas " + myquotes.size + " frases serão removidos para sempre! S E M P R E")
                        .setNeutralButton("Sim me tira daqui agora") { dialogInterface, i ->
                            val progressDialog = ProgressDialog(activity, R.style.Dialog_No_Border)
                            progressDialog.show()
                            for (quotes in myquotes) {
                                quotesDB.Apagarconta(quotes.id!!)
                            }
                            progressDialog.setMessage("Apagando tudo...")
                            val timer = object : CountDownTimer(2000, 100) {
                                override fun onTick(l: Long) {

                                }

                                override fun onFinish() {
                                    progressDialog.dismiss()
                                }
                            }.start()
                        }.setNegativeButton("Cliquei errado calma", null)
                alertDialog.show()


            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun ShowAd() {
        val rewardedVideoAd: RewardedVideoAd
        val progressDialog = ProgressDialog(activity, dialogNoBorder)
        progressDialog.setOnShowListener(this)
        progressDialog.setOnDismissListener(this)

        MobileAds.initialize(activity,
                "ca-app-pub-4979584089010597~4181793255")

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity)
        rewardedVideoAd.rewardedVideoAdListener = object : RewardedVideoAdListener {
            override fun onRewardedVideoAdLoaded() {
                progressDialog.dismiss()
                rewardedVideoAd.show()
            }

            override fun onRewardedVideoAdOpened() {

            }

            override fun onRewardedVideoStarted() {

            }

            override fun onRewardedVideoAdClosed() {

            }

            override fun onRewarded(rewardItem: RewardItem) {

            }

            override fun onRewardedVideoAdLeftApplication() {


            }

            override fun onRewardedVideoAdFailedToLoad(i: Int) {
                Message(erroricon, "Ocorreu um erro carregando o vídeo \uD83D\uDE22 ")
            }

            override fun onRewardedVideoCompleted() {
                Message(activity.getDrawable(R.drawable.flame_success), "Obrigado pela ajuda, você é demais \uD83D\uDE0D!")

            }
        }
        loadRewardedVideoAd(rewardedVideoAd)

    }

    private fun loadRewardedVideoAd(rewardedVideoAd: RewardedVideoAd) {
        rewardedVideoAd.loadAd("ca-app-pub-4979584089010597/9410101997",
                AdRequest.Builder().build())

    }


    private fun CarregarCreators(devs: TextView) {
        val aboutdb = FirebaseDatabase.getInstance().reference.child("Developers")
        aboutdb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val devtext = StringBuilder()

                var i = 0
                for (d in dataSnapshot.children) {
                    val dv = d.getValue(Developers::class.java)

                    if (dv != null) {
                        if (i == 0) {
                            devtext.append("<b>" + dv.nome + "</b>")
                        } else {
                            devtext.append(" e <b>" + dv.nome + "</b>")
                        }
                        i++


                    }

                }
                devs.text = Html.fromHtml(devtext.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun CarregarReferences(designrecycler: RecyclerView) {

        val recyclerReferencesAdapter = RecyclerReferencesAdapter(activity)
        val llm = GridLayoutManager(activity, 1, LinearLayoutManager.VERTICAL, false)
        designrecycler.layoutManager = llm
        designrecycler.setHasFixedSize(true)
        designrecycler.adapter = recyclerReferencesAdapter

    }


    fun loading() {
        val myDialog = Dialog(activity, R.style.Dialog_No_Border)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setContentView(R.layout.loading)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.show()

        val timer = object : CountDownTimer(2500, 100) {
            override fun onTick(l: Long) {

            }

            override fun onFinish() {
                myDialog.dismiss()

            }
        }
        timer.start()

    }

    fun Message(dicon: Drawable?, messages: String) {
        val myDialog = Dialog(activity, dialogNoBorder)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.setContentView(R.layout.message_dialog)
        myDialog.show()
        val icon = myDialog.findViewById<ImageView>(R.id.icon)
        val message = myDialog.findViewById<TextView>(R.id.message)
        Glide.with(activity).load(dicon).into(icon)
        message.text = messages
        val mButton = myDialog.findViewById<Button>(R.id.button)
        mButton.setOnClickListener { myDialog.dismiss() }


    }


    override fun onShow(dialogInterface: DialogInterface) {
        if (blur != null) {
            Tools.fadeIn(this.blur, 1500).subscribe()
        }

    }

    override fun onDismiss(dialogInterface: DialogInterface) {
        if (blur != null) {
            Tools.fadeOut(this.blur, 1500).subscribe()
        }
    }


}

