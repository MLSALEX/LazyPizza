package com.alexmls.lazypizza.cart.di

import com.alexmls.lazypizza.cart.data.repo.CartApiImpl
import com.alexmls.lazypizza.cart.data.repo.CartRepositoryImpl
import com.alexmls.lazypizza.cart.domain.repo.CartRepository
import com.alexmls.lazypizza.cart.domain.usecase.ObserveRecommendedAddonsUseCase
import com.alexmls.lazypizza.cart.presentation.screens.cart.CartViewModel
import com.alexmls.lazypizza.core.domain.cart.CartReadApi
import com.alexmls.lazypizza.core.domain.cart.CartWriteApi
import com.alexmls.lazypizza.core.domain.catalog.CatalogReadApi
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val cartModule = module {
    viewModelOf(::CartViewModel)

    single<CartRepository> { CartRepositoryImpl() }
    single<CartWriteApi> { CartApiImpl(get()) }
    single<CartReadApi>  { get<CartWriteApi>() as CartReadApi }

    factory { ObserveRecommendedAddonsUseCase(get<CatalogReadApi>(), get<CartRepository>()) }
}