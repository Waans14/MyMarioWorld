package com.waans.mario_world.core.di

import androidx.room.Room
import com.waans.mario_world.core.data.MarioRepository
import com.waans.mario_world.core.data.source.local.LocalDataSource
import com.waans.mario_world.core.data.source.local.room.MarioDatabase
import com.waans.mario_world.core.data.source.remote.RemoteDataSource
import com.waans.mario_world.core.data.source.remote.network.ApiService
import com.waans.mario_world.core.domain.repository.IMarioRepository
import com.waans.mario_world.core.utils.AppExecutors
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<MarioDatabase>().marioDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            MarioDatabase::class.java, "Mario.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        val hostname = "waans14.github.io"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/qlJvUaRP4/Oodg/x84EZ52Ulu8y9eUHh++IjI8zJ2bc=")
            .add(hostname, "sha256/RQeZkB42znUfsDIIFWIRiYEcKl7nHwNFwWCrnMMJbVc=")
            .add(hostname, "sha256/r/mIkG3eEpVdm+u/ko/cwxzOMo1bk4TyHIlByibiA5E=")
            .build()
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/waans14/waans14.github.io/main/hosting/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IMarioRepository> {
        MarioRepository(
            get(),
            get(),
            get()
        )
    }
}