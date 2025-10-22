package com.alexmls.lazypizza.app.di

import com.alexmls.lazypizza.app.App
import com.alexmls.lazypizza.app.presentation.shell.MainViewModel
import com.alexmls.lazypizza.core.common.AnonymousAuthInitializer
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AppScope = named("AppScope")

val appModule = module {
    single<CoroutineScope>(AppScope) {
        (androidApplication() as App).applicationScope
    }
    single { AnonymousAuthInitializer(get()) }
    viewModelOf(::MainViewModel)
}