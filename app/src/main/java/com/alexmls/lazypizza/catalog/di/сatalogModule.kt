package com.alexmls.lazypizza.catalog.di

import com.alexmls.lazypizza.catalog.data.remote.FirestoreProductDataSource
import com.alexmls.lazypizza.catalog.data.repository.ProductRepositoryImpl
import com.alexmls.lazypizza.catalog.data.seed.ProductSeeder
import com.alexmls.lazypizza.catalog.domain.repo.ProductRepository
import com.alexmls.lazypizza.catalog.presentation.screens.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val catalogModule = module {
    viewModelOf(::HomeViewModel)

    single { FirestoreProductDataSource(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single { ProductSeeder(get()) }
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
}