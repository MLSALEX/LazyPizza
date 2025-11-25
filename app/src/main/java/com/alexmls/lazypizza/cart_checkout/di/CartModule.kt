package com.alexmls.lazypizza.cart_checkout.di

import androidx.room.Room
import com.alexmls.lazypizza.cart_checkout.data.local.CartDatabase
import com.alexmls.lazypizza.cart_checkout.data.local.GuestCartDao
import com.alexmls.lazypizza.cart_checkout.data.repo.CartApiImpl
import com.alexmls.lazypizza.cart_checkout.data.repo.GuestCartRepository
import com.alexmls.lazypizza.cart_checkout.data.repo.UserSessionCartRepository
import com.alexmls.lazypizza.cart_checkout.domain.model.PickupScheduleConstraints
import com.alexmls.lazypizza.cart_checkout.domain.repo.CartRepository
import com.alexmls.lazypizza.cart_checkout.domain.usecase.ObserveRecommendedAddonsUseCase
import com.alexmls.lazypizza.cart_checkout.presentation.screens.cart.CartViewModel
import com.alexmls.lazypizza.cart_checkout.presentation.screens.checkout.CheckoutViewModel
import com.alexmls.lazypizza.core.domain.cart.CartReadApi
import com.alexmls.lazypizza.core.domain.cart.CartWriteApi
import com.alexmls.lazypizza.core.domain.catalog.CatalogReadApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import java.time.LocalTime

val cartModule = module {
    viewModelOf(::CartViewModel)
    viewModelOf(::CheckoutViewModel)

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

    single {
        PickupScheduleConstraints(
            openTime = LocalTime.of(10, 15),
            closeTime = LocalTime.of(21, 45),
            minLeadMinutes = 15L
        )
    }
}