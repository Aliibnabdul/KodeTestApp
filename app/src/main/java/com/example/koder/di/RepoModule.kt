package com.example.koder.di

import com.example.koder.data.KoderRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module {
    single { KoderRepositoryImpl(get()) }
}