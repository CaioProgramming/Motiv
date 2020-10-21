package com.creat.motiv.view.binders

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.creat.motiv.R
import com.creat.motiv.databinding.QuotescardBinding
import com.creat.motiv.model.Beans.Likes
import com.creat.motiv.model.Beans.Quote
import com.creat.motiv.model.Beans.User
import com.creat.motiv.model.QuoteModel
import com.creat.motiv.presenter.BasePresenter
import com.creat.motiv.presenter.LikesPresenter
import com.creat.motiv.utils.ColorUtils
import com.creat.motiv.utils.Tools
import com.creat.motiv.view.activities.UserActivity
import com.devs.readmoreoption.ReadMoreOption
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.*

class QuoteCardBinder(val quotescardBinding: QuotescardBinding,
                      val quotePresenter: BasePresenter<Quote>,
                      val quote: Quote, val context: Context) {

    var likePresenter: LikesPresenter = LikesPresenter(quotescardBinding, context)

    init {
        quotescardBinding.quote = this.quote
        initView()
    }

    fun initView() {


        quotescardBinding.run {
            setupCard()
            setupLike()
            setupQuote()
            setupUser()
        }
        loadLikes()
    }


    fun defineTextSize(length: Int, textView: TextView) {
        textView.textSize = when {
            length <= 40 -> {
                TypedValue.COMPLEX_UNIT_SP
                60f
            }
            length <= 99 -> {
                TypedValue.COMPLEX_UNIT_SP
                40f
            }
            length >= 100 -> {
                TypedValue.COMPLEX_UNIT_SP
                30f
            }
            length >= 200 -> {
                TypedValue.COMPLEX_UNIT_SP
                20f
            }
            else -> {
                TypedValue.COMPLEX_UNIT_SP
                10f
            }
        }
    }


    private fun showUserProfile(user: User) {
        val i = Intent(context, UserActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("USER", user)
        i.putExtra("USER", bundle)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,
                quotescardBinding.userpic as View, "profilepic")
        context.startActivity(i, options.toBundle())

    }

    private fun goToMyUserPage() {
        val activity: Activity = context as Activity
        val viewPager: ViewPager = activity.findViewById(R.id.pager)
        viewPager.currentItem = 2
    }


    private fun QuotescardBinding.setupUser() {
        if (likePresenter.currentUser != null && likePresenter.currentUser!!.uid == quote.userID) {
            setupFirebaseUser(likePresenter.currentUser!!)

        } else {
            loadUser(quote.userID)
        }
    }

    private fun QuotescardBinding.setupQuote() {
        quoteTextView.typeface = Tools.fonts(context)[quote.font]
        defineTextSize(quote.phrase.length, quoteTextView)
        val color = ColorUtils.lighten(quote.textcolor, 0.3)
        val readMoreOption = ReadMoreOption.Builder(context)
                .textLength(120, ReadMoreOption.TYPE_CHARACTER)
                .moreLabel(" Ver mais...")
                .lessLabel(" Ver menos")
                .moreLabelColor(color)
                .lessLabelColor(color)
                .expandAnimation(true)
                .build()
        readMoreOption.addReadMoreTo(quoteTextView, quote.phrase)
        quoteDate.text = data()
    }

    private fun QuotescardBinding.setupLike() {
        like.setOnClickListener {
            likePresenter.currentUser?.let {
                val like = Likes(it.uid,
                        it.displayName,
                        it.photoUrl?.path!!)

                if (quotescardBinding.like.isChecked) {
                    deslikeQuote(like)
                } else {
                    likeQuote(like)
                }
            }
        }
    }

    private fun QuotescardBinding.setupCard() {
        background.setOnClickListener {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                val mVibratePattern = longArrayOf(100, 150)
                vibrator.vibrate(mVibratePattern, -1) // for 500 ms
            }

            val popup = PopupMenu(context, background)
            popup.menuInflater.inflate(R.menu.menu, popup.menu)
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    item?.let {
                        when (it.itemId) {
                            R.id.quoteCopy -> {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("${quote.author}", quote.phrase)
                                clipboard.setPrimaryClip(clip)
                                return true
                            }
                            R.id.quoteShare -> {
                                val shareintent = Intent(Intent.ACTION_SEND)
                                shareintent.type = "text/pain"
                                shareintent.putExtra(Intent.EXTRA_SUBJECT, "Motiv")
                                shareintent.putExtra(Intent.EXTRA_TEXT, quote.phrase + " -" + quote.author)
                                context.startActivity(Intent.createChooser(shareintent, "Escolha onde quer compartilhar"))
                                return true
                            }
                            R.id.quoteReport -> {
                                val model = QuoteModel(quotePresenter).denunciar(quote)
                                return true
                            }
                            else -> return false
                        }
                    }
                    return false

                }
            })
            popup.show()
            popup.setOnDismissListener {

            }
        }
    }

    private fun loadUser(userID: String) {
        TODO("Not yet implemented")
    }


    fun setupFirebaseUser(user: FirebaseUser) {
        user.run {
            quotescardBinding.username.text = displayName
            Glide.with(context).load(photoUrl).into(quotescardBinding.userpic)
            quotescardBinding.userpic.setOnClickListener {
                goToMyUserPage()
            }
            quotescardBinding.username.setOnClickListener {
                goToMyUserPage()
            }
        }
    }

    fun data(): String {
        val postdia = Tools.convertDate(quote.data)
        val now = Calendar.getInstance().time
        //print("Date comparision ${now.compareTo(postdia)}")
        val fmt = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
        val dayCount = ((now.time - postdia.time) / 1000 / 60 / 60 / 24).toInt()
        return when {
            dayCount < 1 -> {
                "Hoje"
            }
            dayCount == 1 -> {
                "Ontem"
            }
            else -> {
                fmt.format(postdia)
            }
        }
    }

    fun loadLikes() {
        likePresenter.loadData()
    }

    fun likeQuote(like: Likes) {
        likePresenter.likeQuote(like)
    }

    fun deslikeQuote(like: Likes) {
        likePresenter.deslikeQuote(like)
    }

}