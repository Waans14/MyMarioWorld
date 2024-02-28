package com.waans.marioworld.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.waans.mario_world.core.data.source.local.DummyDataSource
import com.waans.mario_world.core.domain.usecase.MarioUseCase

class MainViewModel(characterUseCase: MarioUseCase) : ViewModel() {
    val characters = characterUseCase.getAllCharacters().asLiveData()
    val news = DummyDataSource.getAllNews()
}

