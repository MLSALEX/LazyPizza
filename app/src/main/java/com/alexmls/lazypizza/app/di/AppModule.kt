package com.alexmls.lazypizza.app.di

import com.alexmls.lazypizza.app.App
import com.alexmls.lazypizza.app.presentation.shell.MainViewModel
import com.alexmls.lazypizza.app.session.CartSessionOrchestrator
import com.alexmls.lazypizza.cart.data.repo.GuestCartRepository
import com.alexmls.lazypizza.cart.data.repo.SwitchingCartRepository
import com.alexmls.lazypizza.cart.data.repo.UserSessionCartRepository
import com.alexmls.lazypizza.cart.domain.repo.CartRepository
import com.alexmls.lazypizza.core.common.ActivityProvider
import com.alexmls.lazypizza.core.common.AnonymousAuthInitializer
import com.alexmls.lazypizza.core.domain.auth.AuthRepository
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AppScope = named("AppScope")

val appModule = module {

    single<ActivityProvider> {
        (androidApplication() as App).activityProvider
    }
    single<CoroutineScope>(AppScope) {
        (androidApplication() as App).applicationScope
    }
    single<CartRepository> {
        SwitchingCartRepository(
            auth = get<AuthRepository>(),
            guestRepo = get<GuestCartRepository>(),
            userRepo = get<UserSessionCartRepository>()
        )
    }
    single (createdAtStart = true){
        CartSessionOrchestrator(
            auth = get<AuthRepository>(),
            guestPort = get<GuestCartRepository>(),
            userPort = get<UserSessionCartRepository>(),
            scope = get(AppScope)
        ).apply { start() }
    }
    single { AnonymousAuthInitializer(get()) }
    viewModelOf(::MainViewModel)
}