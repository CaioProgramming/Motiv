package com.creat.motiv.radio

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.creat.motiv.databinding.PlayerLayoutBinding
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.utils.delayedFunction
import com.silent.ilustriscore.core.view.BaseView
import kotlin.random.Random

class PlayerBinder(override val viewBind: PlayerLayoutBinding) : BaseView<Radio>() {

    override val presenter = RadioPresenter(this)
    var mediaPlayer: MediaPlayer? = null
    var radioAdapter: StyleRadioAdapter? = null
    override fun initView() {
        presenter.loadData()

    }


    fun playRadio(radio: Radio) {
        delayedFunction {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                    stopLoading()
                }
            }
            radioAdapter?.updatePlayerStatus(mediaPlayer?.isPlaying ?: false)
            mediaPlayer = MediaPlayer().apply {
                val uri = Uri.parse(radio.url)
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(context, uri)
                prepareAsync()
                setOnPreparedListener {
                    start()
                    startLoading()
                    radioAdapter?.updatePlayerStatus(isPlaying)
                }
                setOnErrorListener { mediaPlayer, i, i2 ->
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.stop()
                    }
                    stopLoading()
                    try {
                        viewBind.radiosPager.setCurrentItem(currentPosition + 1, true)
                    } catch (e: Exception) {
                        try {
                            viewBind.radiosPager.setCurrentItem(currentPosition - 1, true)
                        } catch (e: Exception) {
                            viewBind.radiosPager.setCurrentItem(currentPosition + 1, true)
                        }
                    }
                    false
                }
            }
        }

    }

    private fun stopLoading() {
        viewBind.radiosPager.isEnabled = true
        delayedFunction {
            radioAdapter?.updatePlayerStatus(false)
            viewBind.playingAnimation.pauseAnimation()
        }

    }

    private fun startLoading() {
        viewBind.radiosPager.isEnabled = false
        delayedFunction {
            radioAdapter?.updatePlayerStatus(true)
            viewBind.playingAnimation.playAnimation()
        }

    }

    override fun showListData(list: List<Radio>) {
        super.showListData(list)
        if (list.isNotEmpty()) {
            viewBind.playingAnimation.setOnClickListener {
                mediaPlayer?.let {
                    if (!it.isPlaying) {
                        it.start()
                        startLoading()
                    } else {
                        it.pause()
                        stopLoading()
                    }
                }
            }
            viewBind.radiosPager.run {
                radioAdapter = StyleRadioAdapter(list.sortedBy {
                    it.name
                }) {
                    mediaPlayer?.let {
                        if (it.isPlaying) {
                            it.pause()
                            stopLoading()
                        } else {
                            it.start()
                            startLoading()
                        }
                        radioAdapter?.updatePlayerStatus(it.isPlaying)
                    }
                }
                adapter = radioAdapter
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