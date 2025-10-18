package com.alexmls.lazypizza.catalog.di

import com.alexmls.lazypizza.catalog.data.remote.FirestoreProductDataSource
import com.alexmls.lazypizza.catalog.data.remote.FirestoreToppingDataSource
import com.alexmls.lazypizza.catalog.data.repository.CartRepositoryImpl
import com.alexmls.lazypizza.catalog.data.repository.ProductRepositoryImpl
import com.alexmls.lazypizza.catalog.data.repository.ToppingRepositoryImpl
import com.alexmls.lazypizza.catalog.data.seed.ProductSeeder
import com.alexmls.lazypizza.catalog.domain.repo.CartRepository
import com.alexmls.lazypizza.catalog.domain.repo.ProductRepository
import com.alexmls.lazypizza.catalog.domain.repo.ToppingRepository
import com.alexmls.lazypizza.catalog.domain.usecase.AddToCartUseCase
import com.alexmls.lazypizza.catalog.domain.usecase.ObserveCartCountUseCase
import com.alexmls.lazypizza.catalog.presentation.screens.cart.CartViewModel
import com.alexmls.lazypizza.catalog.presentation.screens.history.HistoryViewModel
import com.alexmls.lazypizza.catalog.presentation.screens.home.HomeViewModel
import com.alexmls.lazypizza.catalog.presentation.screens.product_details.ProductDetailsScreenViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val catalogModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProductDetailsScreenViewModel)
    viewModelOf(::CartViewModel)
    viewModelOf(::HistoryViewModel)


    single { FirestoreProductDataSource(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }

    single { FirestoreToppingDataSource(get()) }
    single<ToppingRepository> { ToppingRepositoryImpl(get()) }

    single<CartRepository> { CartRepositoryImpl() }
    factory { AddToCartUseCase(get<CartRepository>()) }
    factory { ObserveCartCountUseCase(get<CartRepository>()) }

    single { ProductSeeder(get()) }
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
}