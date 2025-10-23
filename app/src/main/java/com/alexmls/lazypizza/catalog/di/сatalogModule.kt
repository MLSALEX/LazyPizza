package com.alexmls.lazypizza.catalog.di

import com.alexmls.lazypizza.app.di.AppScope
import com.alexmls.lazypizza.catalog.data.remote.FirestoreProductDataSource
import com.alexmls.lazypizza.catalog.data.remote.FirestoreToppingDataSource
import com.alexmls.lazypizza.catalog.data.repository.AddonsFromCatalog
import com.alexmls.lazypizza.catalog.data.repository.ProductRepositoryImpl
import com.alexmls.lazypizza.catalog.data.repository.ToppingRepositoryImpl
import com.alexmls.lazypizza.catalog.data.seed.ProductSeeder
import com.alexmls.lazypizza.catalog.domain.repo.ProductRepository
import com.alexmls.lazypizza.catalog.domain.repo.ToppingRepository
import com.alexmls.lazypizza.catalog.domain.usecase.AddProductToCartUseCase
import com.alexmls.lazypizza.catalog.presentation.screens.history.HistoryViewModel
import com.alexmls.lazypizza.catalog.presentation.screens.home.HomeViewModel
import com.alexmls.lazypizza.catalog.presentation.screens.product_details.ProductDetailsScreenViewModel
import com.alexmls.lazypizza.core.domain.catalog.CatalogReadApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val catalogModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProductDetailsScreenViewModel)
    viewModelOf(::HistoryViewModel)

    single { FirestoreProductDataSource(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get(AppScope)) }

    single { FirestoreToppingDataSource(get()) }
    single<ToppingRepository> { ToppingRepositoryImpl(get(), get(AppScope)) }

    single { ProductSeeder(get()) }
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    single<CatalogReadApi> { AddonsFromCatalog(get<ProductRepository>()) }

    single { AddProductToCartUseCase(get(), get(), get()) }
}