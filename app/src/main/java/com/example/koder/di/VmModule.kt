package com.example.koder.di

import com.example.koder.data.KoderRepositoryImpl
import com.example.koder.ui.main.MainFragmentVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel { MainFragmentVM(get<KoderRepositoryImpl>()) }
}