package com.creat.motiv.radio

import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.creat.motiv.databinding.PlayerLayoutBinding
import com.creat.motiv.radio.Radio
import com.creat.motiv.radio.RadioPresenter
import com.ilustris.animations.fadeIn
import com.ilustris.animations.fadeOut
import com.ilustris.animations.repeatFade
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.utils.delayedFunction
import com.silent.ilustriscore.core.view.BaseView
import kotlin.random.Random

class PlayerBinder(override val viewBind: PlayerLayoutBinding) : BaseView<Radio>() {

    override val presenter = RadioPresenter(this)
    var mediaPlayer: MediaPlayer? = null
    override fun initView() {
        viewBind.playingAnimation.hide()
        presenter.loadData()
    }


    fun playRadio(radio: Radio) {
        delayedFunction {
            mediaPlayer?.stop()
            mediaPlayer = MediaPlayer().apply {
                val uri = Uri.parse(radio.url)
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(context, uri)
                prepareAsync()
                setOnPreparedListener {
                    start()
                    startLoading()
                    viewBind.playerContainer.setOnClickListener {
                        if (isPlaying) {
                            pause()
                            stopLoading()
                        } else {
                            start()
                            startLoading()
                        }
                    }
                }
                setOnErrorListener { mediaPlayer, i, i2 ->
                    mediaPlayer.stop()
                    stopLoading()
                    false
                }
            }
        }

    }

    private fun stopLoading() {
        delayedFunction {
            viewBind.playingAnimation.hide()
        }

    }

    private fun startLoading() {
        delayedFunction {
            viewBind.playingAnimation.show()
        }

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
                setOnClickListener {
                    if (currentItem < list.size - 1) {
                        setCurrentItem(currentItem++, true)
                    } else {
                        setCurrentItem(0, true)
                    }
                }
                delayedFunction {
                    setCurrentItem(Random.nextInt(list.size - 1), true)
                }
            }
        }
        if (viewBind.playerContainer.visibility == View.GONE) viewBind.playerContainer.slideInBottom()
    }


}