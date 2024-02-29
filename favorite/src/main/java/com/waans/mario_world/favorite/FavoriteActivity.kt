package com.waans.mario_world.favorite

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.waans.mario_world.core.ui.CharacterAdapter
import com.waans.mario_world.core.utils.MediaPlayerManager
import com.waans.mario_world.core.utils.SharedPreferencesManager
import com.waans.mario_world.favorite.databinding.ActivityFavoriteBinding
import com.waans.marioworld.ui.detail.DetailCharacterActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var bgSoundManager: MediaPlayerManager

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadKoinModules(favoriteModule)

        sharedPreferencesManager = SharedPreferencesManager(applicationContext)
        bgSoundManager = MediaPlayerManager(applicationContext)

        initView()
    }

    private fun initView() {
        with(binding) {
            btnBack.setOnClickListener {
                onBackPressed()
            }

            btnSound.setOnClickListener {
                setSoundStatus(isChangeStatus = true)
            }

            val characterAdapter = CharacterAdapter(this@FavoriteActivity)
            characterAdapter.onItemClick = { selectedData ->
                startActivity(DetailCharacterActivity.intent(this@FavoriteActivity, selectedData))
            }

            favoriteViewModel.favoriteCharacter.observe(this@FavoriteActivity) { data ->
                characterAdapter.setData(data)
                emptyAnimation.visibility = if (data.isNotEmpty()) View.GONE else View.VISIBLE
                rvFavoriteCharacters.visibility = if (data.isNotEmpty()) View.VISIBLE else View.GONE
            }

            with(binding.rvFavoriteCharacters) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = characterAdapter
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
            // set bg sound
            bgSoundManager.startSound(com.waans.mario_world.core.R.raw.bgm_super_mario_bos, true)
        } else {
            bgSoundManager.stopSound()
        }
    }

    override fun onResume() {
        super.onResume()

        initView()

        runBlocking {
            delay(1500)

            setSoundStatus()
        }
    }

    override fun onPause() {
        super.onPause()

        bgSoundManager.stopSound()
    }
}
