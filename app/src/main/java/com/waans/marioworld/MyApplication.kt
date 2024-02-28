package com.waans.marioworld

import android.app.Application
import com.waans.mario_world.core.di.databaseModule
import com.waans.mario_world.core.di.networkModule
import com.waans.mario_world.core.di.repositoryModule
import com.waans.marioworld.di.useCaseModule
import com.waans.marioworld.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}