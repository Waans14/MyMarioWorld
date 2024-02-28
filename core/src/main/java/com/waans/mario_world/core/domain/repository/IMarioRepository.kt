package com.waans.mario_world.core.domain.repository

import com.waans.mario_world.core.data.Resource
import com.waans.mario_world.core.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface IMarioRepository {

    fun getAllCharacters(): Flow<Resource<List<Character>>>

    fun getFavoriteCharacters(): Flow<List<Character>>

    fun setFavoriteCharacter(character: Character, state: Boolean)

}