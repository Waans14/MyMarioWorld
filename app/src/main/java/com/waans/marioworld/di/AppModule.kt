package com.waans.marioworld.di

import com.waans.mario_world.core.domain.usecase.MarioInteractor
import com.waans.mario_world.core.domain.usecase.MarioUseCase
import com.waans.marioworld.ui.detail.DetailCharacterViewModel
import com.waans.marioworld.ui.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<MarioUseCase> { MarioInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailCharacterViewModel(get()) }
}