package com.creat.motiv.utils

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.*
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.WHITE
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.CountDownTimer
import android.os.Handler
import android.text.Html
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.adapters.LikeAdapter
import com.creat.motiv.adapters.RecyclerPicAdapter
import com.creat.motiv.adapters.RecyclerReferencesAdapter
import com.creat.motiv.databinding.DeleteAllDialogBinding
import com.creat.motiv.databinding.MessageDialogBinding
import com.creat.motiv.model.Beans.Developers
import com.creat.motiv.model.Beans.Likes
import com.creat.motiv.model.Beans.Pics
import com.creat.motiv.model.Beans.Quotes
import com.creat.motiv.model.QuotesDB
import com.creat.motiv.model.UserDB
import com.creat.motiv.presenter.ProfilePresenter
import com.creat.motiv.utils.ColorUtils.ERROR
import com.creat.motiv.utils.ColorUtils.WARNING
import com.creat.motiv.utils.Tools.searcharg
import com.creat.motiv.view.activities.EditQuoteActivity
import com.creat.motiv.view.activities.Splash
import com.github.mmin18.widget.RealtimeBlurView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class Alert(private val activity: Activity) : DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    private val blur: RealtimeBlurView? = activity.findViewById(R.id.rootblur)
    private val styles = arrayOf(R.style.Dialog_No_Border,R.style.Bottom_Dialog_No_Border)


    companion object{
        fun builder(activity: Activity): Alert{
            return Alert(activity)
        }

    }




    fun quoteoptions(isfromuser: Boolean, quote: Quotes) {
        val myDialog = BottomSheetDialog(activity, styles[1])
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

            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("${quote.author}",quote.quote)
            clipboard.setPrimaryClip(clip)
            snackmessage(null,"Frase copiada para área de transferência")

        }

        edit?.setOnClickListener {
            myDialog.dismiss()
            val i = Intent(activity, EditQuoteActivity::class.java)
            i.putExtra("Quote", quote)
            activity.startActivity(i)
        }

        share?.setOnClickListener {
            myDialog.dismiss()
            val shareintent = Intent(Intent.ACTION_SEND)
            shareintent.type = "text/pain"
            shareintent.putExtra(Intent.EXTRA_SUBJECT, "Motiv")
            shareintent .putExtra(Intent.EXTRA_TEXT, quote.quote + " -" + quote.author)
            activity.startActivity(Intent.createChooser(shareintent, "Escolha onde quer compartilhar"))
        }

        delete?.setOnClickListener {
            myDialog.dismiss()
            val raiz: DatabaseReference = Tools.quotesreference
            raiz.child(quote.id!!).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    snackmessage(null,"Frase removida")
                } else {
                    snackmessage(ERROR, "Erro ao remover ${task.exception?.localizedMessage}")

                }
            }
        }

        report?.setOnClickListener {
            myDialog.dismiss()
            Report(quote)
        }
        myDialog.show()


    }


    fun snackmessage(backcolor: Int?, message: String) {

        val snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG
        )
        snackbar.config(activity)
        if (backcolor != null) snackbar.setBackgroundTintList(ColorStateList.valueOf(activity.resources.getColor(backcolor)))
        snackbar.show()
        /*
        if (backcolor != null) {
            ChocoBar.builder()
                    .setText(message)
                    .setMaxLines(4)
                    .setBackgroundColor(activity.resources.getColor(backcolor))
                    .setActivity(activity)
                    .setDuration(ChocoBar.LENGTH_LONG)
                    .build()
                    .show()
        }else{
            ChocoBar.builder()
                    .setText(message)
                    .setMaxLines(4)
                    .setActivity(activity)
                    .setDuration(ChocoBar.LENGTH_LONG)
                    .build()
                    .show()
        }*/

        /* if (color == 0)  {
             snackbar.setBackgroundTint(Tools.inversebackcolor(activity))
         }else{
             snackbar.setTextColor(Tools.inversetextcolor(activity))
         }
         snackbar.config(activity)
         snackbar.show()*/


        /* var flashbar = Flashbar.Builder(activity)
                 .gravity(Flashbar.Gravity.BOTTOM)
                 .message(message)
                 .duration(Flashbar.DURATION_LONG)
                 .backgroundColor(Tools.inversebackcolor(activity))
                 .messageColor(Tools.inversetextcolor(activity))
                 .messageTypeface(Typeface.DEFAULT_BOLD)
                 .icon(icon)
                 .build()
         flashbar.show()*/
    }

    fun likemessage(message: String) {

        var snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG
        )
        snackbar.config(activity)
        snackbar.setBackgroundTint(WHITE)
        snackbar.setTextColor(activity.resources.getColor(R.color.red_500))
        snackbar.show()

    }


    private fun Report(quote: Quotes) {
        val myDialog = Dialog(activity, styles[0])
        val dialogbind = DataBindingUtil.inflate<MessageDialogBinding>(LayoutInflater.from(activity),R.layout.message_dialog,null,false)

        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.setContentView(dialogbind.root)
        myDialog.show()
        dialogbind. icon.setBackgroundColor(activity.resources.getColor(R.color.red_800))
        Glide.with(activity).load(activity.getDrawable(R.drawable.flamencodeleteconfirmation)).into(dialogbind.icon)
        dialogbind.message.text = activity.getString(R.string.report_message)
        dialogbind.button.text = "denunciar"
        dialogbind.button.setOnClickListener {
            myDialog.dismiss()
            val quotesDB = QuotesDB(activity, quote)
            quotesDB.denunciar()
        }
    }

    fun suggestad() {
        val myDialog = Dialog(activity, styles[0])
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




    fun version() {
        Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                .title("Atualização")
                .duration(1500)
                .message("Uma nova versão do motiv está disponível")
                .primaryActionText("Ok")
                .backgroundDrawable(R.drawable.gradient)
                .primaryActionTapListener(object : Flashbar.OnActionTapListener {
                    override fun onActionTapped(bar: Flashbar) {
                        bar.dismiss()
                        val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.creat.motiv")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        activity.startActivity(intent)
                    }
                })
                .enterAnimation(FlashAnim.with(activity)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(activity)
                        .animateBar()
                        .duration(400)
                        .accelerateDecelerate())
                .dismissOnTapOutside()
                .duration(3500)
                .build()
                .show()



    }

    fun mailmessage(){
        val myDialog = Dialog(activity, styles[0])
        val messageDialogBinding = MessageDialogBinding.inflate(LayoutInflater.from(activity),null,false)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.setContentView(messageDialogBinding.root)

        Glide.with(activity)
                .load("https://assets-ouch.icons8.com/thumb/86/9869809b-a087-4218-85bf-c4e8739232df.png")
                .into(messageDialogBinding.icon)
        messageDialogBinding.message.text = "Seu email ainda não foi verificado, verifique para poder enviar frases"
        messageDialogBinding.button.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            user?.sendEmailVerification()
            myDialog.dismiss()
            myDialog.show()

        }
    }

    fun nopicture(profilepresenter: ProfilePresenter?) {
        snackmessage(WARNING, "Você está sem foto de perfil...")

    }


    fun about() {

        val myDialog = Dialog(activity, R.style.AppTheme)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setContentView(R.layout.about_layout)
        val adtext = myDialog.findViewById<TextView>(R.id.adtext)
        val creators = myDialog.findViewById<TextView>(R.id.creators)
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

        adtext?.setOnClickListener {
            myDialog.dismiss()
            ShowAd()

        }


        CarregarCreators(creators!!)
        loadreferences(designrecycler!!)

    }

    private fun deletealldialog(msg: String,myquotes: ArrayList<Quotes>){
        val dltdialogbind = DataBindingUtil.inflate<DeleteAllDialogBinding>(LayoutInflater.from(activity),R.layout.delete_all_dialog,null,false)
        val myDialog = Dialog(activity, styles[1])
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setContentView(dltdialogbind.root)
        dltdialogbind.message.text =  msg
        Glide.with(activity)
                .load("https://assets-ouch.icons8.com/thumb/823/480c1c20-2d3e-415b-abb0-36b0ca3a5cad.png")
                .into(dltdialogbind.icon)
        dltdialogbind.icon.setBackgroundColor(activity.resources.getColor( R.color.red_600))
        dltdialogbind.button.setOnClickListener {
            myDialog.dismiss()
            val quotesDB = QuotesDB(activity)
            val progressDialog = ProgressDialog(activity, R.style.Dialog_No_Border)
            progressDialog.show()
            for (quotes in myquotes) {
                quotesDB.apagarconta(quotes.id!!)
            }
        }
        dltdialogbind.cancellbutton.setOnClickListener {
            myDialog.dismiss()
        }
        myDialog.show()
    }
    private fun deletepostsdialog(msg: String,myquotes: ArrayList<Quotes>){
        val dltdialogbind = DataBindingUtil.inflate<DeleteAllDialogBinding>(LayoutInflater.from(activity),R.layout.delete_all_dialog,null,false)
        val myDialog = Dialog(activity, styles[1])
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setContentView(dltdialogbind.root)
        dltdialogbind.message.text =  msg
        dltdialogbind.icon.setBackgroundColor(Color.BLACK)

        Glide.with(activity)
                .load("https://assets-ouch.icons8.com/thumb/119/a525b97e-6b5c-4419-8c83-7d8e475f427c.png")
                .into(dltdialogbind.icon)
        dltdialogbind.button.setOnClickListener {
            myDialog.dismiss()
            val quotesDB = QuotesDB(activity)
            val progressDialog = ProgressDialog(activity, R.style.Dialog_No_Border)
            progressDialog.show()
            for (quotes in myquotes) {
                quotesDB.removerposts(quotes.id!!)
            }
        }
        dltdialogbind.cancellbutton.setOnClickListener {
            myDialog.dismiss()
        }
        myDialog.show()

    }

    fun Picalert(profilePresenter: ProfilePresenter?) {
        val Picslist: ArrayList<Pics>

        val blurView = Objects.requireNonNull(activity).findViewById<RealtimeBlurView>(R.id.rootblur)
        Picslist = ArrayList()
        val myDialog = BottomSheetDialog(activity, styles[1])
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
        val myDialog = BottomSheetDialog(activity,styles[1])
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

    fun Snackbar.config(context: Context){
        val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(12, 12, 12, 12)
        this.view.layoutParams = params

        this.view.background = context.getDrawable(R.drawable.snack_background)
        this.setTextColor(R.attr.backgroundColor)
        ViewCompat.setElevation(this.view, 6f)
    }


    fun changename(profilePresenter: ProfilePresenter?) {
        val user = FirebaseAuth.getInstance().currentUser
        val myDialog = Dialog(activity, styles[0])
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
            val userDB = UserDB(profilePresenter)
            userDB.changeusername(mUsername.text.toString())
        }
    }


    fun settings(profilePresenter: ProfilePresenter) {
        val myDialog = BottomSheetDialog(activity, styles[1])
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
            changename(profilePresenter)
        }
        val mDeleteposts = myDialog.findViewById<TextView>(R.id.deleteposts)
        mDeleteposts!!.setOnClickListener { Removeposts() }
        val mDeleteaccount = myDialog.findViewById<TextView>(R.id.deleteaccount)
        mDeleteaccount!!.setOnClickListener { RemoverAccount() }
        val mExit = myDialog.findViewById<TextView>(R.id.exit)
        mExit!!.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

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
        quotesdb.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val myquotes = ArrayList<Quotes>()
                myquotes.clear()
                for (d in dataSnapshot.children) {
                    val q = d.getValue(Quotes::class.java)
                    myquotes.add(q!!)
                }
                val message = "Suas " + myquotes.size + " frases serão removidas para sempre! S E M P R E"
                deletepostsdialog(message,myquotes)



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
        quotesdb.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val myquotes = ArrayList<Quotes>()
                myquotes.clear()
                for (d in dataSnapshot.children) {
                    val q = d.getValue(Quotes::class.java)
                    myquotes.add(q!!)
                }

                val message = "Você e suas " + myquotes.size + " frases serão removidos para sempre! S E M P R E"
                deletealldialog(message,myquotes)
            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    private fun ShowAd() {
        val rewardedVideoAd: RewardedVideoAd
        val progressDialog = ProgressDialog(activity, styles[0])
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
                snackmessage(ERROR, "Ocorreu um erro carregando o vídeo \uD83D\uDE22 ")
            }

            override fun onRewardedVideoCompleted() {
                message(activity.getDrawable(R.drawable.flame_success), "Obrigado pela ajuda, você é demais \uD83D\uDE0D!")

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

    private fun loadreferences(designrecycler: RecyclerView) {

        val recyclerReferencesAdapter = RecyclerReferencesAdapter(activity)
        val llm = GridLayoutManager(activity, 1, LinearLayoutManager.VERTICAL, false)
        designrecycler.layoutManager = llm
        designrecycler.setHasFixedSize(true)
        designrecycler.adapter = recyclerReferencesAdapter

    }


    fun loading() {
        val myDialog = Dialog(activity, R.style.BottomDialogAnimation)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setContentView(R.layout.loading)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        myDialog.show()
        val handler = Handler()
        handler.postDelayed({
            myDialog.dismiss()
        },3000)

    }

    fun message(dicon: Drawable?, messages: String) {
        val myDialog = Dialog(activity, styles[0])
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        myDialog.setCanceledOnTouchOutside(true)
        myDialog.setOnShowListener(this)
        myDialog.setOnDismissListener(this)
        val messagebind = DataBindingUtil.inflate<MessageDialogBinding>(LayoutInflater.from(activity),R.layout.message_dialog,null,false)
        myDialog.setContentView(messagebind.root)
        myDialog.show()
        val icon = messagebind.icon
        val message = messagebind.message
        Glide.with(activity).load(dicon).into(icon)
        message.text = messages
        val mButton = messagebind.button
        mButton.setOnClickListener {
            myDialog.dismiss() }
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

