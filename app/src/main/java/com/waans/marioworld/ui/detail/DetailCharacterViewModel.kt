package com.waans.marioworld.ui.detail

import androidx.lifecycle.ViewModel
import com.waans.mario_world.core.domain.model.Character
import com.waans.mario_world.core.domain.usecase.MarioUseCase

class DetailCharacterViewModel(private val marioUseCase: MarioUseCase) : ViewModel() {
    fun setFavoriteCharacter(character: Character, newStatus:Boolean) =
        marioUseCase.setFavoriteCharacter(character, newStatus)
}

