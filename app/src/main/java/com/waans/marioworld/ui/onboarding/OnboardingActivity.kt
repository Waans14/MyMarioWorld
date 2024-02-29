package com.waans.marioworld.ui.onboarding

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ncorti.slidetoact.SlideToActView
import com.waans.mario_world.core.utils.MediaPlayerManager
import com.waans.mario_world.core.utils.SharedPreferencesManager
import com.waans.marioworld.databinding.ActivityOnboardingBinding
import com.waans.marioworld.ui.main.MainActivity
import kotlinx.coroutines.runBlocking

class OnboardingActivity : AppCompatActivity(), SlideToActView.OnSlideToActAnimationEventListener {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var bgSoundManager: MediaPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesManager = SharedPreferencesManager(applicationContext)
        if (!sharedPreferencesManager.getIsMute()) {
            sharedPreferencesManager.setIsMute(false)
        }
    }

    override fun onResume() {
        super.onResume()
        bgSoundManager = MediaPlayerManager(applicationContext)
        setSoundStatus()

        initView()
    }

    override fun onPause() {
        super.onPause()
        bgSoundManager.stopSound()
    }

    private fun initView() {
        with(binding) {
            btnSlider.onSlideToActAnimationEventListener = this@OnboardingActivity

            btnSound.setOnClickListener {
                setSoundStatus(isChangeStatus = true)
            }

            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    marioTitle.layoutParams.width = 800
                }
                Configuration.ORIENTATION_LANDSCAPE, Configuration.ORIENTATION_SQUARE -> {
                    marioTitle.layoutParams.width = 600
                }
                Configuration.ORIENTATION_UNDEFINED -> {
                    marioTitle.layoutParams.width = 800
                }
            }
        }
    }

    private fun setSoundStatus(isChangeStatus: Boolean = false) {
        var currentStatus = sharedPreferencesManager.getIsMute()
        if (isChangeStatus) {
            sharedPreferencesManager.setIsMute(!currentStatus)
        }

        currentStatus = sharedPreferencesManager.getIsMute()
        if (currentStatus) {
            binding.btnSound.setImageResource(com.waans.mario_world.core.R.drawable.ic_volume_off)
        } else {
            binding.btnSound.setImageResource(com.waans.mario_world.core.R.drawable.ic_volume_up)
        }

        isPlayBgSound()
    }

    private fun isPlayBgSound() {
        val isMute = sharedPreferencesManager.getIsMute()

        if (!isMute) {
            bgSoundManager.startSound(com.waans.mario_world.core.R.raw.bgm_opening, true)
        } else {
            bgSoundManager.stopSound()
        }
    }

    override fun onSlideCompleteAnimationEnded(view: SlideToActView) {
        val intent = Intent(this@OnboardingActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSlideCompleteAnimationStarted(view: SlideToActView, threshold: Float) {
        runBlocking {
            bgSoundManager.stopSound()

            val mpManager = MediaPlayerManager(applicationContext)
            mpManager.startSound(com.waans.mario_world.core.R.raw.continue_sound, false)
        }
    }

    override fun onSlideResetAnimationEnded(view: SlideToActView) { }

    override fun onSlideResetAnimationStarted(view: SlideToActView) { }
}
