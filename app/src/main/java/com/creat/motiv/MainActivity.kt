package com.creat.motiv

import android.app.Activity
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.creat.motiv.databinding.ActivityMainBinding
import com.creat.motiv.features.radio.RadioAdapter
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ilustris.animations.slideInBottom
import com.ilustris.motiv.base.beans.Radio
import com.ilustris.ui.extensions.gone
import com.ilustris.ui.extensions.showSnackBar
import com.ilustris.ui.extensions.visible
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.delayedFunction
import kotlin.random.Random


open class MainActivity : AppCompatActivity() {

    private val mainViewModel by lazy { MainViewModel(application) }
    var mainActBind: ActivityMainBinding? = null
    var radioAdapter: RadioAdapter? = null
    var mediaPlayer = MediaPlayer()

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            recreate()
        } else {
            mainActBind?.root?.showSnackBar("Ocorreu um erro ao fazer login")
            login()
        }
    }


    private fun login() {
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActBind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActBind!!.root)
        mainActBind?.setupView()
        observeViewModel()
        mainViewModel.getAllData()
    }

    private fun observeViewModel() {
        mainViewModel.viewModelState.observe(this) {
            when (it) {
                ViewModelBaseState.RequireAuth -> {
                    login()
                }
                is ViewModelBaseState.DataListRetrievedState -> {
                    setupRadios(it.dataList as ArrayList<Radio>)
                }
                is ViewModelBaseState.ErrorState -> {
                    if (it.dataException.code == ErrorType.AUTH) {
                        login()
                    }
                    mainActBind?.root?.showSnackBar(it.dataException.code.message)
                }
                else -> {}
            }
        }
    }

    private fun setupRadios(radios: ArrayList<Radio>) {
        mainActBind?.run {
            val bottomSheetBehavior = BottomSheetBehavior.from(playerView.root)
            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HALF_EXPANDED || newState == BottomSheetBehavior.STATE_EXPANDED) {
                        playerView.radioTitle.visible()
                    } else {
                        playerView.radioTitle.gone()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (slideOffset >= 0.5f) playerView.radioTitle.visible() else playerView.radioTitle.gone()
                    playerView.radioTitle.alpha = slideOffset
                }

            })
            radioAdapter = RadioAdapter(radios) {
                playRadio(it)
            }
            playerView.radiosPager.adapter = radioAdapter
            delayedFunction {
                val randomRadio = radios[Random.nextInt(radios.size)]
                playRadio(randomRadio)
            }
            playerView.root.slideInBottom()
        }

    }

    private fun playRadio(radio: Radio) {
        radioAdapter?.let {
            if (it.currentRadio?.id != radio.id) {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    it.updatePlaying(false)
                }
                mediaPlayer.run {
                    reset()
                    val uri = Uri.parse(radio.url)
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                    setDataSource(this@MainActivity, uri)
                    prepareAsync()
                    setOnPreparedListener {
                        setVolume(0.3f, 0.3f)
                        start()
                        radioAdapter?.updateCurrentRadio(radio)
                        radioAdapter?.updatePlaying(mediaPlayer.isPlaying)
                        radioAdapter?.updateEnabled(true)
                    }
                    setOnErrorListener { mp, what, extra ->
                        if (mp.isPlaying) {
                            mp.stop()
                        }
                        mainActBind?.root?.showSnackBar("Ocorreu um erro ao reproduzir a radio")
                        radioAdapter?.updateEnabled(true)
                        false
                    }
                }
            } else {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                } else {
                    mediaPlayer.start()
                }
                radioAdapter?.updatePlaying(mediaPlayer.isPlaying)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun ActivityMainBinding.setupView() {
        val navController = findNavController(R.id.nav_host_fragment)
        //navView.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_profile,
                R.id.navigation_settings,
                R.id.navigation_new_quote
            )
        )
        mainActBind?.run {
            //mainToolbar.setupWithNavController(navController, appBarConfiguration)
            //setSupportActionBar(mainToolbar)
        }

    }

}
