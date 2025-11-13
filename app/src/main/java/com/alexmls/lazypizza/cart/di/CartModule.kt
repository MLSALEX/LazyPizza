package com.alexmls.lazypizza.cart.di

import androidx.room.Room
import com.alexmls.lazypizza.cart.data.local.CartDatabase
import com.alexmls.lazypizza.cart.data.local.GuestCartDao
import com.alexmls.lazypizza.cart.data.repo.CartApiImpl
import com.alexmls.lazypizza.cart.data.repo.GuestCartRepository
import com.alexmls.lazypizza.cart.data.repo.UserSessionCartRepository
import com.alexmls.lazypizza.cart.domain.repo.CartRepository
import com.alexmls.lazypizza.cart.domain.usecase.ObserveRecommendedAddonsUseCase
import com.alexmls.lazypizza.cart.presentation.screens.cart.CartViewModel
import com.alexmls.lazypizza.core.domain.cart.CartReadApi
import com.alexmls.lazypizza.core.domain.cart.CartWriteApi
import com.alexmls.lazypizza.core.domain.catalog.CatalogReadApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val cartModule = module {
    viewModelOf(::CartViewModel)


    single<CartWriteApi> { CartApiImpl(get()) }
    single<CartReadApi>  { get<CartWriteApi>() as CartReadApi }

    factory { ObserveRecommendedAddonsUseCase(get<CatalogReadApi>(), get<CartRepository>()) }

    single {
        Room.databaseBuilder(
            androidContext(),
            CartDatabase::class.java,
            CartDatabase.NAME
        )
            // .fallbackToDestructiveMigration() //  dev only
            .build()
    }
    single<GuestCartDao> { get<CartDatabase>().guestCartDao() }
    single { GuestCartRepository(get()) }
    single { UserSessionCartRepository() }
}