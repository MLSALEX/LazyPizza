package com.alexmls.lazypizza.app.di

import com.alexmls.lazypizza.app.App
import com.alexmls.lazypizza.core.common.AnonymousAuthInitializer
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as App).applicationScope
    }
    single { AnonymousAuthInitializer(get()) }
}