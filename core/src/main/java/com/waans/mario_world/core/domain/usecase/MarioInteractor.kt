package com.waans.mario_world.core.domain.usecase

import com.waans.mario_world.core.domain.model.Character
import com.waans.mario_world.core.domain.repository.IMarioRepository

class MarioInteractor(private val marioRepository: IMarioRepository): MarioUseCase {

    override fun getAllCharacters() = marioRepository.getAllCharacters()

    override fun getFavoriteCharacters() = marioRepository.getFavoriteCharacters()

    override fun setFavoriteCharacter(character: Character, state: Boolean) = marioRepository.setFavoriteCharacter(character, state)
}