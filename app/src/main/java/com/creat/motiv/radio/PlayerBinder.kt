package com.creat.motiv.radio

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.creat.motiv.databinding.PlayerLayoutBinding
import com.creat.motiv.radio.Radio
import com.creat.motiv.radio.RadioPresenter
import com.ilustris.animations.slideInBottom
import com.silent.ilustriscore.core.view.BaseView
import kotlin.random.Random

class PlayerBinder(override val viewBind: PlayerLayoutBinding) : BaseView<Radio>() {

    override val presenter = RadioPresenter(this)
    var mediaPlayer: MediaPlayer? = null
    override fun initView() {
        presenter.loadData()
    }


    fun playRadio(radio: Radio) {
        Handler().postDelayed({
            mediaPlayer?.stop()
            viewBind.playingAnimation.pauseAnimation()
            mediaPlayer = MediaPlayer().apply {
                val uri = Uri.parse(radio.url)
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(context, uri)
                prepareAsync()
                setOnPreparedListener {
                    start()
                    viewBind.playingAnimation.playAnimation()
                    viewBind.playingAnimation.setOnClickListener {
                        if (isPlaying) {
                            pause()
                            viewBind.playingAnimation.pauseAnimation()
                        } else {
                            start()
                            viewBind.playingAnimation.playAnimation()
                        }
                    }
                }
                setOnErrorListener { mediaPlayer, i, i2 ->
                    mediaPlayer.stop()
                    viewBind.playingAnimation.pauseAnimation()
                    false
                }
            }
        }, 2000)


    }

    override fun showListData(list: List<Radio>) {
        super.showListData(list)
        if (list.isNotEmpty()) {
            viewBind.radiosPager.run {
                adapter = StyleRadioAdapter(list.sortedBy {
                    it.name
                })
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        playRadio(list[position])
                    }
                })
                setCurrentItem(Random.nextInt(list.size - 1), true)
            }
        }
        if (viewBind.playerContainer.visibility == View.GONE) viewBind.playerContainer.slideInBottom()
    }


}